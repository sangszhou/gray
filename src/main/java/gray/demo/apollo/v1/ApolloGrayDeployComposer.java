package gray.demo.apollo.v1;

import gray.builder.AtomTaskBuilder;
import gray.builder.ComposerBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.demo.apollo.v1.task.ApolloBatchDeployTask;
import gray.domain.FlowInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApolloGrayDeployComposer extends ComposerBuilder {
    Logger logger = LoggerFactory.getLogger(getClass());

    // 简单类型也得声明
    @FlowParam
    String appName;

    @FlowParam
    boolean env;

    @FlowParam
    int batchNum;

    @FlowParam
    boolean pauseBetweenBatch;


    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        RootTaskBuilder root = new RootTaskBuilder();
        logger.info("apollo gray deploy composer, with  appName: {}, env: {}",
                appName, env);

        for (int i = 0; i < batchNum; i++) {
            AtomTaskBuilder batchBuilder = new AtomTaskBuilder(ApolloBatchDeployTask.class,
                    String.format("apollo-batch-deploy-%d", i));
            batchBuilder.linkStatic("batchNum", i);
            root.addTask(batchBuilder);
        }

        return root;
    }
}
