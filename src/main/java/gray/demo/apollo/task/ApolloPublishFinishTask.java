package gray.demo.apollo.task;

import gray.builder.annotation.Input;
import gray.dag.Task;
import gray.domain.StageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApolloPublishFinishTask implements Task {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Input
    String resource;
    @Input
    String subject;

    @Override
    public StageResult execute() {
        logger.info("apollo publish execute [end] task, with resource: [%s], subject: [%s]",
                resource, subject);
        return new StageResult();
    }

    @Override
    public StageResult query() {
        logger.info("apollo publish query [end] task, with resource: [%s], subject: [%s]",
                resource, subject);
        return new StageResult();
    }
}
