package gray.domain;

public class ApolloDeployReq {
    String appName;
    String env;
    String operator;
    boolean pauseBetweenBatch;
    int batchNum;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean isPauseBetweenBatch() {
        return pauseBetweenBatch;
    }

    public void setPauseBetweenBatch(boolean pauseBetweenBatch) {
        this.pauseBetweenBatch = pauseBetweenBatch;
    }

    public int getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(int batchNum) {
        this.batchNum = batchNum;
    }
}
