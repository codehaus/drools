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



import java.util.ArrayList;
import java.util.List;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;

public class PojoCondition implements Condition {

    private final RuleReflectMethod ruleMethod;
    private final Declaration[] requiredDeclarations;

    public PojoCondition(RuleReflectMethod ruleMethod) {
        this.ruleMethod = ruleMethod;
        this.requiredDeclarations = extractDeclarations(ruleMethod.getArguments());
    }

    public String getMethodName() {
        return ruleMethod.getMethodName();
    }

    private static Declaration[] extractDeclarations(Argument[] arguments) {
        List declarations = new ArrayList(arguments.length);
        for (int i = 0; i < arguments.length; i++) {
            Argument argument = arguments[i];
            if (argument instanceof FactArgument) {
                declarations.add(((FactArgument) argument).getDeclaration());
            }
        }
        return (Declaration[])declarations.toArray(new Declaration[declarations.size()]);
    }

    public Declaration[] getRequiredTupleMembers() {
        return requiredDeclarations;
    }

    public boolean isAllowed(Tuple tuple) throws ConditionException {
        try {
            return ((Boolean)ruleMethod.invokeMethod(tuple)).booleanValue();
        } catch (Exception e) {
            throw new ConditionException(e);
        }
    }

    /**
     * This only compares the ruleMethod, as to facilitate node sharing in the RETE network
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PojoCondition)) {
            return false;
        }
        final PojoCondition pojoCondition = (PojoCondition)other;
        if (!ruleMethod.equals(pojoCondition.ruleMethod)) {
            return false;
        }
        return true;
    }

    public int hashCode()
    {
        return ruleMethod.hashCode();
    }
    
    public String toString() {
        return ruleMethod.toString();
    }
}
