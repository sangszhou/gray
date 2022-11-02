package gray.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class IdUtilsTest {

    @Test
    public void testGen() {
        String id = IdUtils.generateId("atom");
        System.out.println(id);
    }

}