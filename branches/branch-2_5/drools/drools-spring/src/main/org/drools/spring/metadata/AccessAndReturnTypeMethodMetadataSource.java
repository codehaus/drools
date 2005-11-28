package org.drools.spring.metadata;

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
import java.lang.reflect.Modifier;

/**
 * A method will be considered a condition if:
 *  - method is public
 *  - method returns boolean
 *  - method has at least a one parameter
 *
 * A method will be considered a consequence if:
 *  - method is public
 *  - method returns void
 */
public class AccessAndReturnTypeMethodMetadataSource implements MethodMetadataSource, StoppingClassCapable {

    private static final MethodMetadata METHOD_CONDITION_METADATA = new MethodMetadata(MethodMetadata.METHOD_CONDITION);
    private static final MethodMetadata METHOD_CONSEQUENCE_METADATA = new MethodMetadata(MethodMetadata.METHOD_CONSEQUENCE);
    private static final MethodMetadata OBJECT_CONDITION_METADATA = new MethodMetadata(MethodMetadata.OBJECT_CONDITION);

    private Class stoppingClass = Object.class;
    
    public void setStoppingClass(Class stoppingClass) {
        this.stoppingClass = stoppingClass;
    }
    
    public MethodMetadata getMethodMetadata(Method method) {
        if (method.getDeclaringClass().isAssignableFrom(stoppingClass)) {
            return null;
        }
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        if (isReturnTypeBoolean(method) && hasParameters(method)) {
            return METHOD_CONDITION_METADATA;
        } else if (!isReturnTypePrimitive(method) && !hasParameters(method)) {
            return OBJECT_CONDITION_METADATA;
        } else if (isReturnTypeVoid(method)) {
            return METHOD_CONSEQUENCE_METADATA;
        } else {
            return null;
        }
    }

    private boolean isReturnTypePrimitive(Method method) {
        return method.getReturnType().isPrimitive();
    }
    
    private boolean isReturnTypeBoolean(Method method) {
        return boolean.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnTypeVoid(Method method) {
        return void.class.isAssignableFrom(method.getReturnType());
    }

    private boolean hasParameters(Method method) {
        return method.getParameterTypes().length > 0;
    }
}

