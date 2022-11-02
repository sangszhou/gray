package gray.domain;

// 存储态, 是
public class NodePo {

    // long id 是给定的类型, 写到数据库以后才能返回
    String id;
    String flowId;
    // 在内存中就可以定义清楚
    String nodeId;
    String clzName;
    // 这个字段是干嘛的
    String nodeName;
    // 前序 node id, 假如有的话
    String preId;
    String taskClzName;
    String flowClzName;
    // 直接外层 id, 假如有的话
    String wrapperId;
    // 0 -> parent 外层, 1 -> many, 2 -> block 类型
    // 这个字段有用吗?
    String nodeType;

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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    String nodeStatus;


    String paramLinkList;
    // node data 列表
    String outputDataList;

    // 负责不同环境的测试
    String namespace;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }


    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getClzName() {
        return clzName;
    }

    public void setClzName(String clzName) {
        this.clzName = clzName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
        this.preId = preId;
    }

    public String getWrapperId() {
        return wrapperId;
    }

    public void setWrapperId(String wrapperId) {
        this.wrapperId = wrapperId;
    }



    public String getParamLinkList() {
        return paramLinkList;
    }

    public void setParamLinkList(String paramLinkList) {
        this.paramLinkList = paramLinkList;
    }

    public String getOutputDataList() {
        return outputDataList;
    }

    public void setOutputDataList(String outputDataList) {
        this.outputDataList = outputDataList;
    }
}
