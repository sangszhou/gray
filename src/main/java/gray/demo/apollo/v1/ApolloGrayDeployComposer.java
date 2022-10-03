package gray.demo.apollo.v1;

import gray.builder.AtomTaskBuilder;
import gray.builder.ComposerBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.demo.apollo.v1.task.ApolloBatchDeployTask;
import gray.domain.FlowContext;
import gray.domain.FlowInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ApolloGrayDeployComposer extends ComposerBuilder {
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
        FlowContext flowContext = new FlowContext();
        flowContext.setFlowId(UUID.randomUUID().toString());
        flowContext.setFlowInput(flowInput);

        RootTaskBuilder root = new RootTaskBuilder(flowContext);

        logger.info("apollo gray deploy composer, with  appName: {}, env: {}",
                appName, env);

        for (int i = 0; i < batchNum; i++) {
            AtomTaskBuilder batchBuilder = new AtomTaskBuilder(
                    flowContext,
                    ApolloBatchDeployTask.class,
                    String.format("apollo-batch-deploy-%d", i));
            batchBuilder.linkStatic("batchNum", i);
            // add 后的, 是顺序执行的关系
            root.addTask(batchBuilder);
        }

        return root;
    }
}
