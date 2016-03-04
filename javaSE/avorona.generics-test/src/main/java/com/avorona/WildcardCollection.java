package com.avorona;

import java.util.*;

/**
 * Created by avorona on 03.03.16.
 */
public class WildcardCollection {

    static class Lawyer extends GenericSorting.Person {
    }

    static class SubLawyer extends Lawyer {
    }

    public static void main(String[] args) {
        List<? extends GenericSorting.Person> list = new ArrayList<>();
//        Has error
//        list.add(new GenericSorting.Person());

//        Has error to
//        list.add(new Lawyer());

        Stack<? super GenericSorting.Person> personList = new Stack<>();
        Queue<? super GenericSorting.Person> personList2 = new PriorityQueue<>();

//        Works fine for Covariances
        personList.add(new Lawyer());
        personList.add(new GenericSorting.Person());
        personList.add(new SubLawyer()); //Capture even 3-d layer extension
    }
}
