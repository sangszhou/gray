package gray.domain;

import com.alibaba.fastjson.JSONObject;

public class AppExpansionReq {
    String appName;
    String operator;
    String env;
    int targetNum;
    JSONObject nodeSpec;

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

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public int getTargetNum() {
        return targetNum;
    }

    public void setTargetNum(int targetNum) {
        this.targetNum = targetNum;
    }

    public JSONObject getNodeSpec() {
        return nodeSpec;
    }

    public void setNodeSpec(JSONObject nodeSpec) {
        this.nodeSpec = nodeSpec;
    }
}
