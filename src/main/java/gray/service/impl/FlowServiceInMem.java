package gray.service.impl;

import gray.service.FlowService;
import gray.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowServiceInMem implements FlowService {
    // 查询 flow 数据的执行情况
    @Autowired
    NodeService nodeService;

    @Override
    public int queryFlowResult(String flowId) {

        return 0;
    }
}
