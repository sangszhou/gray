package gray.domain;

import com.alibaba.fastjson.JSON;

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

    public FlowInput deepCopy() {
        String inStr = JSON.toJSONString(this);
        return JSON.parseObject(inStr, FlowInput.class);
    }

}
