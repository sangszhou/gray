package gray.demo.expansion.v1.flow;

import gray.builder.FlowBuilder;
import gray.builder.types.RootTaskBuilder;
import gray.domain.FlowInput;

// 问题来了, expansion flow 是怎么把 task 弄出来呢?
// todo 这里先不考虑了
public class NodeExpansionFlow extends FlowBuilder {

    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        return null;
    }
}
