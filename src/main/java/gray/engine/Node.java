package gray.engine;


import gray.dag.FlowAssembler;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

// 还是逻辑态的对象
public class Node {
    // 所属的 flow id
    String flowId;
    String id = UUID.randomUUID().toString();
    // node 既可以是一个 task 定义, 也可以是一个 flow 定义
    String taskClzName;
    String flowClzName;
    String nodeName;
    String preId;
    String wrapperId;
    // 0 -> parent 外层, 1 -> many, 2 -> block 类型
    NodeType type;
    // 初始化为 invalid
    NodeStatus status = NodeStatus.INVALID;
    List<ParamLinker> paramLinkerList;
    List<NodeData> nodeDataList;

    public List<NodeData> getNodeDataList() {
        return nodeDataList;
    }

    public void setNodeDataList(List<NodeData> nodeDataList) {
        this.nodeDataList = nodeDataList;
    }

    public List<ParamLinker> getParamLinkerList() {
        return paramLinkerList;
    }

    public void setParamLinkerList(List<ParamLinker> paramLinkerList) {
        this.paramLinkerList = paramLinkerList;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getTaskClzName() {
        return taskClzName;
    }

    public void setTaskClzName(String taskClzName) {
        this.taskClzName = taskClzName;
    }

    public String getFlowClzName() {
        return flowClzName;
    }

    public void setFlowClzName(String flowClzName) {
        this.flowClzName = flowClzName;
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
