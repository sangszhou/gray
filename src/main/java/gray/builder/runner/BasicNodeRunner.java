package gray.builder.runner;

import gray.builder.NodeDao;
import gray.dag.Task;
import gray.domain.StageResult;
import gray.engine.Node;
import gray.engine.NodeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicNodeRunner {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeDao nodeDao;

    public void trigger(Node basicNode) {
        Class clz = basicNode.getClass();
        Task taskInst = null;
        try {
            taskInst = (Task) clz.newInstance();
            // todo 所有 input 都要 set 进去. 这里还得区分 composerBuilder 和 taskBuilder
            StageResult stageResult = taskInst.execute();
            if (stageResult.getCode() == 2) {
                // success, 进入到 query 阶段
                basicNode.setStatus(NodeStatus.QUERY);
            } else {
                // fail
                basicNode.setStatus(NodeStatus.FAIL);
            }
            nodeDao.save(basicNode);
        } catch (InstantiationException exp) {
            logger.error("init task failed", exp);
        } catch (IllegalAccessException exp) {
            logger.error("init task failed", exp);
        }
    }

    public void query(Node basicNode) {
        Class clz = basicNode.getClass();
        Task taskInst = null;
        try {
            taskInst = (Task) clz.newInstance();
            StageResult stageResult = taskInst.query();
            if (stageResult.getCode() == 2) {
                basicNode.setStatus(NodeStatus.SUCCESS);
            } else if (stageResult.getCode() == 3){
                basicNode.setStatus(NodeStatus.FAIL);
            } else {
                // continue
                logger.info("query basic node, running");
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
