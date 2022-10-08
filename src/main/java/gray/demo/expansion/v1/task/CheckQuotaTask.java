package gray.demo.expansion.v1.task;

import gray.builder.annotation.Input;
import gray.dag.Task;
import gray.domain.StageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckQuotaTask implements Task {
    Logger logger = LoggerFactory.getLogger(CheckQuotaTask.class);

    @Input
    String appName;
    @Input
    String env;
    @Input
    Integer targetNum;

    @Override
    public StageResult execute() {
        logger.info("check quota task always passes. appName: [{}], env: [{}], targetNum: [{}]",
                appName, env, targetNum);
        return StageResult.SuccessResult();
    }

    @Override
    public StageResult query() {
        return StageResult.SuccessResult();
    }
}
