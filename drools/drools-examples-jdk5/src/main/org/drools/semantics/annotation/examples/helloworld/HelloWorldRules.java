package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.Parameter;
import org.drools.semantics.annotation.*;

class HelloWorldRules {
    
    @Rule
    public static class Hello {
        
        @Condition
        public boolean condition(@Parameter("hello") String hello) {
            return hello.equals("Hello");
        }
        
        @Consequence
        public void consequence(@Parameter("hello") String hello) {
            System.out.println("\n" + hello + " World");
        }
    }

    @Rule
    public static class Goodbye {
        @Condition
        public boolean condition(@Parameter("goodbye") String goodbye) {
            return goodbye.equals("Goodbye");
        }
        
        @Consequence
        public void consequence(@Parameter("goodbye") String goodbye) {
            System.out.println("\n" + goodbye + " Cruel World");
        }
    }
    
    @Rule
    public static class Debug {

        @Consequence
        public void consequence(@Parameter("object") Object object) {
            System.out.println("\n" + "Asserted object: " + object);
        }
    }
}
