package gray.builder;

import gray.builder.types.RootTaskBuilder;
import gray.engine.Node;
import org.springframework.beans.factory.annotation.Autowired;

public class ComposerRunner {
    @Autowired
    NodeDao nodeDao;

//    void run(ComposerBuilder composerBuilder) {
//        RootTaskBuilder starterTaskBuilder = composerBuilder.build();
//        Node root = starterTaskBuilder.build();
//        traversal(root);
//    }

    void traversal(Node node) {
        persistentNode(node);
        if (node.getSubNodeList() == null) {
            return;
        }

        for (Node subNode : node.getSubNodeList()) {
            traversal(subNode);
        }
    }

    void persistentNode(Node node) {
        nodeDao.save(node);
    }

}
