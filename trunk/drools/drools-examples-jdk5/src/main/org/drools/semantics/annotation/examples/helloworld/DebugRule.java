package org.drools.semantics.annotation.examples.helloworld;

import org.drools.semantics.annotation.Parameter;
import org.drools.semantics.annotation.*;

@Rule
public class DebugRule {

    @Consequence
    public void consequence(@Parameter("object") Object object) {
        System.out.println("\n" + "Asserted object: " + object);
    }
}
