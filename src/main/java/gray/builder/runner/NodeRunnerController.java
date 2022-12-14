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
        // wrapper ?????????????????????, wrapper ??????????????????????????????
        Node queryParam = new Node();
        queryParam.setStatus(NodeStatus.INVALID);
        List<Node> waitNodeList = nodeService.query(queryParam);
        for (Node node : waitNodeList) {
            String wrapperId = node.getWrapperId();
            if (wrapperId != null) {
                // ??????????????? wrapper ?????? query ??????
                Node wrapperNode = nodeService.getById(wrapperId);
                if (wrapperNode.getStatus().equals(NodeStatus.INVALID) ||
                        wrapperNode.getStatus().equals(NodeStatus.INIT)) {
                    // ????????????????????????
                    continue;
                }
                // wrapper ???????????? query ??????, ?????????????????? success, fail ??????
                // ??????????????????????????????, ???????????????????????????????????????
            }
            String preId = node.getPreId();
            if (preId == null) {
                // ??????????????????, ??????????????????
                node.setStatus(NodeStatus.INIT);
                nodeService.save(node);
                continue;
            }

            // ?????????????????????, ?????????????????????????????????????????????, ?????????????????????
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
        // ??????????????????
        // ?????????????????????????????????????????????, ?????????????????????
        // ?????????????????? event driver ?????????, ????????????????????????, ?????????????????????
        Node queryParam = new Node();
        queryParam.setStatus(NodeStatus.INVALID);

        List<Node> waitNodeList = nodeService.query(queryParam);
        for (Node node : waitNodeList) {
            String preId = node.getPreId();
            if (preId == null) {
                // ?????? error
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
//        // 1. ???????????? input ???????????????
//        // 2. ?????????????????????, ?????? class ????????? clzName ?????????,
//        // ????????????????????????, ????????? clz name ?????????
//        // 3.
////        String clzName = basicNode.getClzName();
//        Class clz = ClzUtils.castTo(basicNode.getTaskClzName());
//        List<Field> fieldList = ClzUtils.getFieldsWithAnnotation(clz, Input.class);
//
//        for (int i = 0; i < basicNode.getParamLinkerList().size(); i++) {
//            ParamLinker paramLinker = basicNode.getParamLinkerList().get(i);
//            ParamLinkerType paramLinkType = paramLinker.getParamLinkerType();
//            if (paramLinkType.equals(ParamLinkerType.STATIC)) {
//                // ?????? const ??????, ????????????????????????????????????
//                NodeData staticNodeData = ParamLinkUtils.buildStatic(paramLinker.getDestFieldName(), );
//
//            } else {
//                // todo ???????????? flow input ????????????
//                // ??????, ????????????????????????????????????
//                String sourceTaskName = paramLinker.getSourceTaskName();
//                String flowId = basicNode.getFlowId();
//                Node sourceNode = nodeDao.getByName(flowId, basicNode.getNodeName());
//                List<NodeData> nodeDataList = sourceNode.getNodeDataList();
//
//                // ????????????
//                Optional<NodeData> targetData = nodeDataList
//                        .stream()
//                        .filter(nodeData -> {
//                            return nodeData.getFieldName().equals(paramLinker.getSourceFieldName());
//                        }).findFirst();
//                if (!targetData.isPresent()) {
//                    logger.error("failed to find target data");
//                }
//
//                // ??????????????????
//                Optional<Field> targetField = inputFieldList
//                        .stream()
//                        .filter(inputField -> inputField.getName().equals(paramLinker.getDestFieldName()))
//                        .findFirst();
//                if (!targetData.isPresent()) {
//                    logger.error("failed to find target field");
//                }
//
//                // ???????????????, ??????????
//
//            }
//        }
//
//    }

    public void buildClusterNode(Node basicNode) {

    }

}
