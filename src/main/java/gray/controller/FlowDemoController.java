package gray.controller;

import gray.demo.SimpleComposer;
import gray.builder.flow.FlowService;
import gray.domain.FlowInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
}
