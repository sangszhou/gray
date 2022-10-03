package gray.util;

import com.alibaba.fastjson.JSON;
import gray.engine.NodeData;

public class ParamLinkUtils {
    public static NodeData buildStatic(String destFieldName, Object data) {
        NodeData nodeData = new NodeData();
        nodeData.setType(0);
        nodeData.setFieldName(destFieldName);
        nodeData.setClassName(data.toString());
        if (isSimpleType(data)) {
            nodeData.setContent(data.toString());
        } else {
            nodeData.setContent(JSON.toJSONString(data));
        }
        return nodeData;
    }

    // todo 记得处理 string 类型
    public static boolean isSimpleType(Object data) {
        if (data instanceof String) {
            return true;
        } else if (data instanceof Character) {
            return true;
        } else if (data instanceof Boolean) {
            return true;
        } else if (data instanceof Number) {
            return true;
        }

        return false;
    }
}
