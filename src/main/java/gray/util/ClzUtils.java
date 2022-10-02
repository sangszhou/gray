package gray.util;

import com.alibaba.dag.composer.exception.AssertException;
import gray.engine.Node;
import gray.engine.ParamLinker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClzUtils {

    // 根据目标值来判断吗?
    public static <T> T castTo(String clazz) {
        try {
            return (T) Class.forName(clazz);
        } catch (Exception var2) {
            throw new AssertException(var2);
        }
    }

    public static <T extends Annotation> T getAnnotation(String clazzStr, Class<T> anno) {
        Class<?> clazz = (Class)castTo(clazzStr);
        return clazz.getAnnotation(anno);
    }

    private static List<Class<?>> getAllLevelClass(Class<?> clazz) {
        List<Class<?>> allLevelClass = new ArrayList();
        if (clazz == null) {
            return allLevelClass;
        } else {
            do {
                allLevelClass.add(clazz);
                clazz = clazz.getSuperclass();
            } while(clazz != null);

            return allLevelClass.subList(0, allLevelClass.size() - 1);
        }
    }

    public static List<Field>  getFieldsWithAnnotation(Class sourceClz, Class<? extends Annotation> fieldAnno) {
        List<Field> list = new ArrayList();
        List<Class<?>> allLevelClass = getAllLevelClass(sourceClz);
        allLevelClass.forEach(oneClz -> {
            Field[] fieldList = oneClz.getDeclaredFields();
            Arrays.stream(fieldList).forEach(field -> {
                if (field.isAnnotationPresent(fieldAnno) && !Modifier.isStatic(field.getModifiers())) {
                    String name = field.getName();
                    if (list.contains(name)) {
                        throw new AssertException(String.format("field:%s is duplicated in %s", name,
                                sourceClz.getSimpleName()));
                    }
                    list.add(field);
                }
            });
        });
        return list;
    }

    // 从 source object 中获取
//    public static <T> T retrieveFromSource(ParamLinker paramLinker, Object sourceObj) {
//        String sourceFieldName = paramLinker.getSourceFieldName();
//
//    }

    // 从已完成的节点中 build node
//    public static <T> T buildTaskInstanceFromNode(Node basicNode) {
//        Class<?> clz = castTo(basicNode.getClzName());
//
//    }

}
