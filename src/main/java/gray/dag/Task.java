package gray.dag;

import gray.domain.StageResult;

public interface Task {
    public StageResult execute();
}
