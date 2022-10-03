package gray.engine;

// 存储序列化好的值
public class NodeData {
    String className;
    String content;
    // task 字段名
    String fieldName;
    // input = 0, output = 1, input 会提前填充完毕
    ParamLinkerType paramLinkerType;

    public ParamLinkerType getParamLinkerType() {
        return paramLinkerType;
    }

    public void setParamLinkerType(ParamLinkerType paramLinkerType) {
        this.paramLinkerType = paramLinkerType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
