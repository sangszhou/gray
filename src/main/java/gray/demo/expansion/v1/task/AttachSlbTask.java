package gray.demo.expansion.v1.task;

import gray.builder.annotation.Input;
import gray.dag.Task;
import gray.domain.StageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AttachSlbTask implements Task {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Input
    List<String> nodeIdList;

    @Override
    public StageResult execute() {
        if (nodeIdList.size() == 0) {
            logger.info("attach slb task, node id size is empty");
            return StageResult.SuccessResult();
        }
        logger.info("node id size: [{}]", nodeIdList.size());
        return StageResult.SuccessResult();
    }

    @Override
    public StageResult query() {
        return StageResult.SuccessResult();
    }
}
