package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.*;

class HelloWorldRules {
    
    @DroolsRule
    public static class Hello {
        
        @DroolsCondition
        public boolean condition(@DroolsParameter("hello") String hello) {
            return hello.equals("Hello");
        }
        
        @DroolsConsequence
        public void consequence(@DroolsParameter("hello") String hello) {
            System.out.println("\n" + hello + " World");
        }
    }

    @DroolsRule
    public static class Goodbye {
        @DroolsCondition
        public boolean condition(@DroolsParameter("goodbye") String goodbye) {
            return goodbye.equals("Goodbye");
        }
        
        @DroolsConsequence
        public void consequence(@DroolsParameter("goodbye") String goodbye) {
            System.out.println("\n" + goodbye + " Cruel World");
        }
    }
    
    @DroolsRule
    public static class Debug {

        @DroolsConsequence
        public void consequence(@DroolsParameter("object") Object object) {
            System.out.println("\n" + "Asserted object: " + object);
        }
    }
}
