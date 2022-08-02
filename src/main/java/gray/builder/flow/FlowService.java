package gray.builder.flow;

import gray.builder.NodeDao;
import gray.demo.SimpleComposer;
import gray.domain.FlowInput;
import gray.engine.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.UUID;

@Service
public class FlowService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeDao nodeDao;

    public String startFlow(Class<SimpleComposer> composer, FlowInput flowInput) {
        String flowId = UUID.randomUUID().toString();
        flowInput.setFlowId(flowId);
        try {
            Node rootNode = composer.newInstance().build(flowInput);
            traversal(rootNode, flowInput);
            return flowId;
        } catch (InstantiationException e) {
            logger.error("InstantiationException", e);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException", e);
        }
        logger.error("failed to start flow");
        return null;
    }

    public void traversal(Node node, FlowInput flowInput) {
        // 设置所属的 flow id
        node.setFlowId(flowInput.getFlowId());
        if (CollectionUtils.isEmpty(node.getSubNodeList())) {
            nodeDao.save(node);
            return;
        }

        for (Node subNode : node.getSubNodeList()) {
            traversal(subNode, flowInput);
        }
        // 最后写入自己, 防止自己直接被调用
        nodeDao.save(node);
    }

}
