package gray.domain;

import gray.engine.NodeStatus;
import gray.engine.NodeType;

// 存储态, 是
public class NodePo {
    // long id 是给定的类型, 写到数据库以后才能返回
    long id;
    String flowId;
    // 在内存中就可以定义清楚
    String nodeId;
    String clzName;
    // 这个字段是干嘛的
    String nodeName;
    // 前序 node id, 假如有的话
    String preId;
    // 直接外层 id, 假如有的话
    String wrapperId;
    // 0 -> parent 外层, 1 -> many, 2 -> block 类型
    // 这个字段有用吗?
    String nodeType;

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    String nodeStatus;


    String paramLinkList;
    // node data 列表
    String nodeDataList;

    // 负责不同环境的测试
    String namespace;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getNodeDataList() {
        return nodeDataList;
    }

    public void setNodeDataList(String nodeDataList) {
        this.nodeDataList = nodeDataList;
    }
}
