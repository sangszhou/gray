package gray.builder;

import com.alibaba.fastjson.JSON;
import gray.dag.Task;
import gray.engine.Node;
import gray.engine.NodeData;
import gray.engine.NodeType;
import gray.engine.ParamLinker;
import gray.util.ParamLinkUtils;

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

        // 静态的属性提前注入
        for (ParamLinker paramLinker : this.paramLinkerList) {
            if (paramLinker.getType() == 0) {
                NodeData staticField = ParamLinkUtils.buildStatic(paramLinker.getDestFieldName(),
                        paramLinker.getSourceValueData());
                thisNode.getNodeDataList().add(staticField);
            }
        }
        return thisNode;
    }

    // 虽然是静态链接, 静态链接的 param link 还需要存储吗
    public TaskBuilder linkConst(String destFieldName, Object destFieldValue) {
        ParamLinker paramLinker = new ParamLinker();
        paramLinker.setType(0);
        paramLinker.setDestFieldName(destFieldName);
        // 简单类型是不是要特殊处理下? 因为简单类型无法进行 toJSONString
        paramLinker.setSourceValueType(destFieldValue.getClass().getSimpleName());
        this.thisNode.getParamLinkerList().add(paramLinker);

        NodeData nodeData = ParamLinkUtils.buildStatic(destFieldName, destFieldValue);
        this.thisNode.getNodeDataList().add(nodeData);
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
        this.thisNode.getParamLinkerList().add(paramLinker);
        return this;
    }


    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        throw new RuntimeException("basicTask addTask not supported");
    }
}
