package gray.builder;

import gray.builder.types.RootTaskBuilder;
import gray.domain.FlowInput;

public class SimpleComposer implements ComposerBuilder {

    public RootTaskBuilder build(FlowInput flowInput) {
        RootTaskBuilder root = new RootTaskBuilder();

        root.addTask(new AtomTaskBuilder(Class.class, "name1"));
        root.addTask(new AtomTaskBuilder(Class.class, "name2"));

        root.addTask(new ParallelTaskBuilder()
                .addTask(new AtomTaskBuilder(Class.class, "name3"))
                .addTask(new AtomTaskBuilder(Class.class, "name4")));

        root.addTask(new SeqTaskBuilder()
                .addTask(new ParallelTaskBuilder()
                        .addTask(new AtomTaskBuilder(Class.class, "name5"))
                        .addTask(new AtomTaskBuilder(Class.class, "name6")))
                .addTask(new ParallelTaskBuilder()
                        .addTask(new AtomTaskBuilder(Class.class, "name7"))
                        .addTask(new AtomTaskBuilder(Class.class, "name8"))));

        root.addTask(new AtomTaskBuilder(Class.class, "end"));

        return root;
    }

}
