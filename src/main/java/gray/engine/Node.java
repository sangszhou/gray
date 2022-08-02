package gray.engine;


import gray.dag.FlowAssembler;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Node {
    String id = UUID.randomUUID().toString();
    String clzName;
    String preId = UUID.randomUUID().toString();
    String wrapperId;
    // 0 -> parent 外层, 1 -> many, 2 -> block 类型
    NodeType type;
    FlowAssembler ref;

    List<Node> subNodeList = new LinkedList<>();

    public List<Node> getSubNodeList() {
        return subNodeList;
    }

    public void setSubNodeList(List<Node> subNodeList) {
        this.subNodeList = subNodeList;
    }

    public FlowAssembler getRef() {
        return ref;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRef(FlowAssembler ref) {
        this.ref = ref;
    }

    public String getClzName() {
        return clzName;
    }

    public void setClzName(String clzName) {
        this.clzName = clzName;
    }

    public String getWrapperId() {
        return wrapperId;
    }

    public void setWrapperId(String wrapperId) {
        this.wrapperId = wrapperId;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
        this.preId = preId;
    }
}
