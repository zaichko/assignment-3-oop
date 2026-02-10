package kz.aitu.digitalcontent.utils;

import kz.aitu.digitalcontent.model.BaseEntity;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {

    public static <T extends BaseEntity> void sortById(List<T> entities) {
        entities.sort((e1, e2) -> Integer.compare(e1.getId(), e2.getId()));
    }

    public static <T extends BaseEntity> void sortByName(List<T> entities) {
        entities.sort(Comparator.comparing(BaseEntity::getName));
    }

    public static <T> void sortWithComparator(List<T> items, Comparator<T> comparator) {
        items.sort(comparator);
    }

    public static <T extends BaseEntity> List<T> filterByNameContains(List<T> entities, String keyword) {
        return entities.stream()
                .filter(e -> e.getName().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public static <T> long countMatching(List<T> items, java.util.function.Predicate<T> predicate) {
        return items.stream()
                .filter(predicate)
                .count();
    }
}