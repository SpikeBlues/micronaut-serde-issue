package com.example.serde;

import com.example.Point;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Decoder;
import io.micronaut.serde.Encoder;
import io.micronaut.serde.Serde;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Singleton // (1)
public class PointSerde<T extends Serializable> implements Serde<Point<T>> { // (2)
    @Override
    public Point<T> deserialize(
            Decoder decoder,
            DecoderContext context,
            Argument<? super Point<T>> type) throws IOException {
        try (Decoder array = decoder.decodeArray()) { // (3)
            Integer x = array.decodeInt();
            Integer y = array.decodeInt();
            return Point.valueOf((T)x, (T)y); // (4)
        }
    }

    @Override
    public void serialize(
            Encoder encoder,
            EncoderContext context,
            Argument<? extends Point<T>> type, Point<T> value) throws IOException {
        Objects.requireNonNull(value, "Point cannot be null"); // (5)
        List<T> coords = value.coords();
        try (Encoder array = encoder.encodeArray(type)) { // (6)
            array.encodeInt((Integer) coords.get(0));
            array.encodeInt((Integer) coords.get(1));
        }
    }
}
