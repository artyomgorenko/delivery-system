package ru.delivery.system.common.utils;

import ru.delivery.system.model.other.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class Utils {
    public static <T, U> U mapWithFallback(T obj, Function<T, U> function, U fallback) {
        if(obj == null) {
            return fallback;
        } else {
            return function.apply(obj);
        }
    }

    public static <T> Collection<T> emptyIfNull(Collection<T> collection) {
        if (collection == null) {
            return new ArrayList<>();
        } else {
            return collection;
        }
    }
}
