package org.drools.semantics.annotation.model;

import java.io.Serializable;

import org.drools.spi.Tuple;

interface ParameterValue extends Serializable
{
    Object getValue( Tuple tuple );
}
