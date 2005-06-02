package org.drools.spring.examples.helloworld;

import org.drools.spring.metadata.annotation.java.*;
class HelloWorldRules {

    @Rule
    public static class Hello {

        @Condition
        public boolean condition(@Fact("hello") String hello) {
            return hello.equals("Hello");
        }

        @Consequence
        public void consequence(@Fact("hello") String hello) {
            System.out.println("\n" + hello + " World");
        }
    }

    @Rule
    public static class Goodbye {
        @Condition
        public boolean condition(@Fact("goodbye") String goodbye) {
            return goodbye.equals("Goodbye");
        }

        @Consequence
        public void consequence(@Fact("goodbye") String goodbye) {
            System.out.println("\n" + goodbye + " Cruel World");
        }
    }

    @Rule
    public static class Debug {

        @Consequence
        public void consequence(@Fact("object") Object object) {
            System.out.println("\n" + "Asserted object: " + object);
        }
    }
}
