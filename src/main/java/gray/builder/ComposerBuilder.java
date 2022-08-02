package gray.builder;

import gray.builder.types.RootTaskBuilder;
import gray.domain.FlowInput;

public interface ComposerBuilder {
     RootTaskBuilder build(FlowInput flowInput);
}
