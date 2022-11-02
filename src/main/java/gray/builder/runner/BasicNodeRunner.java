package gray.builder.runner;

import com.alibaba.fastjson.JSON;
import gray.builder.annotation.Input;
import gray.builder.annotation.Output;
import gray.dag.Task;
import gray.domain.StageResult;
import gray.engine.*;
import gray.service.NodeService;
import gray.util.ClzUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class BasicNodeRunner {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeService nodeService;

    public void trigger(Node basicNode) {
        try {
            Task taskInst = this.initTask(basicNode);
            // todo 所有 input 都要 set 进去. 这里还得区分 composerBuilder 和 taskBuilder
            StageResult stageResult = taskInst.execute();
            this.saveOutput(basicNode, taskInst);

            if (stageResult.getCode() == 2) {
                // success, 进入到 query 阶段
                basicNode.setNodeStatus(NodeStatus.QUERY);
            } else {
                // todo fail 应该怎么处理呢?
                basicNode.setNodeStatus(NodeStatus.FAIL);
            }
            nodeService.save(basicNode);
        } catch (InstantiationException exp) {
            logger.error("basic task, init task failed", exp);
        } catch (IllegalAccessException exp) {
            logger.error("basic task, init task failed", exp);
        }
    }

    public void query(Node basicNode) {
        try {
            Task taskInst = initTask(basicNode);
            StageResult stageResult = taskInst.query();
            this.saveOutput(basicNode, taskInst);
            if (stageResult.getCode() == 2) {
                basicNode.setNodeStatus(NodeStatus.SUCCESS);
                nodeService.save(basicNode);
            } else if (stageResult.getCode() == 3){
                basicNode.setNodeStatus(NodeStatus.FAIL);
                nodeService.save(basicNode);
            } else {
                // continue
                logger.info("query basic node, running");
            }
        } catch (InstantiationException exp) {
            logger.error("basic task, init task failed", exp);
        } catch (IllegalAccessException exp) {
            logger.error("basic task, init task failed", exp);
        }
    }

    // todo autowire spring 的东西也要引进来
    public Task initTask(Node basicNode) throws InstantiationException, IllegalAccessException {
        Class<? extends Task> clz = ClzUtils.castTo(basicNode.getTaskClzName());
        Task taskInst = clz.newInstance();
        initInputFields(basicNode, taskInst);
        initOutputFields(basicNode, taskInst);
        return taskInst;
    }

    // execute 和 query 之后, 把 output 结果记录到 node 节点中, 供下次 task 初始化时使用 or 别的节点引用
    // 如果 output 是 null, 也得处理覆盖已有的节点
    private void saveOutput(Node basicNode, Task taskInst) throws IllegalAccessException {
        List<Field> outputFieldList = ClzUtils.getFieldsWithAnnotation(taskInst.getClass(), Output.class);
        for (Field theField: outputFieldList) {
            theField.setAccessible(true);
            Object fieldValue = theField.get(taskInst);
            String fieldName = theField.getName();

            Optional<OutputData> existingNodeData = basicNode.getOutputDataList().stream()
                    .filter(it -> it.getNodeDataType().equals(NodeDataType.OUTPUT))
                    .filter(it -> it.getFieldName().equals(fieldName))
                    .findFirst();
            if (existingNodeData.isPresent()) {
                existingNodeData.get().setContent(JSON.toJSONString(fieldValue));
            } else {
                OutputData nodeData = new OutputData();
                nodeData.setNodeDataType(NodeDataType.OUTPUT);
                nodeData.setFieldName(fieldName);
                nodeData.setClassName(fieldValue.getClass().getName());
                nodeData.setContent(JSON.toJSONString(fieldValue));
                basicNode.getOutputDataList().add(nodeData);
            }
        }
    }

    private void initInputFields(Node basicNode, Task taskInst) throws InstantiationException, IllegalAccessException {
        List<Field> fieldList = ClzUtils.getFieldsWithAnnotation(taskInst.getClass(), Input.class);
        for (Field theField: fieldList) {
            theField.setAccessible(true);
            boolean foundMatch = false;
            for (OutputData nodeData: basicNode.getOutputDataList()) {
                // 静态传值
                if (nodeData.getFieldName().equals(theField.getName())) {
                    Object dataWithType = JSON.parseObject(nodeData.getContent(), theField.getType());
                    theField.set(taskInst, dataWithType);
                    foundMatch = true;
                    break;
                }
            }

            for (ParamLinker paramLinker: basicNode.getParamLinkerList()) {
                // 动态传值, 需要从前序节点中抽取值, 需要前序节点的状态为完成
                if (paramLinker.getDestFieldName().equals(theField.getName())) {
                    String sourceTaskName = paramLinker.getSourceTaskName();
                    Node linkedNode = nodeService.getByName(basicNode.getFlowId(), sourceTaskName);
                    if (linkedNode == null) {
                        logger.error("failed to found node by flow id: [{}], task name: [{}]",
                                basicNode.getFlowId(), sourceTaskName);
                    }
                    Optional<OutputData> theNodeData = linkedNode.getOutputDataList().stream()
                            .filter(it -> it.getNodeDataType().equals(NodeDataType.OUTPUT))
                            .filter(it -> it.getFieldName().equals(paramLinker.getSourceFieldName()))
                            .findFirst();
                    if (theNodeData.isPresent()) {
                        // todo 可能有父类子类的问题, 是否应该以 source task 的类型为准呢?
                        Object dataWithType = JSON.parseObject(theNodeData.get().getContent(), theField.getType());
                        theField.set(taskInst, dataWithType);
                        foundMatch = true;
                        break;
                    }
                }
            }

            if (!foundMatch) {
                // 如果执行到这里, 说明没有发现 match
                logger.error("failed to find data for arg [{}]", theField.getName());
            }
        }
    }

    private void initOutputFields(Node basicNode, Task taskInst) throws IllegalAccessException {
        // output 的值, 只会在 nodeDataList 中
        List<Field> fieldList = ClzUtils.getFieldsWithAnnotation(taskInst.getClass(), Output.class);
        for (Field theField: fieldList) {
            theField.setAccessible(true);

            Optional<OutputData> existingNodeData = basicNode.getOutputDataList().stream()
                    .filter(it -> it.getFieldName().equals(theField.getName()))
                    .filter(it -> it.getNodeDataType().equals(NodeDataType.OUTPUT))
                    .findFirst();

            if (existingNodeData.isPresent()) {
                Object dataWithType = JSON.parseObject(existingNodeData.get().getContent(), theField.getType());
                theField.set(taskInst, dataWithType);
            }
            // 如果没找到 output 值, 也是正常的.
        }
    }


}
