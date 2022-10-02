package gray.builder.flow;

import gray.builder.ComposerBuilder;
import gray.builder.NodeDao;
import gray.builder.annotation.FlowParam;
import gray.demo.SimpleComposer;
import gray.domain.FlowInput;
import gray.engine.Node;
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
    NodeDao nodeDao;

    public String startFlow(Class<SimpleComposer> composer, FlowInput flowInput) {
        String flowId = UUID.randomUUID().toString();
        flowInput.setFlowId(flowId);
        try {
            ComposerBuilder inst = composer.newInstance();
            fillFields(inst, flowInput);
            Node rootNode = inst.build(flowInput);
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

    public void fillFields(ComposerBuilder composerBuilder, FlowInput flowInput) throws IllegalAccessException {
        List<Field> fieldList = ClzUtils.getFieldsWithAnnotation(
                composerBuilder.getClass(), FlowParam.class);

        for (Field field : fieldList) {
            field.setAccessible(true);
            if (flowInput.getData().containsKey(field.getName())) {
                field.set(composerBuilder, flowInput.getData().get(field.getName()));
            } else {
                logger.error("fill fields failed, field has not data %s",
                        field.getName());
            }
        }
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
