package org.drools.spi;

import org.drools.rule.Rule;

public interface Firing
{
    Rule getRule();
    Tuple getTuple();
}
