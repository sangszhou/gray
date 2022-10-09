package gray.controller;

import gray.demo.SimpleComposer;
import gray.builder.flow.FlowService;
import gray.demo.apollo.v1.flow.ApolloGrayDeployComposer;
import gray.demo.apollo.v2.flow.ApolloGrayBatchComposer;
import gray.demo.apollo.v2.flow.ApolloGrayDeployFlowV2;
import gray.demo.expansion.v1.flow.AppExpansionFlowV1;
import gray.domain.ApolloDeployReq;
import gray.domain.AppExpansionReq;
import gray.domain.FlowInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlowDemoController {
    @Autowired
    FlowService flowService;

    @PostMapping("/flow/run")
    public String runFlow() {
        FlowInput flowInput = new FlowInput();
        String flowId = flowService.startFlow(SimpleComposer.class, flowInput);
        return flowId;
    }

    @PostMapping("/v1/deploy/apollo")
    public String apolloDeployFlow(@RequestBody ApolloDeployReq req) {
        FlowInput flowInput = new FlowInput();
        // 填充必要参数
        flowInput.setAppName(req.getAppName());
        flowInput.setOperator(req.getOperator());

        // 填充额外的参数
        flowInput.getData().put("appName", req.getAppName());
        flowInput.getData().put("env", req.getEnv());
        flowInput.getData().put("batchNum", req.getBatchNum());
        flowInput.getData().put("pauseBetweenBatch", req.isPauseBetweenBatch());
        String flowId = flowService.startFlow(ApolloGrayDeployComposer.class, flowInput);
        return flowId;
    }

    // 这是 batch 发布, 一个发布的子过程
    @PostMapping("/v2/batch/apollo")
    public String apolloBatchFlowV2(@RequestBody ApolloDeployReq req) {
        FlowInput flowInput = new FlowInput();
        flowInput.setAppName(req.getAppName());
        flowInput.setOperator(req.getOperator());
        flowInput.getData().put("appName", req.getAppName());
        flowInput.getData().put("env", req.getEnv());
        flowInput.getData().put("batchNum", req.getBatchNum());
        // currentBatchNum 是从 0 开始的, 测试接口, 这里直接写死
        flowInput.getData().put("currentBatchNum", 1);
        flowInput.getData().put("pauseBetweenBatch", req.isPauseBetweenBatch());

        String flowId = flowService.startFlow(ApolloGrayBatchComposer.class, flowInput);
        return flowId;
    }

    @PostMapping("/v2/gray/deploy/apollo")
    public String apolloGrayFlowV2(@RequestBody ApolloDeployReq req) {
        FlowInput flowInput = new FlowInput();
        flowInput.setAppName(req.getAppName());
        flowInput.setOperator(req.getOperator());

        flowInput.getData().put("appName", req.getAppName());
        flowInput.getData().put("env", req.getEnv());
        flowInput.getData().put("batchNum", req.getBatchNum());
        flowInput.getData().put("pauseBetweenBatch", req.isPauseBetweenBatch());

        String flowId = flowService.startFlow(ApolloGrayDeployFlowV2.class, flowInput);
        return flowId;
    }

    @PostMapping("/v1/app/expansion")
    public String appExpansionV1(@RequestBody AppExpansionReq req) {
        FlowInput flowInput = new FlowInput();
        flowInput.setAppName(req.getAppName());
        flowInput.setOperator(req.getOperator());

        flowInput.getData().put("appName", req.getAppName());
        flowInput.getData().put("env", req.getEnv());
        flowInput.getData().put("targetNum", req.getTargetNum());
        flowInput.getData().put("nodeSpec", req.getNodeSpec());

        String flowId = flowService.startFlow(AppExpansionFlowV1.class, flowInput);
        return flowId;
    }

}
