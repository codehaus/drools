package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.Drools;

@Drools.Rule
class HelloRule {
    private MessagePrinter messagePrinter;
    
    public void setMessagePrinter(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
    }
    
    @Drools.Condition
    public boolean condition(@Drools.Parameter("hello") String hello) {
        return hello.equals("Hello");
    }
    
    @Drools.Consequence
    public void consequence(@Drools.Parameter("hello") String hello) {
        messagePrinter.helloWorld(hello);
    }
}
