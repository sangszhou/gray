package gray.demo.expansion.v1.flow;

import com.alibaba.fastjson.JSONObject;
import gray.builder.AtomTaskBuilder;
import gray.builder.FlowBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.demo.expansion.v1.task.AttachSlbTask;
import gray.demo.expansion.v1.task.CheckQuotaTask;
import gray.demo.expansion.v1.task.CreateNodeTask;
import gray.domain.FlowContext;
import gray.domain.FlowInput;

public class AppExpansionFlowV1 extends FlowBuilder {

    @FlowParam
    String appName;
    @FlowParam
    String env;
    @FlowParam
    JSONObject nodeSpec;
    @FlowParam
    Integer targetNum;

    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        FlowContext flowContext = new FlowContext();
        flowContext.setFlowInput(flowInput);

        RootTaskBuilder root = new RootTaskBuilder(flowContext);
        root.addTask(new AtomTaskBuilder(flowContext, CheckQuotaTask.class, "checkQuotaTask")
                .linkStatic("appName", appName)
                .linkStatic("env", env)
                .linkStatic("targetNum", targetNum));

        root.addTask(new AtomTaskBuilder(flowContext, CreateNodeTask.class, "createNodeTask")
                .linkStatic("appName", appName)
                .linkStatic("env", env)
                .linkStatic("nodeSpec", nodeSpec));

        // 可以简化的地方, 如果 sourceFieldName 和 destFieldName 相同, 那么 fieldName 不需要写两遍
        root.addTask(new AtomTaskBuilder(flowContext, AttachSlbTask.class, "attachSlbTask")
                .linkDynamic("createNodeTask", "nodeIdList", "nodeIdList"));

        return root;
    }
}
