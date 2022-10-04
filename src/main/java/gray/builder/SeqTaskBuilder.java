package gray.builder;

import gray.engine.Node;
import gray.engine.NodeType;

import java.util.LinkedList;
import java.util.List;

public class SeqTaskBuilder extends TaskBuilder {
    Node blockNode = new Node();
    List<Node> subNodeList = new LinkedList<>();

    public SeqTaskBuilder() {
        blockNode.setType(NodeType.SEQUENCE);
    }

    @Override
    public Node build() {
        for (Node node : subNodeList) {
            blockNode.getSubNodeList().add(node);
        }
        return blockNode;
    }

    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        Node node = taskBuilder.build();
        node.setWrapperId(blockNode.getId());

        if (subNodeList.size() > 0) {
            Node preNode = subNodeList.get(subNodeList.size() - 1);
            node.setPreId(preNode.getId());
        }

        subNodeList.add(node);
        return this;
    }

}
