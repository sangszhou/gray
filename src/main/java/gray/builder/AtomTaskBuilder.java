package gray.builder;

import com.alibaba.fastjson.JSON;
import gray.dag.Task;
import gray.engine.Node;
import gray.engine.NodeType;
import gray.engine.ParamLinker;

import java.util.LinkedList;
import java.util.List;

public class AtomTaskBuilder extends TaskBuilder {
    Node thisNode = new Node();
    List<ParamLinker> paramLinkerList = new LinkedList<>();

    public AtomTaskBuilder() {
    }

    public AtomTaskBuilder(Class<? extends Task> clz, String name) {
        this.cls = clz;
        this.name = name;
    }

    @Override
    public Node build() {
        thisNode.setType(NodeType.ATOM);
        thisNode.setParamLinkerList(this.paramLinkerList);
        return thisNode;
    }

    // 虽然是静态链接, 也仍然得动态创建
    public TaskBuilder linkConst(String destName, Object destValue) {
        ParamLinker paramLinker = new ParamLinker();
        paramLinker.setType(0);
        paramLinker.setDestFieldName(destName);
        // 简单类型是不是要特殊处理下? 因为简单类型无法进行 toJSONString
        paramLinker.setSourceValueType(destValue.getClass().getSimpleName());
        paramLinker.setSourceValueData(JSON.toJSONString(destValue));
        this.paramLinkerList.add(paramLinker);
        return this;
    }

    // connect 两个任务的开始和结束字段
    public TaskBuilder linkDynamic(String taskName, String sourceFieldName,
                                   String destFieldName) {
        ParamLinker paramLinker = new ParamLinker();
        paramLinker.setType(1);
        paramLinker.setSourceTaskName(taskName);
        paramLinker.setSourceFieldName(sourceFieldName);
        paramLinker.setDestFieldName(destFieldName);
        this.paramLinkerList.add(paramLinker);
        return this;
    }


    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        throw new RuntimeException("basicTask addTask not supported");
    }
}
