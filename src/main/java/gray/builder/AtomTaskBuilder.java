package gray.builder;

import gray.dag.Task;
import gray.domain.FlowContext;
import gray.engine.*;
import gray.util.ParamLinkUtils;

import java.util.LinkedList;
import java.util.List;

public class AtomTaskBuilder extends TaskBuilder {
    Node atomNode = new Node();
    List<ParamLinker> paramLinkerList = new LinkedList<>();

    public AtomTaskBuilder(FlowContext flowContext, Class<? extends Task> clz, String name) {
        this.setFlowContext(flowContext);
        this.cls = clz;
        this.name = name;
    }

    @Override
    public Node build() {
        // set flow id 是怎么配置的?
        atomNode.setType(NodeType.ATOM);
        atomNode.setTaskClzName(this.cls.getName());
        atomNode.setNodeName(this.name);
        atomNode.setFlowId(getFlowContext().getFlowId());

        // 静态的属性提前注入
        for (ParamLinker paramLinker : this.paramLinkerList) {
            if (paramLinker.getParamLinkerType().equals(ParamLinkerType.STATIC)) {
                NodeData staticField = ParamLinkUtils.buildStatic(paramLinker.getDestFieldName(),
                        paramLinker.getSourceValueData());
                atomNode.getNodeDataList().add(staticField);
            }
        }
        return atomNode;
    }

    // 虽然是静态链接, 静态链接的 param link 还需要存储吗
    public TaskBuilder linkStatic(String destFieldName, Object destFieldValue) {
//        ParamLinker paramLinker = new ParamLinker();
//        paramLinker.setParamLinkerType(ParamLinkerType.STATIC);
//        paramLinker.setDestFieldName(destFieldName);
//        // 简单类型是不是要特殊处理下? 因为简单类型无法进行 toJSONString
//        paramLinker.setSourceValueType(destFieldValue.getClass().getSimpleName());
//        this.thisNode.getParamLinkerList().add(paramLinker);

        NodeData nodeData = ParamLinkUtils.buildStatic(destFieldName, destFieldValue);
        this.atomNode.getNodeDataList().add(nodeData);
        return this;
    }

    // connect 两个任务的开始和结束字段
    public TaskBuilder linkDynamic(String sourceTaskName, String sourceFieldName,
                                   String destFieldName) {
        ParamLinker paramLinker = new ParamLinker();
        paramLinker.setParamLinkerType(ParamLinkerType.DYNAMIC);
        paramLinker.setSourceTaskName(sourceTaskName);
        paramLinker.setSourceFieldName(sourceFieldName);
        paramLinker.setDestFieldName(destFieldName);
        this.atomNode.getParamLinkerList().add(paramLinker);
        return this;
    }


    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        throw new RuntimeException("basicTask addTask not supported");
    }
}
