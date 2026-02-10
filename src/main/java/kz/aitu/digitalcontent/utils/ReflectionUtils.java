package kz.aitu.digitalcontent.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static String getClassInfo(Object obj) {
        if (obj == null) {
            return "Object is null";
        }

        Class<?> clazz = obj.getClass();
        StringBuilder info = new StringBuilder();

        info.append("=== Class Information ===\n");
        info.append("Class Name: ").append(clazz.getName()).append("\n");
        info.append("Simple Name: ").append(clazz.getSimpleName()).append("\n");
        info.append("Package: ").append(clazz.getPackage().getName()).append("\n");
        info.append("Superclass: ").append(clazz.getSuperclass().getSimpleName()).append("\n");

        return info.toString();
    }

    public static String listFields(Object obj) {
        if (obj == null) {
            return "Object is null";
        }

        Class<?> clazz = obj.getClass();
        StringBuilder info = new StringBuilder();

        info.append("=== Fields ===\n");
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            info.append(String.format("- %s %s\n",
                    field.getType().getSimpleName(),
                    field.getName()));
        }

        return info.toString();
    }

    public static String listMethods(Object obj) {
        if (obj == null) {
            return "Object is null";
        }

        Class<?> clazz = obj.getClass();
        StringBuilder info = new StringBuilder();

        info.append("=== Methods ===\n");
        Method[] methods = clazz.getDeclaredMethods();

        String methodList = Arrays.stream(methods)
                .map(m -> String.format("- %s %s()",
                        m.getReturnType().getSimpleName(),
                        m.getName()))
                .collect(Collectors.joining("\n"));

        info.append(methodList).append("\n");

        return info.toString();
    }

    public static String inspectObject(Object obj) {
        if (obj == null) {
            return "Cannot inspect null object";
        }

        StringBuilder report = new StringBuilder();
        report.append("\n========================================\n");
        report.append("    REFLECTION INSPECTION REPORT\n");
        report.append("========================================\n\n");
        report.append(getClassInfo(obj));
        report.append("\n");
        report.append(listFields(obj));
        report.append("\n");
        report.append(listMethods(obj));
        report.append("========================================\n");

        return report.toString();
    }

    public static boolean isInstanceOf(Object obj, Class<?> targetClass) {
        return targetClass.isInstance(obj);
    }

    public static String getInterfaces(Object obj) {
        if (obj == null) {
            return "Object is null";
        }

        Class<?> clazz = obj.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();

        if (interfaces.length == 0) {
            return "No interfaces implemented";
        }

        return "Interfaces: " + Arrays.stream(interfaces)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
    }
}