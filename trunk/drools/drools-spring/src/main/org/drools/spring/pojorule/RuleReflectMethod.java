package org.drools.spring.pojorule;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */



import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;

import org.drools.rule.Rule;
import org.drools.spi.Tuple;

public class RuleReflectMethod implements Externalizable {

    private Rule rule;
    private Object pojo;
    private Method method;
    private Argument[] arguments;

    /**
     * Not intended to be called. Required only for Externalizable.
     */
    public RuleReflectMethod() {
    }

    public RuleReflectMethod(Rule rule, Object pojo, Method method, Argument[] arguments) {
        this.rule = rule;
        this.pojo = pojo;
        this.method = method;
        this.arguments = arguments;
    }

    public String getMethodName() {
        return method.getName();
    }

    public Argument[] getArguments() {
        return arguments;
    }

    public Object invokeMethod(Tuple tuple) throws Exception {
        method.setAccessible(true);
        return method.invoke(pojo, getMethodArguments(tuple));
    }

    private Object[] getMethodArguments(Tuple tuple) {
        Object[] args = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].getValue(tuple);
        }
        return args;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(rule);
        out.writeObject(pojo);
        out.writeObject(method.getName());
        out.writeObject(method.getParameterTypes());
        out.writeObject(arguments);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        rule = (Rule) in.readObject();
        pojo = in.readObject();

        String methodName = (String) in.readObject();
        Class[] parameterTypes = (Class[]) in.readObject();
        try {
            method = pojo.getClass().getMethod(methodName, parameterTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        arguments = (Argument[]) in.readObject();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RuleReflectMethod)) {
            return false;
        }
        final RuleReflectMethod ruleReflectMethod = (RuleReflectMethod)other;
        if (!method.equals(ruleReflectMethod.method)) {
            return false;
        }
        if (!pojo.equals(ruleReflectMethod.pojo)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = pojo.hashCode();
        result = 29 * result + method.hashCode();  
        return result;
    }

    public String toString() {
        return pojo.getClass().getName() + "." + method.getName() + "("
                + toStringParamterTypes() + ")";
    }

    private String toStringParamterTypes() {
        return "...";
        // StringBuilder value = new StringBuilder();
        // String prefix = "";
        // for (Class clazz : method.getParameterTypes()) {
        // value.append(prefix).append(clazz.getSimpleName());
        // prefix = ",";
        // }
        // return value.toString();
    }
}

