package gray.builder.runner;

import gray.builder.dao.NodeDao;
import gray.domain.StageResult;
import gray.engine.Node;
import gray.engine.NodeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParallelNodeRunner {
    @Autowired
    NodeDao nodeDao;

    public StageResult trigger(Node manyNode) {
        manyNode.setNodeStatus(NodeStatus.QUERY);
        return new StageResult();
    }

    public void query(Node wrapNode) {
        // 查找所有的孩子节点, 如果所有的孩子节点都成功, 那么外层 wrapper 节点成功
        Node queryParamNode = new Node();
        queryParamNode.setWrapperId(wrapNode.getId());

        List<Node> wrappedNode = nodeDao.query(queryParamNode);
        if (wrappedNode.size() == 0) {
            wrapNode.setNodeStatus(NodeStatus.SUCCESS);
            nodeDao.save(wrapNode);
            return;
        }

        boolean hasFail = false;
        for (Node node : wrappedNode) {
            if (node.getNodeStatus() != NodeStatus.FAIL && node.getNodeStatus() != NodeStatus.SUCCESS) {
                return;
            }
            if (node.getNodeStatus().equals(NodeStatus.FAIL)) {
                hasFail = true;
            }
        }

        if (hasFail) {
            wrapNode.setNodeStatus(NodeStatus.FAIL);
        } else {
            wrapNode.setNodeStatus(NodeStatus.SUCCESS);
        }

        nodeDao.save(wrapNode);
    }
}

