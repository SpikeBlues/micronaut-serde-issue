package com.example;

import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(startApplication = false)
class SerdeIssueTest {

    @Test
    void testWriteReadPoint(ObjectMapper objectMapper) throws IOException {
        String result = objectMapper.writeValueAsString(
                Point.valueOf(50, 100)
        );
        Point<Integer> point = objectMapper.readValue(result, Point.class);
        assertNotNull(point);
        List<Integer> coords = point.coords();
        assertEquals(50, coords.get(0));
        assertEquals(100, coords.get(1));
    }
}
