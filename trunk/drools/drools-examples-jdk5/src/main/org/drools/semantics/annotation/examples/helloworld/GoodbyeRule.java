package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.Parameter;
import org.drools.semantics.annotation.*;

@Rule
public class GoodbyeRule {
    private MessagePrinter messagePrinter;

    public void setMessagePrinter(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
    }

    @Condition
    public boolean condition(@Parameter("goodbye") String goodbye) {
        return goodbye.equals("Goodbye");
    }

    @Consequence
    public void consequence(@Parameter("goodbye") String goodbye) {
        messagePrinter.goodbyeWorld(goodbye);
    }
}
