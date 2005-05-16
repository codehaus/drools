package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.*;

@DroolsRule
public class GoodbyeRule {
    private MessagePrinter messagePrinter;

    public void setMessagePrinter(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
    }

    @DroolsCondition
    public boolean condition(@DroolsParameter("goodbye") String goodbye) {
        return goodbye.equals("Goodbye");
    }

    @DroolsConsequence
    public void consequence(@DroolsParameter("goodbye") String goodbye) {
        messagePrinter.goodbyeWorld(goodbye);
    }
}
