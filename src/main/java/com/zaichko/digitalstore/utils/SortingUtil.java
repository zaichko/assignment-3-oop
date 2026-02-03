package com.zaichko.digitalstore.utils;

import java.util.Comparator;
import java.util.List;

public class SortingUtil {

    public static <T> void sort(List<T> items, Comparator<T> comparator) {
        items.sort(comparator);
    }

    public static <T> List<T> sortedCopy(List<T> items, Comparator<T> comparator) {
        return items.stream()
                .sorted(comparator)
                .toList();
    }

}
