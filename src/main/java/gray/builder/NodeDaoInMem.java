package gray.builder;

import gray.engine.Node;
import gray.engine.NodeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Service
public class NodeDaoInMem implements NodeDao {
    Logger logger = LoggerFactory.getLogger(getClass());

    List<Node> nodeList = new LinkedList<>();

//    @PostConstruct
//    public void init() {
//        logger.info("node dao item init");
//    }

    @Override
    public void save(Node node) {
        logger.info("save node {},  {}, {}, {}", node.getType(), node.getId(), node.getPreId(), node.getWrapperId());
        nodeList.add(node);
    }

    @Override
    public Node getById(String id) {
        return nodeList.stream()
                .filter(node -> node.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Node getByName(String flowId, String taskName) {
        // 根据 flow
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
