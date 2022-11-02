package gray.builder.dao;

import gray.engine.*;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class NodeDaoMysqlTest {
    @Test
    public void convert1Test() {
        System.out.println("hello world");
        Node node = initNode();

    }

    Node initNode() {
        Node node = new Node();
        node.setId("node-id");
        node.setFlowId("flow-id");
        node.setTaskClzName("task-clz-name");
        node.setFlowClzName("flow-clz-name");
        node.setNodeName("node-name");
        node.setPreId("pre-id");
        node.setWrapperId("wrapper-id");
        node.setWrapperId("wrapper-id");
        node.setNodeType(NodeType.ATOM);
        node.setNodeStatus(NodeStatus.SUCCESS);
        ParamLinker paramLinker = new ParamLinker();
        paramLinker.setParamLinkerType(ParamLinkerType.DYNAMIC);
        paramLinker.setSourceFieldName("source-field-name");
        paramLinker.setDestFieldName("dest-field-name");
        List<ParamLinker> paramLinkerList = new LinkedList<>();
        paramLinkerList.add(paramLinker);
        node.setParamLinkerList(paramLinkerList);
        OutputData outputData = new OutputData();
        outputData.setNodeDataType(NodeDataType.OUTPUT);
        outputData.setClassName("output-clz-name");
        outputData.setFieldName("output-field-name");
        outputData.setContent("content");
        List<OutputData> outputDataList = new LinkedList<>();
        outputDataList.add(outputData);
        node.setOutputDataList(outputDataList);
        return node;
    }

}