package org.drools.semantics.annotation.model;

import java.io.Serializable;
import org.drools.spi.Tuple;

public interface Argument extends Serializable {

    Object getValue(Tuple tuple);
}