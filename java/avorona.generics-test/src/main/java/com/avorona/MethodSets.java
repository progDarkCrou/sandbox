package com.avorona;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by avorona on 02.03.16.
 */
public class MethodSets {

    private static Set<String> objects;

    private static Set<String> methodsSet(Class<?> type) {
        Set<String> methods = new TreeSet<>();
        for (Method method : type.getMethods()) {
            methods.add(Stream.of(method.getName().split("(?=[A-Z][a-z])"))
                    .reduce((s, s2) -> s + " " + s2)
                    .get());
        }
        return methods;
    }

    private static Set<String> declaredMethodsSet(Class<?> type) {
        Set<String> methods = new TreeSet<>();
        for (Method method : type.getDeclaredMethods()) {
            methods.add(method.getName());
        }
        return methods;
    }

    private static <T> Set<T> union(Set<T> typeA, Set<T> typeB) {
        Set<T> union = new HashSet<>(typeA);
        union.addAll(typeB);
        return union;
    }

    private static <T> Set<T> intersection(Set<T> typeA, Set<T> typeB) {
        Set<T> intersec = new HashSet<>(typeA);
        intersec.retainAll(typeB);
        return intersec;
    }

    private static <T> Set<T> complement(Set<T> typeA, Set<T> typeB) {
        Set<T> complement = union(typeA, typeB);

        complement.removeAll(intersection(typeA, typeB));

        return complement;
    }

    public static void main(String[] args) {
        Set<String> declaredMethods = declaredMethodsSet(List.class);
        Set<String> methods = methodsSet(List.class);

        System.out.println(Arrays.toString(ArrayList.class.getMethods()));

        System.out.println("Declared methods: " + declaredMethods);
        System.out.println("Methods: " + methods);



    }
}
