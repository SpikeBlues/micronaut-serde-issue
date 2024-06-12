package com.example;

import io.micronaut.runtime.Micronaut;
import io.micronaut.serde.annotation.SerdeImport;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
