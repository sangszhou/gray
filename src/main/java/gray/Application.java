package gray;

import gray.dag.FlowAssembler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {
        public static void main(String[] args) {

            new FlowAssembler() {
                {
                    System.out.println(this.getClass().getName());
                    System.out.println("hello");

                    new FlowAssembler() {{
                        System.out.println(this.getClass().getName());
                        System.out.println("inner2");
                    }};
                }
            };
            SpringApplication.run(Application.class, args);
    }

}