package gray.dag;

//import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ser.std.ClassSerializer;

public class FlowDefinitionTest {
    public void test1() {
        FlowAssembler fa = new FlowAssembler() {
            {
               pushTask(new ClassSerializer().getClass(), "name1");
               new Many() {{
                  pushTask(new ClassSerializer().getClass(), "name2");
                  pushTask(new ClassSerializer().getClass(), "name3");
               }};
                pushTask(new ClassSerializer().getClass(), "name4");
            }
        };

        fa.traversal();

    }

}