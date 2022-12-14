package gray.builder;

import gray.engine.Node;
import gray.engine.NodeStatus;

import java.util.List;

public interface NodeDao {
    void save(Node node);

    Node getById(String id);

    Node getByName(String flowId, String taskName);

    List<Node> queryByStatus(NodeStatus nodeStatus);

    List<Node> query(Node node);
}
