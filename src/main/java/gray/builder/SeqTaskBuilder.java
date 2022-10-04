package gray.builder;

import gray.domain.Constants;
import gray.domain.FlowContext;
import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.engine.NodeType;

import java.util.LinkedList;
import java.util.List;

public class SeqTaskBuilder extends TaskBuilder {
    Node sequenceNode = new Node();
    List<Node> subNodeList = new LinkedList<>();

    public SeqTaskBuilder(FlowContext flowContext) {
        sequenceNode.setType(NodeType.SEQUENCE);
    }

    @Override
    public Node build() {
        sequenceNode.setFlowId(this.flowContext.getFlowId());
        sequenceNode.setStatus(NodeStatus.INVALID);
        sequenceNode.setNodeName(Constants.INNER_NODE_NAME_SEQUENCE);
        for (Node node : subNodeList) {
            sequenceNode.getSubNodeList().add(node);
        }
        return sequenceNode;
    }

    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        Node node = taskBuilder.build();
        node.setWrapperId(sequenceNode.getId());

        if (subNodeList.size() > 0) {
            Node preNode = subNodeList.get(subNodeList.size() - 1);
            node.setPreId(preNode.getId());
        }

        subNodeList.add(node);
        return this;
    }

}
