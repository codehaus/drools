package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.DroolsRule;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.DroolsConsequence;

@DroolsRule
class HelloRule {
    private MessagePrinter messagePrinter;
    
    public void setMessagePrinter(MessagePrinter messagePrinter) {
        this.messagePrinter = messagePrinter;
    }
    
    @DroolsCondition
    public boolean condition(@DroolsParameter("hello") String hello) {
        return hello.equals("Hello");
    }
    
    @DroolsConsequence
    public void consequence(@DroolsParameter("hello") String hello) {
        messagePrinter.helloWorld(hello);
    }
}
