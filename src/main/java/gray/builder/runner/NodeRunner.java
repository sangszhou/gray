package gray.builder.runner;

import gray.builder.NodeDao;
import gray.builder.annotation.Input;
import gray.dag.Task;
import gray.domain.StageResult;
import gray.engine.Node;
import gray.engine.NodeData;
import gray.engine.NodeStatus;
import gray.engine.ParamLinker;
import gray.util.ClzUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class NodeRunner {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeDao nodeDao;

    // rate 10 seconds
    @Scheduled(fixedRate = 5*1000)
    public void trigger() {
        logger.info("trigger cron job running");
        List<Node> initNode = nodeDao.queryNodes(NodeStatus.INIT);
        for (Node node : initNode) {
            switch (node.getType()) {
                case MANY:
                    TriggerMany(node);
                case ATOM:
                    triggerBasic(node);
                case BLOCK:
                    triggerBlock(node);
                case ROOT:
                    triggerRoot(node);
                default:
                    System.out.println("unsupported node type: " + node.getType());
            }
        }
    }

    @Scheduled(fixedRate = 5 * 1000)
    public void query() {
        List<Node> runningNode = nodeDao.queryNodes(NodeStatus.QUERY);
        for (Node node : runningNode) {
            switch (node.getType()) {
                case MANY:
                    queryMany(node);
                case BLOCK:
                    queryBlock(node);
                case ATOM:
                    queryBasic(node);
                default:
                    logger.error("unsupported query node");
            }
        }
    }

    @Scheduled(fixedRate = 5*1000)
    public void waitNode() {
        List<Node> waitNodeList = nodeDao.queryNodes(NodeStatus.INVALID);
        for (Node node : waitNodeList) {
            String preId = node.getPreId();
            if (preId == null) {
                // 返回 error
                continue;
            }
            Node preNode = nodeDao.getById(preId);
            if (preNode.getStatus().equals(NodeStatus.SUCCESS)) {
                node.setStatus(NodeStatus.INIT);
                nodeDao.save(node);
                return;
            } else if (preNode.getStatus().equals(NodeStatus.FAIL)) {
                node.setStatus(NodeStatus.SKIPPED);
                nodeDao.save(node);
                return;
            } else if (preNode.getStatus().equals(NodeStatus.SKIPPED)) {
                node.setStatus(NodeStatus.SKIPPED);
                nodeDao.save(node);
                return;
            }
        }
    }

    public void triggerRoot(Node root) {
           root.setStatus(NodeStatus.QUERY);
    }

    public void queryRoot(Node root) {

    }

    public void queryWrapperNode(Node wrapNode) {
        Node wrapperSelector = new Node();
        wrapperSelector.setWrapperId(wrapNode.getId());
        List<Node> wrappedNode = nodeDao.queryBySelector(wrapperSelector);
        if (wrappedNode.size() == 0) {
            wrapNode.setStatus(NodeStatus.SUCCESS);
            nodeDao.save(wrapNode);
            return;
        }
        boolean hasFail = false;
        for (Node node : wrappedNode) {
            if (node.getStatus() != NodeStatus.FAIL && node.getStatus() != NodeStatus.SUCCESS) {
                return;
            }
            if (node.getStatus().equals(NodeStatus.FAIL)) {
                hasFail = true;
            }
        }
        if (hasFail) {
            wrapNode.setStatus(NodeStatus.FAIL);
        } else {
            wrapNode.setStatus(NodeStatus.SUCCESS);
        }
        nodeDao.save(wrapNode);
    }


    public void triggerBlock(Node blockNode) {
        blockNode.setStatus(NodeStatus.QUERY);
    }

    public void queryBlock(Node blockNode) {
        queryWrapperNode(blockNode);
    }

    public void TriggerMany(Node manyNode) {
        manyNode.setStatus(NodeStatus.QUERY);
    }

    public void queryMany(Node manyNode) {
        queryWrapperNode(manyNode);
    }


    public void triggerBasic(Node basicNode) {
        Class clz = basicNode.getClass();
        Task taskInst = null;
        try {
            taskInst = (Task) clz.newInstance();
            // todo 所有 input 都要 set 进去. 这里还得区分 composerBuilder 和 taskBuilder
            StageResult stageResult = taskInst.execute();
            if (stageResult.getCode() == 2) {
                // success, 进入到 query 阶段
                basicNode.setStatus(NodeStatus.QUERY);
            } else {
                // fail
                basicNode.setStatus(NodeStatus.FAIL);
            }
            nodeDao.save(basicNode);
        } catch (InstantiationException exp) {
            logger.error("init task failed", exp);
        } catch (IllegalAccessException exp) {
            logger.error("init task failed", exp);
        }
    }

    public void buildAtomTaskNode(Node basicNode) {
        // 1. 获取所有 input 注解标注的
        // 2. 因为是存储过的, 所以 class 需要从 clzName 中获取,
        // 这里为了简单起见, 不再从 clz name 中获取
        // 3.
//        String clzName = basicNode.getClzName();
        Class clz = ClzUtils.castTo(basicNode.getClzName());
        List<Field> fieldList = ClzUtils.getFieldsWithAnnotation(clz, Input.class);

//        Field[] fieldList = clz.getFields();
//        List<Field> inputFieldList = new LinkedList<>();
//        // 查找所有的 input field 字段
//        for (int i = 0; i < fieldList.length; i++) {
//            Field field = fieldList[i];
//            Annotation[] annotations = field.getAnnotations();
//            for (int j = 0; j < annotations.length; j++) {
//                if (annotations[j].annotationType().equals(Input.class)) {
//                    inputFieldList.add(field);
//                }
//            }
//        }

        for (int i = 0; i < basicNode.getParamLinkerList().size(); i++) {
            ParamLinker paramLinker = basicNode.getParamLinkerList().get(i);
            String paramLinkType = paramLinker.getType();
            if (paramLinkType.equals("const")) {
                // 静态, 直接根据目标格式
            } else {
                // 动态, 需要根据目标类型进行转换
                String sourceTaskName = paramLinker.getSourceTaskName();
                String flowId = basicNode.getFlowId();
                Node sourceNode = nodeDao.getByName(flowId, basicNode.getNodeName());
                List<NodeData> nodeDataList = sourceNode.getNodeDataList();

                // 表达字段
                Optional<NodeData> targetData = nodeDataList
                        .stream()
                        .filter(nodeData -> {
                            return nodeData.getFieldName().equals(paramLinker.getSourceFieldName());
                        }).findFirst();
                if (!targetData.isPresent()) {
                    logger.error("failed to find target data");
                }

                // 目标字段类型
                Optional<Field> targetField = inputFieldList
                        .stream()
                        .filter(inputField -> inputField.getName().equals(paramLinker.getDestFieldName()))
                        .findFirst();
                if (!targetData.isPresent()) {
                    logger.error("failed to find target field");
                }

                // 如果是数组, 支持吗?

            }
        }

    }

    public void buildClusterNode(Node basicNode) {

    }

    public void queryBasic(Node basicNode) {
        Class clz = basicNode.getClass();
        Task taskInst = null;
        try {
            taskInst = (Task) clz.newInstance();
            StageResult stageResult = taskInst.query();
            if (stageResult.getCode() == 2) {
                basicNode.setStatus(NodeStatus.SUCCESS);
            } else if (stageResult.getCode() == 3){
                basicNode.setStatus(NodeStatus.FAIL);
            } else {
                // continue
                logger.info("query basic node, running");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
