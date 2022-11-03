package gray.demo;

import gray.builder.AtomTaskBuilder;
import gray.builder.FlowBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.demo.task.NodeUpgradeTask;
import gray.domain.FlowContext;
import gray.domain.FlowInput;

public class AtomNodeUpgradeFlow extends FlowBuilder {

    @FlowParam
    String operatorId;
    @FlowParam
    String nodeIp;

    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        FlowContext flowContext = new FlowContext();
        flowContext.setFlowInput(flowInput);
        RootTaskBuilder root = new RootTaskBuilder(flowContext);

        root.addTask(
                new AtomTaskBuilder(flowContext, NodeUpgradeTask.class, "node_upgrade")
                        .linkStatic("operatorId", operatorId)
                        .linkStatic("nodeIp", nodeIp));
        return root;
    }
}
