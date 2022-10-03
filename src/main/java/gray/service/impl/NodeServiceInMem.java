package gray.service.impl;

import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.service.NodeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeServiceInMem implements NodeService {

    @Override
    public int save(Node node) {
        return 0;
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
    public List<Node> query(Node node) {
        return null;
    }
}
