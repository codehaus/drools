package org.drools.reteoo;

/*
 $Id: ReflectiveVisitor.java,v 1.1 2004-08-07 16:23:31 mproctor Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a trademark of
    The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://werken.com/)

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import java.lang.reflect.Method;

import org.drools.Visitor;

public abstract class ReflectiveVisitor implements Visitor
{
    public void visit(Object object) {
        try
        {
            if (object != null)
            {
                 Method method = getMethod(object.getClass());
                 method.invoke(this, new Object[] {object});
            }
            else
            {
                Method method = this.getClass().getMethod("visitNull", null);
                method.invoke(this, null);
            }
        }
        catch (Exception e)
        {
             e.printStackTrace();
        }
    }

    protected Method getMethod(Class clazz)
    {
        Class newClazz = clazz;
        Method method = null;

        // Try the superclasses
        while (method == null && newClazz != Object.class)
        {
            String methodName = newClazz.getName();
            methodName = "visit" + methodName.substring(methodName.lastIndexOf('.') + 1);
            try
            {
               method = getClass().getMethod(methodName, new Class[] {newClazz});
            }
            catch (NoSuchMethodException e)
            {
               newClazz = newClazz.getSuperclass();
            }
        }

        // Try the interfaces.
        if (newClazz == Object.class)
        {
            Class[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; i++)
            {
                String methodName = interfaces[i].getName();
                methodName = "visit" + methodName.substring(methodName.lastIndexOf('.') + 1);
                try
                {
                    method = getClass().getMethod(methodName, new Class[] {interfaces[i]});
                }
                catch (NoSuchMethodException e)
                {
                    //swallow
                }
            }
        }
        if (method == null)
        {
            try
            {
                method = this.getClass().getMethod("visitObject", new Class[] {Object.class});
            }
            catch (Exception e)
            {
                // Can't happen
            }
        }
        return method;
    }
}