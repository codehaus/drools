package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.*;

@Drools.Rule
class DebugRule {

    @Drools.Consequence
    public void consequence(@Drools.Parameter("object") Object object) {
        System.out.println("\n" + "Asserted object: " + object);
    }
}
