package gray.demo.apollo.v2.flow;

import gray.builder.FlowBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.domain.FlowContext;
import gray.domain.FlowInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApolloGrayDeployFlowV2 extends FlowBuilder {
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
        flowContext.setFlowInput(flowInput);

        RootTaskBuilder root = new RootTaskBuilder(flowContext);

        for (int i = 0; i < batchNum; i ++) {
            // 这里是不是要 deep copy 一下原始的 flowInput data?
            FlowInput subFlowInput = flowInput.deepCopy();
            subFlowInput.getData().put("currentBatchNum", i);
            root.addFlow(ApolloGrayBatchComposer.class, subFlowInput);
        }

        return root;
    }
}
