package gray.builder.flow;

import gray.builder.FlowBuilder;
import gray.builder.annotation.FlowParam;
import gray.domain.Constants;
import gray.domain.FlowInput;
import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.engine.NodeType;
import gray.service.NodeService;
import gray.util.ClzUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class FlowService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeService nodeService;

    public String startFlow(Class<? extends FlowBuilder> composer, FlowInput flowInput) {
        try {
            // 怎么启动的, 需要先把状态置位 init 吗
            FlowBuilder inst = initComposerBuilder(composer, flowInput);
            Node rootNode = inst.build(flowInput);
            persistNode(rootNode, flowInput);
            return rootNode.getFlowId();
        } catch (InstantiationException e) {
            logger.error("InstantiationException", e);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException", e);
        }

        logger.error("failed to start flow");
        return null;
    }

    private FlowBuilder initComposerBuilder(Class<? extends FlowBuilder> composer, FlowInput flowInput) throws IllegalAccessException, InstantiationException {
        FlowBuilder inst = composer.newInstance();
        fillFields(inst, flowInput);
        return inst;
    }

    public NodeStatus flowStatus(String flowId) {
        Node queryParam = new Node();
        queryParam.setType(NodeType.ROOT);
        queryParam.setFlowId(flowId);
        Node rootNode = nodeService.getByName(flowId, "ROOT");
        if (rootNode == null) {
            logger.warn("flow status, flow id: {} is null", flowId);
            return null;
        }

        return rootNode.getStatus();
    }

    public List<Node> queryByParentFlowId(String parentFlowId) {
        Node queryParam = new Node();
        queryParam.setType(NodeType.ROOT);
        queryParam.setParentFlowId(parentFlowId);

        List<Node> subFlowList = nodeService.query(queryParam);
        return subFlowList;
    }

    private void fillFields(FlowBuilder composerBuilder, FlowInput flowInput) throws IllegalAccessException {
        List<Field> fieldList = ClzUtils.getFieldsWithAnnotation(
                composerBuilder.getClass(), FlowParam.class);
        logger.info("flow fill fields: [{}]", fieldList.stream()
                .map(it -> it.getName())
                .reduce((a, b) -> a + "," + b).orElse(null));

        for (Field field : fieldList) {
            field.setAccessible(true);
            if (flowInput.getData().containsKey(field.getName())) {
                field.set(composerBuilder, flowInput.getData().get(field.getName()));
            } else {
                logger.error("fill fields failed, field has not data: {}", field.getName());
            }
        }
    }

    public void persistNode(Node node, FlowInput flowInput) {
        // 设置父子关联
        if (flowInput.getData().containsKey(Constants.INNER_PARENT_FLOW_ID)) {
            node.setParentFlowId(flowInput.getData()
                    .get(Constants.INNER_PARENT_FLOW_ID).toString());
        }

        // fast path, 但似乎没必要, 反而可能会引入 bug
//        if (CollectionUtils.isEmpty(node.getSubNodeList())) {
//            nodeService.save(node);
//            return;
//        }

        for (Node subNode : node.getSubNodeList()) {
            persistNode(subNode, flowInput);
        }

        // 最后写入自己, 防止自己直接被调用
        nodeService.save(node);
    }

}
