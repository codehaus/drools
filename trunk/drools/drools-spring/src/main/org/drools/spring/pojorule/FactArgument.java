package org.drools.spring.pojorule;

import org.drools.rule.Declaration;
import org.drools.spi.Tuple;

public class FactArgument implements Argument {

    private final Declaration declaration;

    public FactArgument(Declaration declaration) {
        this.declaration = declaration;
    }

    public Object getValue(Tuple tuple) {
        return tuple.get(declaration);
    }

    public Declaration getDeclaration() {
        return declaration;
    }
}
