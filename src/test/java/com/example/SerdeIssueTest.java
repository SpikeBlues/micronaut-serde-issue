package com.example;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(startApplication = false)
class SerdeIssueTest {

    @SuppressWarnings({"unchecked", "raw"})
    @Test
    void shouldSerializeBook(ObjectMapper objectMapper) throws IOException {
        MultiLocaleValue<String> localeValue =
                new MultiLocaleValue<String>()
                        .withValue(Locale.ENGLISH, "English content")
                        .withValue(Locale.CHINA, "Chinese content");
        // nested case
        //        MultiLocaleValue<Book> test =
        //                new MultiLocaleValue<Book>()
        //                        .withValue(Locale.UK, new Book("EN", 1, localeValue))
        //                        .withValue(Locale.CHINA, new Book("CN", 3, null));
        Book test = new Book("title", 2, localeValue);
        String json = objectMapper.writeValueAsString(test);
        Book test2 =
                objectMapper.readValue(json, Argument.of(Book.class));
        assertEquals(test2, test);
    }

    @Test
    void shouldSerializePoint(ObjectMapper objectMapper) throws IOException {
        String result = objectMapper.writeValueAsString(Point.valueOf(50, 100));
        Point point = objectMapper.readValue(result, Point.class);
        assertNotNull(point);
        int[] coords = point.coords();
        assertEquals(50, coords[0]);
        assertEquals(100, coords[1]);
    }
}
