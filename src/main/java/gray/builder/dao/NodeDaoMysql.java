package gray.builder.dao;

import com.alibaba.fastjson.JSON;
import gray.domain.NodePo;
import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.engine.NodeType;

import java.util.List;

public class NodeDaoMysql implements NodeDao{
    @Override
    public void save(Node node) {

    }

    @Override
    public Node getById(String id) {
        return null;
    }

    @Override
    public Node getByName(String flowId, String taskName) {
        return null;
    }

    @Override
    public List<Node> queryByStatus(NodeStatus nodeStatus) {
        return null;
    }

    @Override
    public List<Node> query(Node node) {
        return null;
    }

    private Node convert1(NodePo nodePo) {
        String nodeType = nodePo.getNodeType();
        String nodeStatus = nodePo.getNodeStatus();
        String nodeDataListStr = nodePo.getNodeDataList();
        String paramLinkListStr = nodePo.getParamLinkList();

        String jsonStr = JSON.toJSONString(nodePo);
        Node node = JSON.parseObject(jsonStr, Node.class);
        node.setNodeStatus(NodeStatus.valueOf(nodeStatus));
        node.setNodeType(NodeType.valueOf(nodeType));
        // todo 有内部状态的, 怎么处理呢?
        node.setParamLinkerList(JSON.parseObject(paramLinkListStr,
                List.class));
        node.setOutputDataList(JSON.parseObject(nodeDataListStr,
                List.class));

        return node;
    }

//    private NodePo convert2(Node node) {
//
//    }

}
