package com.avorona;

import java.nio.channels.CancelledKeyException;

/**
 * Created by avorona on 03.03.16.
 */
public class TypeCheckingClosure {
    private static class TypeClosure<T> {

        private Class<T> clazz;

        public TypeClosure(Class<T> tClass) {
            this.clazz = tClass;
        }

        public boolean check(Object obj) {
            return clazz.isInstance(obj);
        }

        public void printCheck(Object object) {
            System.out.println(String.format("Is object type is the same as %s, or assignable: %s",
                    clazz.getSimpleName(), check(object)));
        }
    }

    public static void main(String[] args) {
        TypeClosure<String> stringClosure = new TypeClosure<>(String.class);

        stringClosure.printCheck(new Object());
        stringClosure.printCheck(true);
        stringClosure.printCheck(1);
        stringClosure.printCheck("yes");
    }

}
