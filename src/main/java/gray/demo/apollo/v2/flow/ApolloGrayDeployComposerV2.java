package gray.demo.apollo.v2.flow;

import gray.builder.AtomTaskBuilder;
import gray.builder.ComposerBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.flow.FlowService;
import gray.builder.types.RootTaskBuilder;
import gray.demo.apollo.v1.task.ApolloBatchDeployTask;
import gray.domain.FlowContext;
import gray.domain.FlowInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class ApolloGrayDeployComposerV2  extends ComposerBuilder {
    Logger logger = LoggerFactory.getLogger(getClass());

    // 简单类型也得声明
    @FlowParam
    String appName;

    @FlowParam
    String env;

    @FlowParam
    int batchNum;

    @FlowParam
    boolean pauseBetweenBatch;

    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        logger.info("apollo gray deploy composer v2, with  appName: {}, env: {}",
                appName, env);

        FlowContext flowContext = new FlowContext();
        flowContext.setFlowId(UUID.randomUUID().toString());
        flowContext.setFlowInput(flowInput);

        RootTaskBuilder root = new RootTaskBuilder(flowContext);

        for (int i = 0; i < batchNum; i++) {
            // 这里是不是要 deep copy 一下原始的 flowInput data?
            FlowInput subFlowInput = new FlowInput();
            subFlowInput.getData().put("currentBatchNum", i);
            root.addFlow(ApolloGrayBatchComposer.class, subFlowInput);
        }

        return root;
    }
}
