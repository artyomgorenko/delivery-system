package ru.delivery.system.common.utils;

import java.util.function.Function;

public class Utils {
    public static <T, U> U mapWithFallback(T obj, Function<T, U> function, U fallback) {
        if(obj == null) {
            return fallback;
        } else {
            return function.apply(obj);
        }
    }
}
