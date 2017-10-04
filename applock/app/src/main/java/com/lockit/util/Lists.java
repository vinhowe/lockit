package com.lockit.util;

import com.android.internal.util.Predicate;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    public static <T, F> List<F> transform(List<T> list, Function1<T, F> func) {
        List<F> transformedList = new ArrayList<>();
        for (T t : list)
            transformedList.add(func.call(t));
        return transformedList;
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> filteredList = new ArrayList<>();
        for (T t : list)
            if (predicate.apply(t))
                filteredList.add(t);
        return filteredList;
    }
}
