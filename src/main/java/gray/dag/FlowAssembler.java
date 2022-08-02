package gray.dag;

import gray.engine.Node;
import gray.engine.NodeType;

import java.util.LinkedList;
import java.util.List;

public class FlowAssembler {
    List<Node> nodes = new LinkedList<>();

    public FlowAssembler() {
        System.out.println("init init");
    }

    Node pushTask(Class clz)  {
        Node node = doPushTask(clz);
        nodes.add(node);
        return node;
    }

    Node doPushTask(Class clz) {
        Node node = new Node();
        node.setClzName(clz.getSimpleName());
        node.setType(NodeType.ATOM);
        return node;
    }

    Node pushMany(Class ...clzs) {
        Node manyNode = new Node();
        manyNode.setType(NodeType.MANY);
        for (Class clz : clzs) {
            Node taskNode = new Node();
            taskNode.setClzName(clz.getSimpleName());
            taskNode.setPreId(manyNode.getId());
            taskNode.setWrapperId(manyNode.getId());
            manyNode.getSubNodeList().add(taskNode);
        }

        return manyNode;
    }

    Node pushBlock(Class... clzs) {
        Node blockNode = new Node();
        blockNode.setType(NodeType.BLOCK);
        for (Class clz : clzs) {
            Node taskNode = new Node();
            taskNode.setClzName(clz.getSimpleName());
            taskNode.setWrapperId(blockNode.getId());
            if (blockNode.getSubNodeList().size() == 0) {
                taskNode.setPreId(blockNode.getId());
            } else {
                String preId = blockNode.getSubNodeList().get(blockNode.getSubNodeList().size() - 1).getId();
                taskNode.setPreId(preId);
            }
            blockNode.getSubNodeList().add(taskNode);
        }

        return blockNode;
    }

    public void build(int depth) {
        // write to db
        System.out.println(depth + " --> node" + depth);
//        for (FlowAssembler f : nodes) {
//            f.build(depth + 1);
//        }
    }


//    List<Node> nodeList = new LinkedList<>();



    // 并行
    public class Many extends FlowAssembler {


    }

    // 串行
//    public class Block extends FlowAssembler {
//        public void build(int depth) {
//            System.out.println("");
//            for (FlowAssembler f : nodes) {
//                System.out.println(" --> node" );
//                f.build(depth+1);
//            }
//        }
//    }


}
