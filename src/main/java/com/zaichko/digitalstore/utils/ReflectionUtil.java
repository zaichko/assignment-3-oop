package com.zaichko.digitalstore.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static void inspect(Object obj) {
        Class<?> clazz = obj.getClass();

        System.out.println("Class: " + clazz.getSimpleName());

        System.out.println("Fields:");
        for (Field f : clazz.getDeclaredFields()) {
            System.out.println(" - " + f.getName());
        }

        System.out.println("Methods:");
        for (Method m : clazz.getDeclaredMethods()) {
            System.out.println(" - " + m.getName());
        }
    }
}