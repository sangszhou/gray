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
     @Autowired
     NodeDao nodeDao;

     public abstract RootTaskBuilder doBuild(FlowInput flowInput);

     public void build(FlowInput flowInput) {
          RootTaskBuilder rootTaskBuilder = doBuild(flowInput);
          Node rootNode = rootTaskBuilder.build();
          logger.info("start traverse root node");
          traversal(rootNode, flowInput);
     }

     void traversal(Node node, FlowInput flowInput) {
          // 设置所属的 flow id
          node.setFlowId(flowInput.getFlowId());
          if (CollectionUtils.isEmpty(node.getSubNodeList())) {
               return;
          }
          for (Node subNode : node.getSubNodeList()) {
               traversal(subNode, flowInput);
          }
          // 最后写入自己, 防止自己直接被调用
          nodeDao.save(node);
     }

}
