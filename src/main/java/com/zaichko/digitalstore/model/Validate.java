package com.zaichko.digitalstore.model;

import java.util.List;

public interface Validate {

    void validate();

    default boolean isValid() {
        try {
            validate();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    static void validateAll(List<? extends Validate> items) {
        items.forEach(Validate::validate);
    }

}
