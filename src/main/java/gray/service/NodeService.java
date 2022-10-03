package gray.service;

import gray.engine.Node;
import gray.engine.NodeStatus;

import java.util.List;

public interface NodeService {
    // 返回成功的数量, 成功为 1, 失败为 0
    int save(Node node);

    // string 类型的 id 似乎也用不到吧
    Node getById(String id);

    Node getByName(String flowId, String taskName);

    List<Node> query(Node node);
}
