package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.*;

@DroolsRule
public class DebugRule {

    @DroolsConsequence
    public void consequence(@DroolsParameter("object") Object object) {
        System.out.println("\n" + "Asserted object: " + object);
    }
}
