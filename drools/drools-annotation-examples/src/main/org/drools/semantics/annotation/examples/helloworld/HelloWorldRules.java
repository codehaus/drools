package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.*;

class HelloWorldRules {
    
    @Drools.Rule
    public static class Hello {
        
        @Drools.Condition
        public boolean condition(@Drools.Parameter("hello") String hello) {
            return hello.equals("Hello");
        }
        
        @Drools.Consequence
        public void consequence(@Drools.Parameter("hello") String hello) {
            System.out.println("\n" + hello + " World");
        }
    }

    @Drools.Rule
    public static class Goodbye {
        @Drools.Condition
        public boolean condition(@Drools.Parameter("goodbye") String goodbye) {
            return goodbye.equals("Goodbye");
        }
        
        @Drools.Consequence
        public void consequence(@Drools.Parameter("goodbye") String goodbye) {
            System.out.println("\n" + goodbye + " Cruel World");
        }
    }
    
    @Drools.Rule
    public static class Debug {

        @Drools.Consequence
        public void consequence(@Drools.Parameter("object") Object object) {
            System.out.println("\n" + "Asserted object: " + object);
        }
    }
}
