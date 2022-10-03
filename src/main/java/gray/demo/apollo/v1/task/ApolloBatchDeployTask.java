package gray.demo.apollo.v1.task;

import gray.builder.annotation.Input;
import gray.dag.Task;
import gray.domain.StageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ApolloBatchDeployTask  implements Task {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Input
    int batchNum;

    @Override
    public StageResult execute() {
        logger.info("apollo batch {} executed", batchNum);

        return StageResult.SuccessResult();
    }

    @Override
    public StageResult query() {
        logger.info("apollo queried, with batch num {}", batchNum);
        return StageResult.SuccessResult();
    }
}
