package com.avorona;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by avorona on 03.03.16.
 */
public class GenericSorting {

    static class Person {
        private String name;
        private String surname;

        public Person(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }

        public Person() {
            this("noname", "nosurname");
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", surname='" + surname + '\'' +
                    '}';
        }
    }

    static class SharedUtils {
        public static <T, U extends Comparable> Function<T, U> getComparableKeyExtractor(Class<T> clazz,
                                                                                         String keyName) {
            try {
                Method getKeyMethod = clazz.getMethod(keyName);
                if (!Comparable.class.isAssignableFrom(getKeyMethod.getReturnType())) {
                    throw new IllegalArgumentException("Can return extractor key which return type is instance of " +
                            "Comparable");
                }

                return new Function<T, U>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public U apply(T t) {
                        try {
                            return (U) getKeyMethod.invoke(t);
                        } catch (IllegalAccessException | InvocationTargetException ignored) {
                        }
                        return null;
                    }
                };
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        List<Person> personList = new ArrayList<>();

        personList.add(new Person());
        personList.add(new Person("Bohn", "Travolta"));
        personList.add(new Person("Aarah", "Conor"));
        personList.add(new Person("Cusan", "Miller"));

        // ~ Before sorting
        System.out.println(String.format("Person list before sorting: %s", personList));

        Comparator<Person> personComparator = Comparator.comparing(Person::getName, String::compareToIgnoreCase);

        personList = personList.stream().sorted(personComparator).collect(Collectors.toList());

        // ~ After sorting
        System.out.println(String.format("Person list after sorting: %s", personList));

        Function<Person, Comparable> keyExtractor = SharedUtils.getComparableKeyExtractor(Person.class, "getName");

        //Will compile without explicit type reference, but it is added for more code clarity
        personComparator = Comparator.nullsLast(Comparator.<Person, Comparable>comparing(keyExtractor));

        personList = personList.stream()
                .sorted(personComparator)
                .collect(Collectors.toList());

        System.out.println(String.format("Person list after second sorting: %s", personList));
    }

}
