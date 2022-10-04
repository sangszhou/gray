package gray.demo.apollo.v2.flow;

import gray.builder.AtomTaskBuilder;
import gray.builder.ComposerBuilder;
import gray.builder.ParallelTaskBuilder;
import gray.builder.SeqTaskBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.demo.apollo.v2.task.ApolloHostFinishTask;
import gray.demo.apollo.v2.task.ApolloHostStartTask;
import gray.domain.FlowContext;
import gray.domain.FlowInput;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;

import java.util.UUID;

public class ApolloGrayBatchComposer extends ComposerBuilder {
    @FlowParam
    int currentBatchNum;
    int hostNumInBatch = 2;

    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        FlowContext flowContext = new FlowContext();
        flowContext.setFlowId(UUID.randomUUID().toString());
        flowContext.setFlowInput(flowInput);

        RootTaskBuilder root = new RootTaskBuilder(flowContext);

        ParallelTaskBuilder hostListTaskBuilder = new ParallelTaskBuilder();
        for (int i = 0; i < hostNumInBatch; i++) {
            SeqTaskBuilder oneHostTaskBuilder = new SeqTaskBuilder();

            AtomTaskBuilder hostStartTaskBuilder = new AtomTaskBuilder(
                    flowContext,
                    ApolloHostStartTask.class,
                    String.format("HostStart-%s-%d", currentBatchNum, i)
            );
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
