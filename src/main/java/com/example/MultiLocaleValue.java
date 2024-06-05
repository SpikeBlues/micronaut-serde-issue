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
     * Gets the best matching value according to the provided range
     * <p>
     * The {@code ranges} to be given can take one of the following forms:
     *
     * <pre>
     *   "Accept-Language: ja,en;q=0.4"  (weighted list with Accept-Language prefix)
     *   "ja,en;q=0.4"                   (weighted list)
     *   "ja,en"                         (prioritized list)
     * </pre>
     *
     * Will try to get the closest value if there is no exact match (ex: if
     * requested 'en-GB', 'en' may be return as a second choice) Null if not found
     *
     * @param ranges
     *            The locale to search
     * @return The locale value
     */
    public Map.Entry<Locale, T> getValue(String ranges) {
        List<Locale.LanguageRange> rangesList = Locale.LanguageRange.parse(ranges);
        Locale locale = Locale.lookup(rangesList, getValues().keySet());
        return locale == null ? null : new AbstractMap.SimpleEntry<>(locale, getValues().get(locale));
    }

    /**
     * Adds a new entry to this multi language field Replaces in case of conflict
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
