package gray.builder.dao;

import com.alibaba.fastjson.JSON;
import gray.domain.NodePo;
import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.engine.NodeType;
import gray.util.NodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeDaoMysql implements NodeDao{

    @Autowired
    NodePoDao nodePoDao;

    @Override
    public void insert(Node node) {
        NodePo nodePo = NodeUtils.convertNodePo(node);
        nodePoDao.insert(nodePo);
    }

    @Override
    public Node getById(String id) {
        NodePo nodePo = nodePoDao.get(id);
        Node node = NodeUtils.convertNode(nodePo);
        return node;
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
