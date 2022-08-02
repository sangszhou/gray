package gray.demo;


import gray.dag.Task;
import gray.domain.StageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoTask implements Task {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public StageResult execute() {
        logger.info("echo task executing %s", "demo");
        StageResult result = new StageResult();
        result.setCode(2);
        return result;
    }

    @Override
    public StageResult query() {
        logger.info("echo task query %s", "demo");
        StageResult result = new StageResult();
        result.setCode(2);
        return result;
    }
}
