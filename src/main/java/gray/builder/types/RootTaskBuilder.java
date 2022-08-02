package gray.builder.types;

import gray.builder.TaskBuilder;
import gray.engine.Node;
import gray.engine.NodeType;

import java.util.LinkedList;
import java.util.List;

public class RootTaskBuilder extends TaskBuilder {
    List<Node> subNodeList = new LinkedList<>();

    Node thisNode = new Node();

    public RootTaskBuilder() {
        thisNode.setType(NodeType.STARTER);
    }

    @Override
    public Node build() {
        for (Node node : subNodeList) {
//            node.setPreId(thisNode.getId());
            node.setWrapperId(thisNode.getId());
        }
        return thisNode;
    }

    @Override
    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        Node node = taskBuilder.build();
        subNodeList.add(node);
        return this;
    }

}
