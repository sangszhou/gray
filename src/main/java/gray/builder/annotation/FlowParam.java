package gray.builder.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FlowParam {
    boolean required() default true;

    String label() default "";
}
