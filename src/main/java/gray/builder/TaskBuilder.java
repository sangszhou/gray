package gray.builder;

import gray.domain.FlowContext;
import gray.engine.Node;

public abstract class TaskBuilder {
    Class cls;
    String name;
    String preId;
    String wrapperId;
    FlowContext flowContext;

    public abstract Node build();
    public abstract TaskBuilder addTask(TaskBuilder taskBuilder);

    public TaskBuilder linkStatic(String destName, Object destValue) {
        throw new RuntimeException("not supported");
    }

    public TaskBuilder linkDynamic(String taskName, String sourceName,
                                   String destName) {
        throw new RuntimeException("not supported");
    }

    public FlowContext getFlowContext() {
        return flowContext;
    }

    public void setFlowContext(FlowContext flowContext) {
        this.flowContext = flowContext;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
        this.preId = preId;
    }

    public String getWrapperId() {
        return wrapperId;
    }

    public void setWrapperId(String wrapperId) {
        this.wrapperId = wrapperId;
    }

}
