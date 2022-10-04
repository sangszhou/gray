package gray.demo.apollo.v2.flow;

import gray.builder.AtomTaskBuilder;
import gray.builder.FlowBuilder;
import gray.builder.ParallelTaskBuilder;
import gray.builder.SeqTaskBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.demo.apollo.v2.task.ApolloHostFinishTask;
import gray.demo.apollo.v2.task.ApolloHostStartTask;
import gray.domain.FlowContext;
import gray.domain.FlowInput;

public class ApolloGrayBatchComposer extends FlowBuilder {
    @FlowParam
    int currentBatchNum;
    int hostNumInBatch = 2;

    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        FlowContext flowContext = new FlowContext();
        flowContext.setFlowInput(flowInput);

        RootTaskBuilder root = new RootTaskBuilder(flowContext);

        ParallelTaskBuilder hostListTaskBuilder = new ParallelTaskBuilder(flowContext);
        for (int i = 0; i < hostNumInBatch; i++) {
            SeqTaskBuilder oneHostTaskBuilder = new SeqTaskBuilder(flowContext);

            AtomTaskBuilder hostStartTaskBuilder = new AtomTaskBuilder(
                    flowContext,
                    ApolloHostStartTask.class,
                    String.format("HostStart-%s-%d", currentBatchNum, i));

            hostStartTaskBuilder
                    .linkStatic("currentBatchNum", currentBatchNum)
                    .linkStatic("hostId", i);

            AtomTaskBuilder hostFinishTaskBuilder = new AtomTaskBuilder(
                    flowContext,
                    ApolloHostFinishTask.class,
                    String.format("HostFinish-%d-%d", currentBatchNum, i));

            hostFinishTaskBuilder
                    .linkStatic("currentBatchNum", currentBatchNum)
                    .linkStatic("hostId", i);

            oneHostTaskBuilder
                    .addTask(hostStartTaskBuilder)
                    .addTask(hostFinishTaskBuilder);

            hostListTaskBuilder.addTask(oneHostTaskBuilder);
        }

        root.addTask(hostListTaskBuilder);

        return root;
    }
}
