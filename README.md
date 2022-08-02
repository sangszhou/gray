[1] pre 和 wrapper 不能同时配置

[2] 如果前置节点失败了, 后续节点要设置怎样的状态, 状态是怎么传递的?

[3] 分批算法的逻辑
```java
class BatchDeploy {
    void build() {
        pushTask(flowStartTask.class);
        for (int i = 0; i < batchNum; i++) {
            Block{{
                pushTask(batchStartTask.class);
                for(int j = 0; j < hostBatch(i); j ++) {
                    Many {{
                        pushTask(hostStartTask.class);
                        for(int k = 0; k < stepNum; k ++) {
                            pushTask(stepTask.class)
                                    .linkConst("idx", k);
                        }
                        pushTask(hostEndTask.class);    
                    }}
                }
                pushTask(batchEndTask.class);
            }}
        }
    }
}
```

上面的实现

```java
import gray.builder.AtomTaskBuilder;
import gray.builder.BasicTaskBuilder;
import gray.builder.SeqTaskBuilder;
import gray.builder.ParallelTaskBuilder;
import gray.builder.types.RootTaskBuilder;
import gray.builder.types.StarterTaskBuilder;

class BatchDeploy2 {
    void build() {
        RootTaskBuilder starterTaskBuilder = new RootTaskBuilder();
        starterTaskBuilder.addTask(new AtomTaskBuilder(FlowStartTask.class, "flowStart"));
        for (int i = 0; i < batchNum; i++) {
            SeqTaskBuilder batchBuilder = new SeqTaskBuilder();
            batchBuilder.addTask(new AtomTaskBuilder(BatchStart.class, "batchStart"));

            ParallelTaskBuilder manyHostBuilder = new ParallelTaskBuilder();
            for (int j = 0; j < hostBatch(i); j++) {
                SeqTaskBuilder hostBuilder = new SeqTaskBuilder();
                hostBuilder.addTask(new AtomTaskBuilder(HostStartTask.class, "hostStart"));
                for (int k = 0; k < stepNum; k++) {
                    AtomTaskBuilder stepTask = new AtomTaskBuilder(StepTask.class, "step");
                    stepTask.linkConst("idx", k);
                    hostBuilder.addTask(stepTask);
                }
                hostBuilder.addTask(new AtomTaskBuilder(HostEndTask.class, "hostEnd"));
                manyHostBuilder.addTask(hostBuilder);
            }

            batchBuilder.addTask(manyHostBuilder);
            batchBuilder.addTask(BatchEndTask.class, "batchEnd");
            starterTaskBuilder.addTask(batchBuilder);
        }
        starterTaskBuilder.addTask(new AtomTaskBuilder(FlowEndTask.class, "flowStart"));
    }
}
```

