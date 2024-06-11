package com.example;

import lombok.Generated;
import lombok.Value;
import lombok.experimental.NonFinal;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Value
@NonFinal
public class MultiLocaleValue<T extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** The map of values according to a locale */
    HashMap<Locale, T> values = new HashMap<>();

    /**
     * Explicit no-arguments constructor
     */
    @Generated
    public MultiLocaleValue() {
        // To evade javadoc lint complain the uncommented implicit constructor
    }

    /**
     * Adds a new entry to this multi locale field
     * Replaces in case of conflict
     * Won't add anything null
     *
     * @param locale
     *            The locale to add
     * @param value
     *            The value to add
     * @return The instance
     */
    public MultiLocaleValue<T> withValue(Locale locale, T value) {
        if (locale != null && value != null) {
            getValues().put(locale, value);
        }
        return this;
    }
}
