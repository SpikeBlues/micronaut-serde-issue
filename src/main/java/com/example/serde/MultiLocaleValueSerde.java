package com.example.serde;

import com.example.MultiLocaleValue;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.ArrayUtils;
import io.micronaut.serde.*;
import io.micronaut.serde.exceptions.SerdeException;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Singleton
public class MultiLocaleValueSerde<T extends Serializable> implements Serde<MultiLocaleValue<T>> {
    /**
     * default fallback locale
     */
    private final Locale defaultFallback;

    /**
     * value argument
     */
    private final Argument<T> valueArgument;

    /**
     * value deserializer
     */
    private final Deserializer<? extends T> valueDeserializer;

    public MultiLocaleValueSerde() {
        this.defaultFallback = Locale.ENGLISH;;
        this.valueArgument = null;
        this.valueDeserializer = null;
    }

    public MultiLocaleValueSerde(Locale defaultFallback, Argument<T> valueArgument, Deserializer<? extends T> valueDeserializer) {
        this.defaultFallback = defaultFallback;
        this.valueArgument = valueArgument;
        this.valueDeserializer = valueDeserializer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(@NonNull Encoder encoder, EncoderContext context,
                          @NonNull Argument<? extends MultiLocaleValue<T>> type, @NonNull MultiLocaleValue<T> value)
            throws IOException {
//        boolean isFiltered = type.getAnnotationMetadata().isAnnotationPresent(MultiLocaleFiltered.class);
//        Map<String, T> locales = MultiLocaleUtils.filterValue(encoder.currentPath(), value, isFiltered,
//                this.defaultFallback);

        Map<String, T> locales = new HashMap<>();
        value.getValues().forEach((o, o2) -> locales.put(o.toLanguageTag(), o2));
        // slow path, generic look up per element
        final Encoder childEncoder = encoder.encodeObject(type);
        Class<?> lastValueClass = null;
        Serializer<? super T> componentSerializer = null;
        Argument<T> generic = null;
        for (Map.Entry<String, T> entry : locales.entrySet()) {
            encoder.encodeKey(entry.getKey());
            T t = entry.getValue();
            if (t == null) {
                encoder.encodeNull();
            } else {
                if (componentSerializer == null || lastValueClass != t.getClass()) {
                    generic = (Argument<T>) Argument.of(t.getClass());
                    componentSerializer = context.findSerializer(generic).createSpecific(context, generic);
                    lastValueClass = t.getClass();
                }
                componentSerializer.serialize(childEncoder, context, generic, t);
            }
        }
        childEncoder.finishStructure();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Deserializer<MultiLocaleValue<T>> createSpecific(DecoderContext context,
                                                                     @NonNull Argument<? super MultiLocaleValue<T>> type) throws SerdeException {
        final Argument<?>[] generics = type.getTypeParameters();
        if (ArrayUtils.isEmpty(generics)) {
            return this;
        }
        final Argument<T> collectionItemArgument = (Argument<T>) generics[0];
        Deserializer<? extends T> serialValueDeser = context.findDeserializer(collectionItemArgument)
                .createSpecific(context, collectionItemArgument);
        return new MultiLocaleValueSerde<>(defaultFallback, collectionItemArgument,
                serialValueDeser);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable MultiLocaleValue<T> deserialize(@NonNull Decoder decoder, DecoderContext context,
                                                     @NonNull Argument<? super MultiLocaleValue<T>> type) throws IOException {
        final Decoder objectDecoder = decoder.decodeObject(type);
        MultiLocaleValue<T> multiLocaleValue = new MultiLocaleValue<>();

        String key = objectDecoder.decodeKey();
        while (key != null) {
            final Locale.Builder builder = new Locale.Builder();
            if (valueDeserializer == null) {
                log.warn("valueDeserializer is null, decode by arbitrary type");
                multiLocaleValue.withValue(builder.setLanguageTag(key).build(),
                        (T) objectDecoder.decodeArbitrary());
            } else {
                multiLocaleValue.withValue(builder.setLanguageTag(key).build(),
                        valueDeserializer.deserializeNullable(objectDecoder, context, valueArgument));
            }
            key = objectDecoder.decodeKey();

        }
        objectDecoder.finishStructure();
        return multiLocaleValue;
    }

    @Override
    public boolean isEmpty(EncoderContext context, MultiLocaleValue<T> value) {
        if (value == null) {
            return true;
        }
        return MapUtils.isEmpty(value.getValues());
    }
}
