package org.drools.semantics.java;

/*
 $Id: ClassObjectType.java,v 1.6 2002-08-21 05:46:13 bob Exp $

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

import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConversionException;

/** Java class semantics <code>ObjectType</code>.
 * 
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 *
 *  @version $Id: ClassObjectType.java,v 1.6 2002-08-21 05:46:13 bob Exp $
 */
public class ClassObjectType implements ObjectType
{

    // ------------------------------------------------------------
    //     Class initialization
    // ------------------------------------------------------------
    
    /** Register conversions for jakarta-beanutils.
     */
    static
    {
        ConvertUtils.register(
            new Converter()
            {
                public Object convert(Class type, Object value)
                {
                    if ( value instanceof Class )
                    {
                        return (Class) value;
                    }
                    else if ( value instanceof String )
                    {
                        ClassLoader cl = Thread.currentThread().getContextClassLoader();
                        
                        if ( cl == null )
                        {
                            cl = getClass().getClassLoader();
                        }
                        try
                        {
                            return cl.loadClass( (String) value );
                        }
                        catch (Exception e)
                        {
                            throw new ConversionException( "Cannot convert " + value + " to a java.lang.Class",
                                                           e );
                        }
                    }
                    throw new ConversionException( "Cannot convert " + value + " to a java.lang.Class" );
                }
            },
            Class.class
            );
    }

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Java object class. */
    private Class objectTypeClass;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct, partially.
     *
     *  @see #setType
     */
    public ClassObjectType()
    {
        this.objectTypeClass = java.lang.Object.class;
    }

    /** Construct.
     *
     *  @param objectTypeClass Java object class.
     */
    public ClassObjectType(Class objectTypeClass)
    {
        this.objectTypeClass = objectTypeClass;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the Java object class.
     *
     *  @param objectTypeClass The Java object class.
     */
    public void setType(Class objectTypeClass)
    {
        this.objectTypeClass = objectTypeClass;
    }

    /** Return the Java object class.
     *
     *  @return The Java object class.
     */
    public Class getType()
    {
        return this.objectTypeClass;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.spi.ObjectType
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

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
        return getType().isInstance( object );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Determine if another object is equal to this.
     *
     *  @param thatObj The object to test.
     *
     *  @return <code>true</code> if <code>thatObj</code> is equal
     *          to this, otherwise <code>false</code>.
     */
    public boolean equals(Object thatObj)
    {
        if ( thatObj == null
             ||
             ( ! ( thatObj instanceof ClassObjectType ) ) )
        {
            return false;
        }
             
        ClassObjectType that = (ClassObjectType) thatObj;

        return ( getType().equals( that.getType() ) );
    }

    /** Produce a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return "[ClassObjectType: type=" + getType().getName() + "]";
    }
}