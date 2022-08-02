package gray.engine;

public enum NodeStatus {
    INVALID, // 还未初始化
    INIT,
    RUNNING,
    // query 和 running 怎么区分呢
    QUERY,
    SKIPPED,
    SUCCESS,
    FAIL,
}
