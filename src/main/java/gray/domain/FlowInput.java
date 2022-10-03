package gray.domain;

import java.util.HashMap;
import java.util.Map;

public class FlowInput {
    String appName;
    String operator;
//    String flowId;
    Map<String, Object> data = new HashMap<>();

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

//    public String getFlowId() {
//        return flowId;
//    }
//
//    public void setFlowId(String flowId) {
//        this.flowId = flowId;
//    }
}
