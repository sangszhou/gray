package gray.controller;

import gray.builder.dao.NodeDao;
import gray.engine.Node;
import gray.engine.NodeType;
import gray.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 测试 db 读写
@RestController
public class DbController {
    @Autowired
    NodeDao nodeDao;

    @GetMapping("/node/insert")
    public int insert() {
        Node node = new Node();
        node.setFlowId("flow-id");
        node.setNodeName("node-name");
        node.setTaskClzName("task-clz-name");
        node.setNodeType(NodeType.ATOM);
        nodeDao.insert(node);
        return 1;
    }
}
