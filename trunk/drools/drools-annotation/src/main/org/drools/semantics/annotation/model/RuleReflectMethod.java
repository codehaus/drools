package org.drools.semantics.annotation.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;

import org.drools.rule.Rule;
import org.drools.spi.Tuple;

class RuleReflectMethod implements Externalizable
{
    private Rule rule;
    private Object pojo;
    private Method method;
    private ParameterValue[] parameterValues;

    /**
     * Not intended to be called. Required only for Externalizable. 
     */
    public RuleReflectMethod() {}
    
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

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeObject(rule);
        out.writeObject(pojo);
        out.writeObject(method.getName());
        out.writeObject(method.getParameterTypes());
        out.writeObject(parameterValues);
    }

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException
    {
        rule = (Rule) in.readObject();
        pojo = in.readObject();
        
        String methodName = (String) in.readObject();
        Class[] parameterTypes = (Class[]) in.readObject(); 
        try
        {
            method = pojo.getClass().getMethod(methodName, parameterTypes);
        }
        catch ( Exception e )
        {
            throw new RuntimeException(e);
        }
        
        parameterValues = (ParameterValue[]) in.readObject();
    }
    
    public String toString() {
        return pojo.getClass().getSimpleName() 
                + "." + method.getName()
                + "(" + toStringParamterTypes() + ")";
    }
    
    private String toStringParamterTypes() {
        return "...";
//        StringBuilder value = new StringBuilder();
//        String prefix = "";
//        for (Class clazz : method.getParameterTypes()) {
//            value.append(prefix).append(clazz.getSimpleName());
//            prefix = ",";
//        }
//        return value.toString();
    }
}
