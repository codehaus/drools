package org.drools.spring.factory;

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



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.DroolsException;
import org.drools.rule.Rule;
import org.drools.spring.metadata.ArgumentMetadata;
import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.PojoCondition;
import org.drools.spring.pojorule.PojoConsequence;
import org.drools.spring.pojorule.RuleReflectMethod;

public class RuleBuilder {

    public static final class InvalidReturnTypeException extends DroolsException {
        InvalidReturnTypeException(String message) {
            super(message);
        }
    }

    public static final class InvalidParameterException extends DroolsException {
        InvalidParameterException(String message) {
            super(message);
        }
    }
    
    public static final class InvalidPojoConditionException extends DroolsException {
        InvalidPojoConditionException(String msg, Throwable rootCause) {
            super(msg, rootCause);
        }

        InvalidPojoConditionException(String message) {
            super(message);
        }
    }

    // ---- ---- ----

    private MethodMetadataSource methodMetadataSource;
    private ArgumentMetadataSource argumentMetadataSource;

    public void setMethodMetadataSource(MethodMetadataSource methodMetadataSource) {
        this.methodMetadataSource = methodMetadataSource;
    }

    public void setArgumentMetadataSource(ArgumentMetadataSource argumentMetadataSource) {
        this.argumentMetadataSource = argumentMetadataSource;
    }

    // ---- ---- ----

    private static Log log = LogFactory.getLog(RuleBuilder.class);

    public Rule buildRule(Rule rule, Object pojo) throws DroolsException {
        List conditionRuleReflectMethods = new ArrayList();

        Method[] pojoMethods = pojo.getClass().getMethods();
        for (int i = 0; i < pojoMethods.length; i++) {
            Method pojoMethod = pojoMethods[i];
            MethodMetadata methodMetadata = methodMetadataSource.getMethodMetadata(pojoMethod);
            if (methodMetadata == null) {
                if (log.isDebugEnabled()) {
                    log.debug("No metadata for method " + pojoMethod.toString());
                }
                continue;
            }

            ArgumentMetadata[] argumentsMetadata = getArgumentMetadata(pojoMethod);
            Argument[] arguments = getArguments(rule, argumentsMetadata);

            if (methodMetadata.getMethodType() == MethodMetadata.METHOD_CONDITION) {
                assertReturnType(pojoMethod, boolean.class);
                rule.addCondition(
                        new PojoCondition(new RuleReflectMethod(rule, pojo, pojoMethod, arguments)));
                log.info("Condition method added to rule: " + pojoMethod.toString());

            } else if (methodMetadata.getMethodType() == MethodMetadata.METHOD_CONSEQUENCE) {
                conditionRuleReflectMethods.add(
                        new RuleReflectMethod(rule, pojo, pojoMethod, arguments));
                log.info("Consequence method added to rule: " + pojoMethod.toString());
            } else if (methodMetadata.getMethodType() == MethodMetadata.OBJECT_CONDITION) {
                if (arguments.length != 0) {
                    throw new InvalidPojoConditionException("Rule pojo condition must not have arguments"
                                                            + ": method = " + pojoMethod + ", arguments = " + arguments);
                }
                try {
                    buildObjectConditions(rule, pojoMethod.invoke(pojo, new Object[0]));
                } catch (Exception e) {
                    throw new InvalidPojoConditionException("Unable to execute pojo condition"
                                                            + ": method = " + pojoMethod, e);
                }
            }
        }

        if (!conditionRuleReflectMethods.isEmpty()) {
            addConsequence(rule, conditionRuleReflectMethods);
        }

        rule.checkValidity();
        return rule;
    }

    private void buildObjectConditions(Rule rule, Object pojo) throws DroolsException {
        Method[] pojoMethods = pojo.getClass().getMethods();
        for (int i = 0; i < pojoMethods.length; i++) {
            Method pojoMethod = pojoMethods[i];
            MethodMetadata methodMetadata = methodMetadataSource.getMethodMetadata(pojoMethod);
            if (methodMetadata == null) {
                continue;
            }
    
            ArgumentMetadata[] argumentsMetadata = getArgumentMetadata(pojoMethod);
            Argument[] arguments = getArguments(rule, argumentsMetadata);
    
            if (methodMetadata.getMethodType() == MethodMetadata.METHOD_CONDITION) {
                assertReturnType(pojoMethod, boolean.class);
                rule.addCondition(new PojoCondition(new RuleReflectMethod(rule, pojo, pojoMethod, arguments)));
    
            }
        }
    }

    private ArgumentMetadata[] getArgumentMetadata(Method pojoMethod) throws InvalidParameterException {
        Class[] parameterTypes = pojoMethod.getParameterTypes();
        ArgumentMetadata[] metadata = new ArgumentMetadata[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            metadata[i] = argumentMetadataSource.getArgumentMetadata(pojoMethod, parameterTypes[i], i);
            if (metadata[i] == null) {
                throw new InvalidParameterException("Cannot determine parameter metdata"
                        + ": method=" + pojoMethod.getName()
                        + ", parameterType[" + i + "]=" + parameterTypes[i]);
            }
        }
        return metadata;
    }

    private Argument[] getArguments(Rule rule, ArgumentMetadata[] argumentsMetadata) throws DroolsException {
        Argument[] arguments = new Argument[argumentsMetadata.length];
        for (int i = 0; i < argumentsMetadata.length; i++) {
            arguments[i] = argumentsMetadata[i].createArgument(rule);
        }
        return arguments;
    }

    private static void assertReturnType(Method method, Class returnClass)
            throws InvalidReturnTypeException {
        if (method.getReturnType() != returnClass) {
            throw new InvalidReturnTypeException("Rule method returns the wrong class"
                    + ": method = " + method + ", expected return class = " + returnClass
                    + ", actual return class = " + method.getReturnType());
        }
    }

    private void addConsequence(Rule rule, List conditionRuleReflectMethodsCollector) {
        RuleReflectMethod[] conditionRuleReflectMethodArray = (RuleReflectMethod[]) conditionRuleReflectMethodsCollector.toArray(
                new RuleReflectMethod[conditionRuleReflectMethodsCollector.size()]);
        PojoConsequence consequence = new PojoConsequence(conditionRuleReflectMethodArray);
        rule.setConsequence(consequence);
    }
}

