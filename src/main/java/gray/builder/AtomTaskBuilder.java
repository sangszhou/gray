package gray.builder;

import gray.engine.Node;
import gray.engine.NodeType;

public class AtomTaskBuilder extends TaskBuilder {
    Node thisNode = new Node();

    public AtomTaskBuilder() {
        thisNode.setType(NodeType.BASIC);
    }

    public AtomTaskBuilder(Class clz, String name) {
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
