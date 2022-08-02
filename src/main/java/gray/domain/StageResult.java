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

}
