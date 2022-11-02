package gray.service.impl;

import gray.engine.Node;
import gray.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NodeServiceInMem implements NodeService {
    Logger logger = LoggerFactory.getLogger(getClass());

    Map<String, Node> nodeMap = new HashMap<>();

    @Override
    public int save(Node node) {
        if (nodeMap.containsKey(node.getId())) {
            return 0;
        }
        nodeMap.put(node.getId(), node);
        return 1;
    }

    @Override
    public Node getById(String id) {
        return nodeMap.get(id);
    }

    @Override
    public Node getByName(String flowId, String taskName) {
        Optional<Node> theNode = nodeMap
                .values()
                .stream()
                .map(it -> {
                    if (it.getFlowId() == null) {
                        logger.info("get by name, flow id is null: [{}]", it.getNodeName());
                    } else if (it.getNodeName() == null) {
                        logger.info("get by name, node name is null: [{}]", it.getNodeType());
                    }
                    return it;
                })
                .filter(it -> it.getFlowId().equals(flowId) && it.getNodeName().equals(taskName))
                .findFirst();
        if (theNode.isPresent()) {
            return theNode.get();
        }
        return null;
    }

    @Override
    public List<Node> query(Node node) {
        // 判断条件没写完, 后续可以继续补充
        List<Node> nodeList = nodeMap.values()
                .stream()
                .filter(it -> {
                    if (node.getNodeName() != null && !node.getNodeName().equals(it.getNodeName())) {
                        return false;
                    }
                    if (node.getNodeStatus() != null && !node.getNodeStatus().equals(it.getNodeStatus())) {
                        return false;
                    }
                    if (node.getNodeType() != null && !node.getNodeType().equals(it.getNodeType())) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());

        return nodeList;
    }
}
