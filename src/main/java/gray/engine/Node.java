package gray.engine;


import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

// 还是逻辑态的对象
public class Node {
    String id = UUID.randomUUID().toString();
    // 所属的 flow id
    String flowId;
    // 如果是子流程, 会有 parent flow id
    String parentFlowId;

    // node 既可以是一个 task 定义, 也可以是一个 flow 定义
    String taskClzName;
    String flowClzName;
    String nodeName;
    String preId;
    String wrapperId;
    // 0 -> atomic?, 1 -> many, 2 -> block 类型, 3 -> flow?
    NodeType nodeType;
    // 初始化为 invalid
    NodeStatus nodeStatus = NodeStatus.INVALID;
    List<ParamLinker> paramLinkerList = new LinkedList<>();

    List<OutputData> outputDataList = new LinkedList<>();

    public String getParentFlowId() {
        return parentFlowId;
    }

    public void setParentFlowId(String parentFlowId) {
        this.parentFlowId = parentFlowId;
    }

    public List<OutputData> getOutputDataList() {
        return outputDataList;
    }

    public void setOutputDataList(List<OutputData> outputDataList) {
        this.outputDataList = outputDataList;
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

    public NodeStatus getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(NodeStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
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

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
        this.preId = preId;
    }
}
