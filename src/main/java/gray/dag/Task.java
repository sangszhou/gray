package gray.dag;

import gray.domain.StageResult;

public interface Task {
    StageResult execute();

    StageResult query();
}
