package gray.demo.task;

import gray.builder.annotation.Input;
import gray.builder.annotation.Output;
import gray.dag.Task;
import gray.domain.StageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeUpgradeTask implements Task {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Input
    String operatorId;
    @Input
    String nodeIp;
    @Output
    String msg;

    @Override
    public StageResult execute() {
        logger.info("node upgrade with operator {}, node ip: {}", operatorId, nodeIp);
        msg = String.format("operator: [%s], upgrade ip: [%s]", operatorId, nodeIp);
        return StageResult.SuccessResult();
    }

    @Override
    public StageResult query() {
        logger.info("query msg: {}", msg);
        return StageResult.SuccessResult();
    }
}
