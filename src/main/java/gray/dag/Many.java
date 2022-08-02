package gray.dag;

import gray.engine.Node;

public class Many extends FlowAssembler {

    void pushTask(Class clz, String name) {
        Node node = new Node();
        node.setClzName(clz.getSimpleName());
        nodes.add(node);
    }

    public void build(int depth) {
        System.out.println("");

//        for (FlowAssembler f : nodes) {
//            System.out.println(" --> node" );
//            f.build(depth+1);
//        }
    }

}
