package gray.builder;

import gray.builder.types.RootTaskBuilder;
import gray.domain.FlowInput;
import gray.engine.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FlowBuilder {
     Logger logger = LoggerFactory.getLogger(getClass());

     public abstract RootTaskBuilder doBuild(FlowInput flowInput);

     public Node build(FlowInput flowInput) {
          RootTaskBuilder rootTaskBuilder = doBuild(flowInput);
          // build 是否可以填充一个 rootBuild 参数呢?
          Node rootNode = rootTaskBuilder.build();
          return rootNode;
     }

}
