package gray.builder;

import gray.builder.types.RootTaskBuilder;
import gray.domain.FlowInput;
import gray.engine.Node;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

public abstract class ComposerBuilder {
     Logger logger = LoggerFactory.getLogger(getClass());

     public abstract RootTaskBuilder doBuild(FlowInput flowInput);

     public Node build(FlowInput flowInput) {
          RootTaskBuilder rootTaskBuilder = doBuild(flowInput);
          Node rootNode = rootTaskBuilder.build();
          return rootNode;
     }

}
