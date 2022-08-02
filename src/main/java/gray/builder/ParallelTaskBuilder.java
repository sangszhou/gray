package gray.builder;

import gray.engine.Node;
import gray.engine.NodeType;

import java.util.LinkedList;
import java.util.List;

public class ParallelTaskBuilder extends TaskBuilder {
    Node manyNode = new Node();
    List<Node> subNodeList = new LinkedList<>();

    public ParallelTaskBuilder() {
        manyNode.setType(NodeType.MANY);
    }

    @Override
    public Node build() {
        for (Node node : subNodeList) {
            manyNode.getSubNodeList().add(node);
        }
        return manyNode;
    }

    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        Node node = taskBuilder.build();
        node.setWrapperId(manyNode.getId());
        subNodeList.add(node);
        return this;
    }

}
