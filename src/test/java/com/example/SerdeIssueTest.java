package com.example;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.Serializable;
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
        Book test = new Book("title", 2, localeValue);
        String json = objectMapper.writeValueAsString(test);
        Book test2 =
                objectMapper.readValue(json, Argument.of(Book.class));
        assertEquals(test2, test);
    }

    @SuppressWarnings({"unchecked", "raw"})
    @Test
    void shouldSerializeNestedMultiLocale(ObjectMapper objectMapper) throws IOException {
        MultiLocaleValue<String> localeValue =
                new MultiLocaleValue<String>()
                        .withValue(Locale.ENGLISH, "English content")
                        .withValue(Locale.CHINA, "Chinese content");
        MultiLocaleValue<Serializable> nestedValue =
                new MultiLocaleValue<>().withValue(Locale.UK, localeValue);
        String json = objectMapper.writeValueAsString(nestedValue);
        MultiLocaleValue<MultiLocaleValue<String>> test2 =
                objectMapper.readValue(json, Argument.of(MultiLocaleValue.class,
                        MultiLocaleValue.class));
        assertEquals(test2, nestedValue);
    }


    @SuppressWarnings("unchecked")
    @Test
    void shouldSerializeNestedBook(ObjectMapper objectMapper) throws IOException {
        MultiLocaleValue<String> localeValue =
                new MultiLocaleValue<String>()
                        .withValue(Locale.ENGLISH, "English content")
                        .withValue(Locale.CHINA, "Chinese content");
        // nested case
        MultiLocaleValue<Serializable> book =
                new MultiLocaleValue<>()
                        .withValue(Locale.UK, new Book("EN title", 1, localeValue))
                        .withValue(Locale.CHINA, new Book("CN title", 3, null));
        String json = objectMapper.writeValueAsString(book);
        MultiLocaleValue<Book> test2 =
                (MultiLocaleValue<Book>) objectMapper.readValue(json, Argument.of(MultiLocaleValue.class, Book.class));
        assertEquals(book, test2);
    }

}
