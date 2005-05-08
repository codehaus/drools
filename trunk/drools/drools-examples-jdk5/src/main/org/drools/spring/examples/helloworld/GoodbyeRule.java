package org.drools.spring.examples.helloworld;

import org.drools.spring.metadata.annotation.java.*;

@Rule
public class GoodbyeRule {
    private MessagePrinter messagePrinter;

    public void setMessagePrinter(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
    }

    @Condition
    public boolean condition(@Fact("goodbye") String goodbye) {
        return goodbye.equals("Goodbye");
    }

    @Consequence
    public void consequence(@Fact("goodbye") String goodbye) {
        messagePrinter.goodbyeWorld(goodbye);
    }
}
