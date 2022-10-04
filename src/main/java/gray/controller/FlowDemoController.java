package gray.controller;

import gray.demo.SimpleComposer;
import gray.builder.flow.FlowService;
import gray.demo.apollo.v1.flow.ApolloGrayDeployComposer;
import gray.domain.ApolloDeployReq;
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
}
