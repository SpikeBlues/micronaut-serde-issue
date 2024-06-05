package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Value;
import java.io.Serial;
import java.io.Serializable;

@Serdeable
@Value
public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String title;
    @JsonProperty("qty")
    int quantity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    MultiLocaleValue<String> multiLocaleValue;

    @JsonCreator
    public Book(String title, int quantity, MultiLocaleValue<String> multiLocaleValue) {
        this.title = title;
        this.quantity = quantity;
        this.multiLocaleValue = multiLocaleValue;
    }
}
