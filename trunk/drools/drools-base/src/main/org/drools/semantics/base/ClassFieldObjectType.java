package org.drools.semantics.base;

/*
 * $Id: ClassFieldObjectType.java,v 1.3 2004-11-16 14:35:32 simon Exp $
 *
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
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

import org.drools.spi.ObjectType;

import java.lang.reflect.Method;

/**
 * Java class semantics <code>ObjectType</code>.
 *
 * @author <a href="mailto:bob@werken.com">bob@werken.com </a>
 *
 * @version $Id: ClassFieldObjectType.java,v 1.3 2004-11-16 14:35:32 simon Exp $
 */
public class ClassFieldObjectType extends ClassObjectType implements ObjectType
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------
    /** Java object field. */
    private String objectFieldName;

    private String objectFieldValue;

    /** Java getter method. */
    private Method getterMethod;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param objectTypeClass Java object class.
     */
    public ClassFieldObjectType(Class objectTypeClass, String fieldName, String fieldValue)
    {
        super(objectTypeClass);
        this.objectFieldName = fieldName;
        this.objectFieldValue = fieldValue;
    }

    /**
     * Return the Java object class.
     *
     * @return The Java object class.
     */
    public String getFieldName()
    {
        return this.objectFieldName;
    }

    /**
     * Return the Java object class.
     *
     * @return The Java object class.
     */
    public String getFieldValue()
    {
        return this.objectFieldValue;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.spi.ObjectType
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Determine if the passed <code>Object</code> belongs to the object type
     * defined by this <code>objectType</code> instance.
     *
     * @param object The <code>Object</code> to test.
     *
     * @return <code>true</code> if the <code>Object</code> matches this
     *         object type, else <code>false</code>.
     */
    public boolean matches(Object object)
    {
        if (!getType( ).isInstance( object )) return false;

        if (this.getterMethod == null )
        {
            String fieldName = getFieldName( );
            String fieldGetter = "get" + fieldName.toUpperCase().charAt(0)
                                 + fieldName.substring(1);
            try
            {
                getterMethod = getType( ).getMethod(fieldGetter, null);
            }
            catch (Exception e)
            {
                // shouldn't happen, this is checked in factory
            }
        }
        try
        {
            return getterMethod.invoke(object, null).equals( getFieldValue() );
        }
        catch (Exception e)
        {
               // shouldn't happen, this is checked in factory
        }

        return false;

    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Determine if another object is equal to this.
     *
     * @param thatObj The object to test.
     *
     * @return <code>true</code> if <code>thatObj</code> is equal to this,
     *         otherwise <code>false</code>.
     */
    public boolean equals(Object thatObj)
    {
        if (this == thatObj)
        {
            return true;
        }
        ClassFieldObjectType thatClassField = ( ClassFieldObjectType ) thatObj;

        if ( thatObj instanceof ClassFieldObjectType )
        {
            return getType( ).equals( thatClassField.getType( ) )
                   && getFieldName( ).equals( thatClassField.getFieldName( ) )
                   && getFieldValue( ).equals( thatClassField.getFieldValue( ) );
        }

        return false;
    }

    /**
     * Produce the hash of this object.
     *
     * @return The hash.
     */
    public int hashCode()
    {
        return getType( ).hashCode( ) ^ getFieldName().hashCode( ) ^ getFieldValue().hashCode();
    }

    public String toString()
    {
        String fieldName = getFieldName( );
        return getType( ).getName( ) + ".get" + fieldName.toUpperCase().charAt(0)
               + fieldName.substring(1) + "(\"" + getFieldValue( ) + "\")";
    }
}