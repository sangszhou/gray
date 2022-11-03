package gray.builder.dao;

import gray.engine.Node;
import gray.engine.NodeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

//@Service
public class NodeDaoInMem implements NodeDao {
    Logger logger = LoggerFactory.getLogger(getClass());

    List<Node> nodeList = new LinkedList<>();

//    @PostConstruct
//    public void init() {
//        logger.info("node dao item init");
//    }

    @Override
    public void insert(Node node) {
        logger.info("save node {},  {}, {}, {}", node.getNodeType(), node.getId(), node.getPreId(), node.getWrapperId());
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
    public List<Node> queryByStatus(NodeStatus status) {
        List<Node> nodes = new LinkedList<>();
        for (Node node : nodeList) {
            if (node.getNodeStatus().equals(status)) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    @Override
    public List<Node> query(Node nodeSelector) {
        List<Node> nodes = new LinkedList<>();
        for (Node node : nodeList) {
            if (nodeSelector.getNodeType() != null && !nodeSelector.getNodeType().equals(node.getNodeType())) {
                continue;
            }
            if (nodeSelector.getNodeStatus() != null && !nodeSelector.getNodeStatus().equals(node.getNodeStatus())) {
                continue;
            }


            nodes.add(node);
        }
        return nodes;
    }
}
