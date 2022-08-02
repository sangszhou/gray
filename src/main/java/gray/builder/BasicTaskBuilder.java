package gray.builder;

import gray.engine.Node;
import gray.engine.NodeType;

public class BasicTaskBuilder extends TaskBuilder {
    Node thisNode = new Node();

    public BasicTaskBuilder() {
        thisNode.setType(NodeType.BASIC);
    }

    public BasicTaskBuilder(Class clz, String name) {
        this.cls = clz;
        this.name = name;
    }

    @Override
    public Node build() {
        return thisNode;
    }

    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        throw new RuntimeException("basicTask addTask not supported");
    }
}
