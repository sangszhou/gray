package gray.demo.expansion.v1.task;

import com.alibaba.fastjson.JSONObject;
import gray.builder.annotation.Input;
import gray.builder.annotation.Output;
import gray.dag.Task;
import gray.domain.StageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CreateNodeTask implements Task {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Input
    String appName;
    @Input
    String env;
    // 可能是 ecs or cvm spec
    // 具体的格式, 下沉到方法中解析
    @Input
    JSONObject nodeSpec;
    @Output
    List<String> nodeIdList;

    @Override
    public StageResult execute() {
        logger.info("create node task with appName: [{}], env: [{}], node spec: [{}]",
                appName, env, nodeSpec.toString());

        return StageResult.SuccessResult();
    }

    @Override
    public StageResult query() {
        logger.info("create node task querying");
        List<String> nodeIdList = new LinkedList<>();
        nodeIdList.add("cvm-1");
        nodeIdList.add("cvm-2");
        return StageResult.SuccessResult();
    }
}
