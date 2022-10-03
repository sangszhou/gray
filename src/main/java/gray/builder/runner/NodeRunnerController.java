package gray.builder.runner;

import gray.builder.NodeDao;
import gray.builder.annotation.Input;
import gray.builder.flow.FlowService;
import gray.dag.Task;
import gray.domain.StageResult;
import gray.engine.*;
import gray.util.ClzUtils;
import gray.util.ParamLinkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class NodeRunnerController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeDao nodeDao;
    @Autowired
    FlowService flowService;
    @Autowired
    FlowNodeRunner flowNodeRunner;
    @Autowired
    BlockNodeRunner blockNodeRunner;
    @Autowired
    ManyNodeRunner manyNodeRunner;
    @Autowired
    RootNodeRunner rootNodeRunner;
    @Autowired
    BasicNodeRunner basicNodeRunner;

    @Scheduled(fixedRate = 10*1000)
    public void trigger() {
        List<Node> initNode = nodeDao.queryByStatus(NodeStatus.INIT);
        logger.info("trigger cron job running, init node number: {}", initNode.size());
        for (Node node : initNode) {
            switch (node.getType()) {
                case MANY:
                    manyNodeRunner.trigger(node);
                case ATOM:
                    basicNodeRunner.trigger(node);
                case BLOCK:
                    blockNodeRunner.trigger(node);
                case ROOT:
                    rootNodeRunner.trigger(node);
                case FLOW:
                    flowNodeRunner.triggerNode(node);
                default:
                    logger.error("unsupported node type: " + node.getType());
            }
        }
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void query() {
        List<Node> runningNode = nodeDao.queryByStatus(NodeStatus.QUERY);
        for (Node node : runningNode) {
            switch (node.getType()) {
                case MANY:
                    manyNodeRunner.query(node);
                case BLOCK:
                    blockNodeRunner.query(node);
                case ATOM:
                    basicNodeRunner.query(node);
                default:
                    logger.error("unsupported query node: {}", node.getType());
            }
        }
    }

    @Scheduled(fixedRate = 5*1000)
    public void waitNode() {
        // 驱动节点前进
        // 如果一个节点的前驱节点全部结束, 那么此节点结束
        List<Node> waitNodeList = nodeDao.queryByStatus(NodeStatus.INVALID);
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

//    public void buildAtomTaskNode(Node basicNode) {
//        // 1. 获取所有 input 注解标注的
//        // 2. 因为是存储过的, 所以 class 需要从 clzName 中获取,
//        // 这里为了简单起见, 不再从 clz name 中获取
//        // 3.
////        String clzName = basicNode.getClzName();
//        Class clz = ClzUtils.castTo(basicNode.getTaskClzName());
//        List<Field> fieldList = ClzUtils.getFieldsWithAnnotation(clz, Input.class);
//
//        for (int i = 0; i < basicNode.getParamLinkerList().size(); i++) {
//            ParamLinker paramLinker = basicNode.getParamLinkerList().get(i);
//            ParamLinkerType paramLinkType = paramLinker.getParamLinkerType();
//            if (paramLinkType.equals(ParamLinkerType.STATIC)) {
//                // 静态 const 类型, 直接根据目标类型进行转换
//                NodeData staticNodeData = ParamLinkUtils.buildStatic(paramLinker.getDestFieldName(), );
//
//            } else {
//                // todo 还有一个 flow input 需要处理
//                // 动态, 需要根据目标类型进行转换
//                String sourceTaskName = paramLinker.getSourceTaskName();
//                String flowId = basicNode.getFlowId();
//                Node sourceNode = nodeDao.getByName(flowId, basicNode.getNodeName());
//                List<NodeData> nodeDataList = sourceNode.getNodeDataList();
//
//                // 表达字段
//                Optional<NodeData> targetData = nodeDataList
//                        .stream()
//                        .filter(nodeData -> {
//                            return nodeData.getFieldName().equals(paramLinker.getSourceFieldName());
//                        }).findFirst();
//                if (!targetData.isPresent()) {
//                    logger.error("failed to find target data");
//                }
//
//                // 目标字段类型
//                Optional<Field> targetField = inputFieldList
//                        .stream()
//                        .filter(inputField -> inputField.getName().equals(paramLinker.getDestFieldName()))
//                        .findFirst();
//                if (!targetData.isPresent()) {
//                    logger.error("failed to find target field");
//                }
//
//                // 如果是数组, 支持吗?
//
//            }
//        }
//
//    }

    public void buildClusterNode(Node basicNode) {

    }

}
