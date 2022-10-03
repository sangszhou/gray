package gray.demo;

import gray.builder.AtomTaskBuilder;
import gray.builder.ComposerBuilder;
import gray.builder.ParallelTaskBuilder;
import gray.builder.SeqTaskBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.domain.FlowInput;


public class SimpleComposer extends ComposerBuilder {

    // flow param 直接从 flowInput 中抽取, 用起来比较方便
    @FlowParam
    String composerName;
    @FlowParam
    String operatorId;

    public RootTaskBuilder doBuild(FlowInput flowInput) {
        RootTaskBuilder root = new RootTaskBuilder();

        root.addTask(new AtomTaskBuilder(EchoTask.class, "1_1"));
        root.addTask(new AtomTaskBuilder(EchoTask.class, "1_2"));

        root.addTask(new ParallelTaskBuilder()
                .addTask(new AtomTaskBuilder(EchoTask.class, "2_1"))
                .addTask(new AtomTaskBuilder(EchoTask.class, "2_2")));

        root.addTask(new SeqTaskBuilder()
                .addTask(new ParallelTaskBuilder()
                        .addTask(new AtomTaskBuilder(EchoTask.class, "3_1"))
                        .addTask(new AtomTaskBuilder(EchoTask.class, "3_2")))
                .addTask(new ParallelTaskBuilder()
                        .addTask(new AtomTaskBuilder(EchoTask.class, "3_3"))
                        .addTask(new AtomTaskBuilder(EchoTask.class, "3_4"))));

        root.addTask(new AtomTaskBuilder(EchoTask.class, "1_3"));

        return root;
    }

}
