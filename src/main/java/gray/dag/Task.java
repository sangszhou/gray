package gray.dag;

import gray.domain.StageResult;

public interface Task {
    //? 是不是需要一个参数, 把 context 传进来的呢
    StageResult execute();

    StageResult query();
}
