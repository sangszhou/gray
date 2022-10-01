package gray.demo.apollo.flow;

import gray.builder.ComposerBuilder;
import gray.builder.annotation.FlowParam;
import gray.builder.types.RootTaskBuilder;
import gray.domain.FlowInput;

public class ApolloHostDeployComposer extends ComposerBuilder {
    @FlowParam
    String hostId;

    @Override
    public RootTaskBuilder doBuild(FlowInput flowInput) {
        RootTaskBuilder root = new RootTaskBuilder();



        return root;
    }
}
