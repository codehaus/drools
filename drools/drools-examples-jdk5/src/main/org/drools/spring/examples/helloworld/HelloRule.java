package org.drools.spring.examples.helloworld;

import org.drools.spring.metadata.annotation.java.*;

@Rule
public class HelloRule {
    private MessagePrinter messagePrinter;

    public void setMessagePrinter(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
    }

    @Condition
    public boolean condition(@Fact("hello") String hello) {
        return hello.equals("Hello");
    }

    @Consequence
    public void consequence(@Fact("hello") String hello) {
        messagePrinter.helloWorld(hello);
    }
}
