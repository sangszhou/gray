package gray.builder;

import gray.domain.Constants;
import gray.domain.FlowContext;
import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.engine.NodeType;

import java.util.LinkedList;
import java.util.List;

public class ParallelTaskBuilder extends TaskBuilder {
    Node parallelNode = new Node();
    List<Node> subNodeList = new LinkedList<>();
    FlowContext flowContext;

    public ParallelTaskBuilder(FlowContext flowContext) {
        parallelNode.setType(NodeType.PARALLEL);
        this.flowContext = flowContext;
    }

    @Override
    public Node build() {
        parallelNode.setFlowId(this.flowContext.getFlowId());
        parallelNode.setStatus(NodeStatus.INVALID);
        parallelNode.setNodeName(Constants.INNER_NODE_NAME_PARALLEL);
        for (Node node : subNodeList) {
            parallelNode.getSubNodeList().add(node);
        }
        return parallelNode;
    }

    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        Node node = taskBuilder.build();
        node.setWrapperId(parallelNode.getId());
        subNodeList.add(node);
        return this;
    }

}
