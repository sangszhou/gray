package gray.engine;

public class ParamLinker {
    String sourceFieldName;
    String sourceValueType;
    String sourceValueData;
    String destFieldName;
    String sourceTaskName;
    // type 可以是 const, or taskLink or
    int type;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
