package gray.service.impl;

import gray.domain.NodePo;
import gray.engine.Node;

public class NodeServiceUtils {

    public static Node convert2Node(NodePo nodePo) {
        Node node = new Node();
        return node;
    }

    public static NodePo convert2NodePo(Node node) {
        NodePo nodePo = new NodePo();
        return nodePo;
    }
}
