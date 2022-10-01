package gray.demo.apollo.task;

import gray.builder.annotation.Input;
import gray.dag.Task;
import gray.domain.StageResult;

public class ApolloBatchStartTask implements Task {
    @Input
    int batchIdx;

    @Override
    public StageResult execute() {

        return null;
    }

    @Override
    public StageResult query() {
        return null;
    }
}
