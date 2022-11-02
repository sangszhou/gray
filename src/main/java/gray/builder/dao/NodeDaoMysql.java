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

}
