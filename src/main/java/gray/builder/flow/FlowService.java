package gray.builder.flow;

import gray.builder.ComposerBuilder;
import gray.builder.annotation.FlowParam;
import gray.domain.FlowInput;
import gray.engine.Node;
import gray.service.NodeService;
import gray.util.ClzUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@Service
public class FlowService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeService nodeService;

    public String startFlow(Class<? extends ComposerBuilder> composer, FlowInput flowInput) {
        String flowId = UUID.randomUUID().toString();
        flowInput.setFlowId(flowId);
        try {
            // 怎么启动的, 需要先把状态置位 init 吗
            ComposerBuilder inst = initComposerBuilder(composer, flowInput);
            Node rootNode = inst.build(flowInput);
            persistNode(rootNode, flowInput);
            return flowId;
        } catch (InstantiationException e) {
            logger.error("InstantiationException", e);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException", e);
        }

        logger.error("failed to start flow");
        return null;
    }

    private ComposerBuilder initComposerBuilder(Class<? extends ComposerBuilder> composer, FlowInput flowInput) throws IllegalAccessException, InstantiationException {
        ComposerBuilder inst = composer.newInstance();
        fillFields(inst, flowInput);
        return inst;
    }

    private void fillFields(ComposerBuilder composerBuilder, FlowInput flowInput) throws IllegalAccessException {
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
        // 设置所属的 flow id
        node.setFlowId(flowInput.getFlowId());
        if (CollectionUtils.isEmpty(node.getSubNodeList())) {
            nodeService.save(node);
            return;
        }

        for (Node subNode : node.getSubNodeList()) {
            persistNode(subNode, flowInput);
        }

        // 最后写入自己, 防止自己直接被调用
        nodeService.save(node);
    }

}
