package org.drools.semantic.java;

/*
 $Id: JavaObjectType.java,v 1.6 2002-08-17 05:49:22 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).
 
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

import org.drools.spi.ObjectType;

/** <code>ObjectType</code> implementing Java <code>Class</code> semantics
 *  for object type delineation.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class JavaObjectType implements ObjectType
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The java class of the type. */
    private Class objectClass;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param objectClass The Java class of this type.
     */
    public JavaObjectType(Class objectClass)
    {
        this.objectClass = objectClass;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the Java class of this type.
     *
     *  @return The java class of this type.
     */
    public Class getObjectClass()
    {
        return this.objectClass;
    }

    /** Determine if this <code>JavaObjectType</code> is
     *  semantically equal to another.
     *
     *  @param thatObj The object to compare.
     *
     *  @return <code>true</code> if <code>thatObj</code> is
     *          semantically equal to this <code>JavaObjectType</code>.
     */
    public boolean equals(Object thatObj)
    {
        if ( ! ( thatObj instanceof JavaObjectType ) )
        {
            return false;
        }

        JavaObjectType that = (JavaObjectType) thatObj;

        return this.objectClass.equals( that.objectClass );
    }

    /** Determine if the passed <code>Object</code>
     *  belongs to the object type defined by this
     *  <code>objectType</code> instance.
     *
     *  @param object The <code>Object</code> to test.
     *
     *  @return <code>true</code> if the <code>Object</code>
     *          matches this object type, else <code>false</code>.
     */
    public boolean matches(Object object)
    {
        return getObjectClass().isAssignableFrom( object.getClass() );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Produce a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return "[JavaObjectType: class=" + getObjectClass() +"]";
    }
}
