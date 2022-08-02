package gray.builder;

import gray.engine.Node;
import gray.engine.NodeStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class NodeDaoInMem implements NodeDao {

    List<Node> nodeList = new LinkedList<>();

    @Override
    public void save(Node node) {
        nodeList.add(node);
    }

    @Override
    public Node getById(String id) {
        return null;
    }

    @Override
    public List<Node> queryNodes(NodeStatus status) {
        List<Node> nodes = new LinkedList<>();
        for (Node node : nodeList) {
            if (node.getStatus().equals(status)) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    @Override
    public List<Node> queryBySelector(Node nodeSelector) {
        List<Node> nodes = new LinkedList<>();
        for (Node node : nodeList) {
            if (nodeSelector.getType() != null && !nodeSelector.getType().equals(node.getType())) {
                continue;
            }
            if (nodeSelector.getStatus() != null && !nodeSelector.getStatus().equals(node.getStatus())) {
                continue;
            }


            nodes.add(node);
        }
        return nodes;
    }
}
