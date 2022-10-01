package gray.demo.apollo.flow;

import gray.builder.AtomTaskBuilder;
import gray.builder.ComposerBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.demo.apollo.task.ApolloBatchFinishTask;
import gray.demo.apollo.task.ApolloBatchStartTask;
import gray.domain.FlowInput;

public class ApolloBatchDeployComposer extends ComposerBuilder {
    @FlowParam
    int batchId;

    // host num 是预先计算好的还是实时计算呢?
    int hostNumInBatch;

    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        RootTaskBuilder root = new RootTaskBuilder();
        // step1: 开始 host 发布
        // step2:
        root.addTask(new AtomTaskBuilder(ApolloBatchStartTask.class,
                "apolloBatchStartTask-" + batchId));


        root.addTask(new AtomTaskBuilder(ApolloBatchFinishTask.class,
                "apolloBatchFinishTask-" + batchId));
        return root;
    }
}
