package org.drools.spring.pojorule;

import java.io.Serializable;
import org.drools.spi.Tuple;

public interface Argument extends Serializable {

    Object getValue(Tuple tuple);
    
}