package org.drools.semantics.annotation.examples.helloworld;

class MessagePrinter {
    
    public void helloWorld(String hello) {
        System.out.println("\n" + hello + " World");
    }

    public void goodbyeWorld(String goodbye) {
        System.out.println("\n" + goodbye + " Cruel World");
    }
}
