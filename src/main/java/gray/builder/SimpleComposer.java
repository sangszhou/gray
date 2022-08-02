package gray.builder;

import gray.builder.types.StarterTaskBuilder;

public class SimpleComposer implements ComposerBuilder {

    public StarterTaskBuilder build() {
        StarterTaskBuilder root = new StarterTaskBuilder();

        root.addTask(new BasicTaskBuilder(Class.class, "name1"));
        root.addTask(new BasicTaskBuilder(Class.class, "name2"));

        root.addTask(new ManyTaskBuilder()
                .addTask(new BasicTaskBuilder(Class.class, "name3"))
                .addTask(new BasicTaskBuilder(Class.class, "name4")));

        root.addTask(new BlockTaskBuilder()
                .addTask(new ManyTaskBuilder()
                        .addTask(new BasicTaskBuilder(Class.class, "name5"))
                        .addTask(new BasicTaskBuilder(Class.class, "name6")))
                .addTask(new ManyTaskBuilder()
                        .addTask(new BasicTaskBuilder(Class.class, "name7"))
                        .addTask(new BasicTaskBuilder(Class.class, "name8"))));

        root.addTask(new BasicTaskBuilder(Class.class, "end"));

        return root;
    }

}
