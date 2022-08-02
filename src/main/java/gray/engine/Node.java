package gray.engine;


import gray.dag.FlowAssembler;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Node {
    // 所属的 flow id
    String flowId;
    String id = UUID.randomUUID().toString();
    String clzName;
    String preId;
    String wrapperId;
    // 0 -> parent 外层, 1 -> many, 2 -> block 类型
    NodeType type;
    // 初始化为 invalid
    NodeStatus status = NodeStatus.INVALID;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    List<Node> subNodeList = new LinkedList<>();

    public List<Node> getSubNodeList() {
        return subNodeList;
    }

    public void setSubNodeList(List<Node> subNodeList) {
        this.subNodeList = subNodeList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
