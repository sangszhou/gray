package gray.domain;

public class StageResult {
    // 0 -> invalid, 1 -> running, 2 -> success, 3 -> fail
    int code;
    String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static StageResult RunningResult() {
        StageResult result = new StageResult();
        result.setCode(1);
        result.setMsg("fail");
        return result;
    }

    public static StageResult SuccessResult() {
        StageResult result = new StageResult();
        result.setCode(2);
        result.setMsg("success");
        return result;
    }

    public static StageResult FailResult() {
        StageResult result = new StageResult();
        result.setCode(3);
        result.setMsg("fail");
        return result;
    }



}
