package com.example;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Point {
    private final int x, y;

    public int[] coords() {
        return new int[] { x, y };
    }

    public static Point valueOf(int x, int y) {
        return new Point(x, y);
    }
}
