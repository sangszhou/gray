package gray.service.impl;

import gray.engine.Node;
import gray.service.NodeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NodeServiceInMem implements NodeService {

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
                    if (node.getStatus() != null && !node.getStatus().equals(it.getStatus())) {
                        return false;
                    }
                    if (node.getType() != null && !node.getType().equals(it.getType())) {
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());

        return nodeList;
    }
}
