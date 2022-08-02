package gray.builder;

import gray.dag.Task;
import gray.engine.Node;
import gray.engine.NodeType;

public class AtomTaskBuilder extends TaskBuilder {
    Node thisNode = new Node();

    public AtomTaskBuilder() {
    }

    public AtomTaskBuilder(Class<? extends Task> clz, String name) {
        this.cls = clz;
        this.name = name;
    }

    @Override
    public Node build() {
        thisNode.setType(NodeType.ATOM);
        return thisNode;
    }

    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        throw new RuntimeException("basicTask addTask not supported");
    }
}
