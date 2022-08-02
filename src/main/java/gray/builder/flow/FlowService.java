package gray.builder.flow;

import gray.demo.SimpleComposer;
import gray.domain.FlowInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlowService {
    Logger logger = LoggerFactory.getLogger(getClass());

    public String startFlow(Class<SimpleComposer> composer, FlowInput flowInput) {
        String flowId = UUID.randomUUID().toString();
        flowInput.setFlowId(flowId);
        try {
            composer.newInstance().build(flowInput);
            return flowId;
        } catch (InstantiationException e) {
            logger.error("InstantiationException", e);
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException", e);
        }
        logger.error("failed to start flow");
        return null;
    }
}
