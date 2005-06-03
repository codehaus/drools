package org.drools.spring.examples.helloworld;

import org.drools.spring.metadata.annotation.java.*;

@Rule
public class DebugRule {

    @Consequence
    public void consequence(@Fact("object") Object object) {
        System.out.println("\n" + "Asserted object: " + object);
    }
}
