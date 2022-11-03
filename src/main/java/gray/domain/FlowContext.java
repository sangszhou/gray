package gray.domain;

import gray.util.IdUtils;

import java.util.UUID;

public class FlowContext {
    String flowId = IdUtils.generateId("flow");
    FlowInput flowInput;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public FlowInput getFlowInput() {
        return flowInput;
    }

    public void setFlowInput(FlowInput flowInput) {
        this.flowInput = flowInput;
    }
}
