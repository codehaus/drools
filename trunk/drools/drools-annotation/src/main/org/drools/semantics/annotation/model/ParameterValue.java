package org.drools.semantics.annotation.model;

import org.drools.spi.Tuple;

interface ParameterValue
{
    Object getValue( Tuple tuple );
}
