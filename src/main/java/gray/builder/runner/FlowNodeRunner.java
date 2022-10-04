package gray.builder.runner;

import com.alibaba.fastjson.JSON;
import gray.builder.ComposerBuilder;
import gray.builder.flow.FlowService;
import gray.domain.FlowInput;
import gray.domain.StageResult;
import gray.engine.*;
import gray.service.NodeService;
import gray.util.ClzUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlowNodeRunner {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    FlowService flowService;
    @Autowired
    NodeService nodeService;

    public StageResult triggerNode(Node flowNode) {
        // 1. 子 flow 的 parent flow 怎么传递下来呢?
        logger.info("flow node trigger node...");
        // 嵌套 flow 的执行逻辑
        String flowClzName = flowNode.getFlowClzName();
        Optional<ParamLinker> flowInputLinker = flowNode.getParamLinkerList().stream()
                .filter(it -> it.getParamLinkerType().equals(ParamLinkerType.FLOW_INPUT))
                .findFirst();
        FlowInput subFlowInput = null;
        if (flowInputLinker.isPresent()) {
            String data = flowInputLinker.get().getSourceValueData();
            subFlowInput = JSON.parseObject(data, FlowInput.class);
        }

        // 这里, flow input 不能为空, 至少要有一个 flow input
        if (subFlowInput == null) {
            logger.error("sub flow input cannot be null");
            return StageResult.FailResult();
        }

        // 直接触发子 flow 的执行, 和触发逻辑是一样的
        // flow service 要怎么初始化呢?
        Class<? extends ComposerBuilder> flowClz = ClzUtils.castTo(flowClzName);
        String subFlowId = flowService.startFlow(flowClz, subFlowInput);

        // 父亲节点不需要记录孩子节点的信息
//        NodeData nodeData = new NodeData();
//        nodeData.setFieldName(FLOW_I);
//        nodeData.setContent(subFlowId);
//        flowNode.getNodeDataList().add(nodeData);
        flowNode.setStatus(NodeStatus.QUERY);
        nodeService.save(flowNode);
        // todo 存储到数据库
        return StageResult.SuccessResult();
    }

    public StageResult query(Node flowNode) {
        logger.info("flow node runner querying...");
        NodeStatus nodeStatus = flowService.flowStatus(flowNode.getFlowId());
        if (nodeStatus.equals(NodeStatus.SUCCESS)) {
            return StageResult.SuccessResult();
        } else if (nodeStatus.equals(NodeStatus.FAIL)) {
            return StageResult.FailResult();
        }
        // running 的就不再更新了
        return null;
    }

}
