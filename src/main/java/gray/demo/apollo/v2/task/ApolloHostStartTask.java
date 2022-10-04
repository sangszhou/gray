package gray.demo.apollo.v2.task;

import gray.builder.annotation.Input;
import gray.dag.Task;
import gray.domain.StageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApolloHostStartTask implements Task {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Input
    int currentBatchNum;
    @Input
    int hostId;

    @Override
    public StageResult execute() {
        logger.info("host [start, execute] task, batchId:hostId - [{}:{}]", currentBatchNum, hostId);
        return StageResult.SuccessResult();
    }

    @Override
    public StageResult query() {
        logger.info("host [start, query] task, batchId:hostId - [{}:{}]", currentBatchNum, hostId);
        return StageResult.SuccessResult();
    }
}
