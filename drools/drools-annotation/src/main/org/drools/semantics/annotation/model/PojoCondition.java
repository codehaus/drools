package org.drools.semantics.annotation.model;

import java.util.ArrayList;
import java.util.List;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;

class PojoCondition implements Condition
{
    private final RuleReflectMethod ruleMethod;
    private final Declaration[] requiredDeclarations;

    public PojoCondition( RuleReflectMethod ruleMethod )
    {
        this.ruleMethod = ruleMethod;
        this.requiredDeclarations = extractDeclarations( ruleMethod.getParameterValues( ) );
    }

    private static Declaration[] extractDeclarations( ParameterValue[] parameterValues )
    {
        List<Declaration> declarations = new ArrayList<Declaration>( parameterValues.length );
        for (ParameterValue value : parameterValues)
        {
            if (value instanceof TupleParameterValue)
            {
                declarations.add( ((TupleParameterValue) value).getDeclaration( ) );
            }
        }
        return declarations.toArray( new Declaration[declarations.size( )] );
    }

    public Declaration[] getRequiredTupleMembers( )
    {
        return requiredDeclarations;
    }

    public boolean isAllowed( Tuple tuple ) throws ConditionException
    {
        try
        {
            return (Boolean) ruleMethod.invokeMethod( tuple );
        }
        catch (Exception e)
        {
            throw new ConditionException( e );
        }
    }
}