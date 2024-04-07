package com.spring.test;

import java.util.Arrays;

public class Test {

	public static void main(String[] args) {
        String classpath = System.getProperty("java.class.path");
        String[] arrayOfClassPath = classpath.split(":");
       // System.out.println("Classpath: " + classpath);
        Arrays.stream(arrayOfClassPath).forEach(System.out::println);
    }
	
}
