package gray.engine;

public class ParamLinker {
    String sourceFieldName;
    String sourceValueType;
    String sourceValueData;
    String destFieldName;
    String sourceTaskName;
    // type 可以是 const, or taskLink or flow input
    // 0 ->
    ParamLinkerType paramLinkerType;

    public String getSourceValueType() {
        return sourceValueType;
    }

    public void setSourceValueType(String sourceValueType) {
        this.sourceValueType = sourceValueType;
    }

    public String getSourceValueData() {
        return sourceValueData;
    }

    public void setSourceValueData(String sourceValueData) {
        this.sourceValueData = sourceValueData;
    }

    public ParamLinkerType getParamLinkerType() {
        return paramLinkerType;
    }

    public void setParamLinkerType(ParamLinkerType paramLinkerType) {
        this.paramLinkerType = paramLinkerType;
    }

    public String getSourceFieldName() {
        return sourceFieldName;
    }

    public void setSourceFieldName(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    public String getDestFieldName() {
        return destFieldName;
    }

    public void setDestFieldName(String destFieldName) {
        this.destFieldName = destFieldName;
    }

    public String getSourceTaskName() {
        return sourceTaskName;
    }

    public void setSourceTaskName(String sourceTaskName) {
        this.sourceTaskName = sourceTaskName;
    }
}
