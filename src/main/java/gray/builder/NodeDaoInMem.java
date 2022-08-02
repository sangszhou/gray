package gray.builder;

import gray.engine.Node;

import java.util.LinkedList;
import java.util.List;

public class NodeDaoInMem implements NodeDao {

    List<Node> nodeList = new LinkedList<>();

    @Override
    public void save(Node node) {
        nodeList.add(node);
    }
}
