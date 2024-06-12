package com.example;

import java.io.Serializable;
import java.util.List;

public final class Point<T extends Serializable> implements Serializable{
    private final T x, y;

    private Point(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public List<T> coords() {
        return List.of(x, y);
    }

    public static <T extends Serializable> Point<T> valueOf(T x, T y) {
        return new Point<>(x, y);
    }
}
