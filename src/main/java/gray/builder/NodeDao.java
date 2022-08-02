package gray.builder;

import gray.engine.Node;
import gray.engine.NodeStatus;

import java.util.List;

public interface NodeDao {
    void save(Node node);

    Node getById(String id);

    List<Node> queryNodes(NodeStatus nodeStatus);

    List<Node> queryBySelector(Node node);
}
