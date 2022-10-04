package gray.builder.types;

import com.alibaba.fastjson.JSON;
import gray.builder.FlowBuilder;
import gray.builder.TaskBuilder;
import gray.domain.Constants;
import gray.domain.FlowContext;
import gray.domain.FlowInput;
import gray.engine.*;

import java.util.LinkedList;
import java.util.List;

public class RootTaskBuilder extends TaskBuilder {
    List<Node> subNodeList = new LinkedList<>();
    Node thisNode = new Node();

    public RootTaskBuilder(FlowContext flowContext) {
        this.setFlowContext(flowContext);
    }

    @Override
    public Node build() {
        // todo. 如果是子 flow, 那么 node 节点不能是 INIT, 应该还是 invalid
        // 不对, 及时是子 flow, node 也是 init 不是 invalid
        // todo. context 是否可以在 build 里传递, 这样就不需要让用户填充 flowContext 参数了
        thisNode.setType(NodeType.ROOT);
        thisNode.setFlowId(getFlowContext().getFlowId());
        thisNode.setNodeName(Constants.INNER_NODE_NAME_ROOT);
        thisNode.setStatus(NodeStatus.INIT);

        for (Node node : subNodeList) {
            node.setWrapperId(thisNode.getId());
            thisNode.getSubNodeList().add(node);
        }

        return thisNode;
    }

    @Override
    public TaskBuilder addTask(TaskBuilder taskBuilder) {
        Node node = taskBuilder.build();
        if (subNodeList.size() > 0) {
            Node preNode = subNodeList.get(subNodeList.size() - 1);
            node.setPreId(preNode.getId());
        }
        subNodeList.add(node);
        return this;
    }

    public TaskBuilder addFlow(Class<? extends FlowBuilder> flowClz, FlowInput flowInput) {
        // todo flowInput 该怎么创建呢?
        // todo 这里不能直接 build, 需要延迟到后期吗? 还是可以直接 build 的? 是实时 build 还是延迟 build, 这是个问题
        Node node = new Node();
        node.setFlowClzName(flowClz.getName());
        node.setType(NodeType.FLOW);
        node.setNodeName(Constants.INNER_NODE_NAME_FLOW);
        node.setStatus(NodeStatus.INVALID);
        FlowContext flowContext = this.getFlowContext();
        node.setFlowId(flowContext.getFlowId());

        // 注入 flow id, 向下传递
        flowInput.getData().put(Constants.INNER_PARENT_FLOW_ID, node.getFlowId());

        ParamLinker flowInputLinker = new ParamLinker();
        flowInputLinker.setParamLinkerType(ParamLinkerType.FLOW_INPUT);
        flowInputLinker.setSourceValueData(JSON.toJSONString(flowInput));
        node.getParamLinkerList().add(flowInputLinker);

        subNodeList.add(node);
        return this;
    }

}
