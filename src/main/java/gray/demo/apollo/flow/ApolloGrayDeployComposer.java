package gray.demo.apollo.flow;

import gray.builder.AtomTaskBuilder;
import gray.builder.ComposerBuilder;
import gray.builder.annotation.Input;
import gray.builder.types.RootTaskBuilder;
import gray.demo.apollo.task.ApolloPublishFinishTask;
import gray.demo.apollo.task.ApolloPublishStartTask;
import gray.domain.FlowInput;

public class ApolloGrayDeployComposer extends ComposerBuilder {
    @Input
    int batchNum;

    public RootTaskBuilder doBuild(FlowInput flowInput) {
        RootTaskBuilder root = new RootTaskBuilder();
        root.addTask(new AtomTaskBuilder(ApolloPublishStartTask.class,
                "apolloPublishStartTask"));

        // 如何做到对于前端友好呢?
        for (int batchIdx = 0; batchIdx < batchNum; batchIdx++) {
//            root.addTask(new AtomTaskBuilder(ApolloBatchStartTask.class,
//                    "apolloBatchStartTask"));

            // 是不是 deep copy 一下更有意义呢?
            flowInput.getData().put("batchId", batchIdx);

//            root.addFlow(new ApolloBatchDeployComposer(), flowInput);
//            root.addTask(new AtomTaskBuilder(ApolloBatchStartTask.class,
//                    "apolloBatchFinishTask"));
        }

        root.addTask(new AtomTaskBuilder(ApolloPublishFinishTask.class,
                "apolloPublishFinishTask"));

        return root;
    }
}
