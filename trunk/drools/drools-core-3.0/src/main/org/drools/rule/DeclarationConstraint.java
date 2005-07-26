package org.drools.rule;

import org.drools.FactHandle;
import org.drools.spi.BooleanExpressionConstraint;
import org.drools.spi.ReturnValueExpressionConstraint;
import org.drools.spi.Tuple;

public class DeclarationConstraint
{        
    private final Declaration declaration;   
    
    
    public DeclarationConstraint(Declaration declaration)
    {
        this.declaration = declaration;
    }     
    
    public Declaration getDeclaration()
    {
        return this.declaration;
    }
    
}
;