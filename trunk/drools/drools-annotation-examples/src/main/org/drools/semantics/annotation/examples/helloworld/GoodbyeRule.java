package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.*;

@Drools.Rule
class GoodbyeRule {
    private MessagePrinter messagePrinter;
    
    public void setMessagePrinter(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
    }
    
    @Drools.Condition
    public boolean condition(@Drools.Parameter("goodbye") String goodbye) {
        return goodbye.equals("Goodbye");
    }
    
    @Drools.Consequence
    public void consequence(@Drools.Parameter("goodbye") String goodbye) {
        messagePrinter.goodbyeWorld(goodbye);
    }
}
