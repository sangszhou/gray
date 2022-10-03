package gray.builder.runner;

import gray.builder.NodeDao;
import gray.domain.StageResult;
import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockNodeRunner {
    @Autowired
    NodeService nodeService;

    public StageResult trigger(Node blockNode) {
        blockNode.setStatus(NodeStatus.QUERY);
        // stage result set to success
        return new StageResult();
    }

    public void query(Node wrapNode) {
        // 查找所有的孩子节点, 如果所有的孩子节点都成功, 那么外层 wrapper 节点成功
        Node queryParamNode = new Node();
        queryParamNode.setWrapperId(wrapNode.getId());

        List<Node> wrappedNode = nodeService.query(queryParamNode);
        if (wrappedNode.size() == 0) {
            wrapNode.setStatus(NodeStatus.SUCCESS);
            nodeService.save(wrapNode);
            return;
        }

        boolean hasFail = false;
        for (Node node : wrappedNode) {
            if (node.getStatus() != NodeStatus.FAIL && node.getStatus() != NodeStatus.SUCCESS) {
                return;
            }
            if (node.getStatus().equals(NodeStatus.FAIL)) {
                hasFail = true;
            }
        }

        if (hasFail) {
            wrapNode.setStatus(NodeStatus.FAIL);
        } else {
            wrapNode.setStatus(NodeStatus.SUCCESS);
        }

        nodeService.save(wrapNode);
    }

}
