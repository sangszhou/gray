package gray.builder.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gray.builder.ComposerBuilder;
import gray.builder.NodeDao;
import gray.builder.annotation.Input;
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
            if (stageResult.getCode() == 2) {
                // success, 进入到 query 阶段
                basicNode.setStatus(NodeStatus.QUERY);
            } else {
                // fail
                basicNode.setStatus(NodeStatus.FAIL);
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
            if (stageResult.getCode() == 2) {
                basicNode.setStatus(NodeStatus.SUCCESS);
                nodeService.save(basicNode);
            } else if (stageResult.getCode() == 3){
                basicNode.setStatus(NodeStatus.FAIL);
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

    public Task initTask(Node basicNode) throws InstantiationException, IllegalAccessException {
        Class<? extends Task> clz = ClzUtils.castTo(basicNode.getTaskClzName());
        Task taskInst = clz.newInstance();
        List<Field> fieldList = ClzUtils.getFieldsWithAnnotation(clz, Input.class);
        for (int i = 0; i < fieldList.size(); i++) {
            Field theField = fieldList.get(i);
            theField.setAccessible(true);
            for (int j = 0; j < basicNode.getNodeDataList().size(); j++) {
                // 静态传值
                NodeData nodeData = basicNode.getNodeDataList().get(j);
                if (nodeData.getFieldName().equals(theField.getName())) {
                    Object dataWithType = JSON.parseObject(nodeData.getContent(), theField.getType());
                    theField.set(taskInst, dataWithType);
                    break;
                }
            }
            for (int j = 0; j < basicNode.getParamLinkerList().size(); j++) {
                // 动态传值, 需要从前序节点中抽取值, 需要前序节点的状态为完成
                ParamLinker paramLinker = basicNode.getParamLinkerList().get(j);
                if (paramLinker.getDestFieldName().equals(theField.getName())) {
                    String sourceTaskName = paramLinker.getSourceTaskName();
                    Node linkedNode = nodeService.getByName(basicNode.getFlowId(), sourceTaskName);
                    Optional<NodeData> theNodeData = linkedNode.getNodeDataList().stream()
                            .filter(it -> it.getNodeDataType().equals(NodeDataType.OUTPUT))
                            .filter(it -> it.getFieldName().equals(paramLinker.getSourceFieldName()))
                            .findFirst();
                    if (theNodeData.isPresent()) {
                        // todo 可能有父类子类的问题, 是否应该以 source task 的类型为准呢?
                        Object dataWithType = JSON.parseObject(theNodeData.get().getContent(), theField.getType());
                        theField.set(taskInst, dataWithType);
                    }
                    break;
                }
            }
            // 如果执行到这里, 说明没有发现 match
            logger.error("failed to find data for arg {}", theField.getName());
        }
        return taskInst;
    }

}
