package gray.builder.runner;

import com.alibaba.fastjson.JSON;
import gray.builder.ComposerBuilder;
import gray.builder.flow.FlowService;
import gray.domain.FlowInput;
import gray.domain.StageResult;
import gray.engine.Node;
import gray.engine.NodeData;
import gray.engine.ParamLinker;
import gray.engine.ParamLinkerType;
import gray.util.ClzUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlowNodeRunner {
    @Autowired
    FlowService flowService;

    public StageResult triggerNode(Node flowNode) {
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
        // 直接触发子 flow 的执行, 和触发逻辑是一样的
        // flow service 要怎么初始化呢?
        Class<? extends ComposerBuilder> flowClz = ClzUtils.castTo(flowClzName);
        String subFlowId = flowService.startFlow(flowClz, subFlowInput);
        NodeData nodeData = new NodeData();
        nodeData.setFieldName("SystemInnerFlowId");
        nodeData.setContent(subFlowId);
        flowNode.getNodeDataList().add(nodeData);
        // todo 存储到数据库
        return null;
    }

    public StageResult query(Node flowNode) {
        return null;
    }

}
