package org.drools.semantics.annotation.model;

import java.lang.reflect.Method;

import org.drools.rule.Rule;
import org.drools.spi.Tuple;

class RuleReflectMethod
{
    private final Rule rule;
    private final Object pojo;
    private final Method method;
    private final ParameterValue[] parameterValues;

    public RuleReflectMethod( Rule rule, Object pojo, Method method,
            ParameterValue[] parameterValues )
    {
        this.rule = rule;
        this.pojo = pojo;
        this.method = method;
        this.parameterValues = parameterValues;
    }

    public ParameterValue[] getParameterValues( )
    {
        return parameterValues;
    }

    public Object invokeMethod( Tuple tuple ) throws Exception
    {
        method.setAccessible( true );
        return method.invoke( pojo, getMethodArguments( tuple ) );
    }

    private Object[] getMethodArguments( Tuple tuple )
    {
        Object[] args = new Object[parameterValues.length];
        for (int i = 0; i < parameterValues.length; i++)
        {
            args[i] = parameterValues[i].getValue( tuple );
        }
        return args;
    }
}
