package gray.builder.types;

import gray.builder.ComposerBuilder;
import gray.builder.TaskBuilder;
import gray.domain.FlowContext;
import gray.domain.FlowInput;
import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.engine.NodeType;

import java.util.LinkedList;
import java.util.List;

public class RootTaskBuilder extends TaskBuilder {
    List<Node> subNodeList = new LinkedList<>();
    Node thisNode = new Node();

//    public RootTaskBuilder() {
//    }

    public RootTaskBuilder(FlowContext flowContext) {
        this.setFlowContext(flowContext);
    }

    @Override
    public Node build() {
        thisNode.setType(NodeType.ROOT);
        thisNode.setFlowId(getFlowContext().getFlowId());
        thisNode.setNodeName("ROOT");

        for (Node node : subNodeList) {
            node.setWrapperId(thisNode.getId());
            thisNode.getSubNodeList().add(node);
        }
        thisNode.setStatus(NodeStatus.INIT);
        return thisNode;
    }

    @Override
    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        Node node = taskBuilder.build();
        if (subNodeList.size() > 0) {
            Node preNode = subNodeList.get(subNodeList.size() - 1);
            node.setPreId(preNode.getId());
        }
        subNodeList.add(node);
        return this;
    }

    public TaskBuilder addFlow(Class<? extends ComposerBuilder> flowClz, FlowInput flowInput) {
        // todo flowInput 该怎么创建呢?
        Node node = new Node();
        node.setFlowClzName(flowClz.getSimpleName());
        node.setType(NodeType.FLOW);
        node.setStatus(NodeStatus.INVALID);

        subNodeList.add(node);
        return this;
    }

}
