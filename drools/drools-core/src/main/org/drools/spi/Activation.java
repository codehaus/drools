package org.drools.spi;

import org.drools.rule.Rule;

public interface Activation
{
    Rule getRule();
    Tuple getTuple();
}
