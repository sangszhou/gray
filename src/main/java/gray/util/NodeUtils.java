package gray.util;

import com.alibaba.fastjson.JSON;
import gray.domain.NodePo;
import gray.engine.Node;
import gray.engine.NodeStatus;
import gray.engine.NodeType;

import java.util.List;

public class NodeUtils {

    public static Node convertNode(NodePo nodePo) {
        String nodeType = nodePo.getNodeType();
        String nodeStatus = nodePo.getNodeStatus();
        String nodeDataListStr = nodePo.getOutputDataList();
        String paramLinkListStr = nodePo.getParamLinkList();

        nodePo.setNodeType(null);
        nodePo.setNodeStatus(null);
        nodePo.setOutputDataList(null);
        nodePo.setParamLinkList(null);

        String jsonStr = JSON.toJSONString(nodePo);
        Node node = JSON.parseObject(jsonStr, Node.class);
        node.setNodeStatus(NodeStatus.valueOf(nodeStatus));
        node.setNodeType(NodeType.valueOf(nodeType));
        // todo 有内部状态的, 怎么处理呢?
        node.setParamLinkerList(JSON.parseObject(paramLinkListStr,
                List.class));
        node.setOutputDataList(JSON.parseObject(nodeDataListStr,
                List.class));

        return node;
    }

    public static NodePo convertNodePo(Node node) {
        String nodeType = node.getNodeType().toString();
        String nodeStatus = node.getNodeStatus().toString();
        String outputData = JSON.toJSONString(node.getOutputDataList());
        String paramLink = JSON.toJSONString(node.getParamLinkerList());

        node.setNodeType(null);
        node.setNodeStatus(null);
        node.setParamLinkerList(null);
        node.setOutputDataList(null);

        String nodeStr = JSON.toJSONString(node);
        NodePo nodePo = JSON.parseObject(nodeStr, NodePo.class);
        nodePo.setNodeType(nodeType);
        nodePo.setNodeStatus(nodeStatus);
        nodePo.setOutputDataList(outputData);
        nodePo.setParamLinkList(paramLink);
        return nodePo;
    }

}
