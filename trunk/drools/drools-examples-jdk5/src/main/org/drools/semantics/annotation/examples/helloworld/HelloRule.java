package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.Rule;
import org.drools.semantics.annotation.Parameter;
import org.drools.semantics.annotation.Condition;
import org.drools.semantics.annotation.Consequence;

@Rule
public class HelloRule {
    private MessagePrinter messagePrinter;

    public void setMessagePrinter(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
    }

    @Condition
    public boolean condition(@Parameter("hello") String hello) {
        return hello.equals("Hello");
    }

    @Consequence
    public void consequence(@Parameter("hello") String hello) {
        messagePrinter.helloWorld(hello);
    }
}
