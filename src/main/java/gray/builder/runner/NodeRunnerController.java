package gray.builder.runner;

import gray.builder.NodeDao;
import gray.builder.annotation.Input;
import gray.builder.flow.FlowService;
import gray.dag.Task;
import gray.domain.StageResult;
import gray.engine.*;
import gray.service.NodeService;
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
    NodeService nodeService;
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
        Node queryParam = new Node();
        queryParam.setStatus(NodeStatus.INIT);

        List<Node> initNode = nodeService.query(queryParam);
        logger.info("trigger cron job running, init node number: {}", initNode.size());
        for (Node node : initNode) {
            switch (node.getType()) {
                case MANY:
                    manyNodeRunner.trigger(node);
                    break;
                case ATOM:
                    basicNodeRunner.trigger(node);
                    break;
                case BLOCK:
                    blockNodeRunner.trigger(node);
                    break;
                case ROOT:
                    rootNodeRunner.trigger(node);
                    break;
                case FLOW:
                    flowNodeRunner.triggerNode(node);
                    break;
                default:
                    logger.error("unsupported node type: " + node.getType());
            }
        }
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void query() {
        Node queryParam = new Node();
        queryParam.setStatus(NodeStatus.QUERY);

        List<Node> runningNode = nodeService.query(queryParam);
        for (Node node : runningNode) {
            switch (node.getType()) {
                case MANY:
                    manyNodeRunner.query(node);
                case BLOCK:
                    blockNodeRunner.query(node);
                case ATOM:
                    basicNodeRunner.query(node);
                case ROOT:
                    rootNodeRunner.query(node);
                case FLOW:
                    flowNodeRunner.query(node);
                default:
                    logger.error("unsupported query node: {}", node.getType());
            }
        }
    }

    @Scheduled(fixedRate = 5*1000)
    public void wrapperExecuting() {
        // wrapper 节点执行结束后, wrapper 内的函数可以开始执行
        Node queryParam = new Node();
        queryParam.setStatus(NodeStatus.INVALID);
        List<Node> waitNodeList = nodeService.query(queryParam);
        for (Node node : waitNodeList) {
            String wrapperId = node.getWrapperId();
            if (wrapperId != null) {
                // 需要先确保 wrapper 进入 query 阶段
                Node wrapperNode = nodeService.getById(wrapperId);
                if (wrapperNode.getStatus().equals(NodeStatus.INVALID) ||
                        wrapperNode.getStatus().equals(NodeStatus.INIT)) {
                    // 前序节点还未还是
                    continue;
                }
                // wrapper 节点进入 query 状态, 可能不能处于 success, fail 阶段
                // 或者可以处于上述阶段, 但是需要更加仔细的判断状态
            }
            String preId = node.getPreId();
            if (preId == null) {
                // 没有前序阶段, 可以继续执行
                node.setStatus(NodeStatus.INIT);
                nodeService.save(node);
                continue;
            }

            // 如果有前序节点, 那么只有在前序阶段成功的情况下, 才会继续往下走
            Node preNode = nodeService.getById(preId);
            if (preNode.getStatus().equals(NodeStatus.SUCCESS)) {
                node.setStatus(NodeStatus.INIT);
                nodeService.save(node);
                return;
            } else if (preNode.getStatus().equals(NodeStatus.FAIL)) {
                node.setStatus(NodeStatus.SKIPPED);
                nodeService.save(node);
                return;
            } else if (preNode.getStatus().equals(NodeStatus.SKIPPED)) {
                node.setStatus(NodeStatus.SKIPPED);
                nodeService.save(node);
                return;
            }
        }
    }

//    @Scheduled(fixedRate = 5*1000)
    public void waitNode() {
        // deprecated
        // 驱动节点前进
        // 如果一个节点的前驱节点全部结束, 那么此节点结束
        // 本来可以通过 event driver 驱动的, 这里为了简单期间, 先搞成定时任务
        Node queryParam = new Node();
        queryParam.setStatus(NodeStatus.INVALID);

        List<Node> waitNodeList = nodeService.query(queryParam);
        for (Node node : waitNodeList) {
            String preId = node.getPreId();
            if (preId == null) {
                // 返回 error
                continue;
            }
            Node preNode = nodeService.getById(preId);
            if (preNode.getStatus().equals(NodeStatus.SUCCESS)) {
                node.setStatus(NodeStatus.INIT);
                nodeService.save(node);
                return;
            } else if (preNode.getStatus().equals(NodeStatus.FAIL)) {
                node.setStatus(NodeStatus.SKIPPED);
                nodeService.save(node);
                return;
            } else if (preNode.getStatus().equals(NodeStatus.SKIPPED)) {
                node.setStatus(NodeStatus.SKIPPED);
                nodeService.save(node);
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
