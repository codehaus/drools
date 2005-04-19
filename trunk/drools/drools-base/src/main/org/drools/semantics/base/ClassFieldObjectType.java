package org.drools.semantics.base;

/*
 * $Id: ClassFieldObjectType.java,v 1.8 2005-04-19 22:34:31 mproctor Exp $
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.drools.spi.ObjectType;

/**
 * Java class semantics <code>ObjectType</code>.
 *
 * @author <a href="mailto:bob@werken.com">bob@werken.com </a>
 *
 * @version $Id: ClassFieldObjectType.java,v 1.8 2005-04-19 22:34:31 mproctor Exp $
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
    public ClassFieldObjectType( Class objectTypeClass, String fieldName, String fieldValue )
    {
        super( objectTypeClass );
        this.objectFieldName = fieldName;
        this.objectFieldValue = fieldValue;
    }

    /**
     * Return the Java object class.
     *
     * @return The Java object class.
     */
    public String getFieldName( )
    {
        return this.objectFieldName;
    }

    /**
     * Return the Java object class.
     *
     * @return The Java object class.
     */
    public String getFieldValue( )
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
    public boolean matches( Object object )
    {
        if ( !this.getType( ).isInstance( object ) )
        {
            return false;
        }

        if ( this.getterMethod == null )
        {
            String fieldName = this.getFieldName( );
            String fieldGetter = "get" + fieldName.toUpperCase( ).charAt( 0 )
                                 + fieldName.substring( 1 );
            try
            {
                this.getterMethod = getType( ).getMethod( fieldGetter, ( Class[] ) null );
            }
            catch ( NoSuchMethodException e )
            {
                // shouldn't happen, this is checked in factory
                return false;
            }
        }

        boolean result;
        try
        {
            result = this.getterMethod.invoke( object, ( Object[] )null ).equals( this.getFieldValue( ) );
        }
        catch ( IllegalAccessException e )
        {
            // shouldn't happen, this is checked in factory
            result = false;
        }
        catch ( InvocationTargetException e )
        {
            // shouldn't happen, this is checked in factory
            result = false;
        }

        return result;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Determine if another object is equal to this.
     *
     * @param object The object to test.
     *
     * @return <code>true</code> if <code>object</code> is equal to this,
     *         otherwise <code>false</code>.
     */
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null || getClass( ) != object.getClass( ) )
        {
            return false;
        }

        ClassFieldObjectType other = ( ClassFieldObjectType ) object;

        return getType( ).equals( other.getType( ) )
               && getFieldName().equals( other.getFieldName( ) )
               && getFieldValue().equals( other.getFieldValue( ) );
    }

    /**
     * Produce the hash of this object.
     *
     * @return The hash.
     */
    public int hashCode( )
    {
        return getType( ).hashCode( ) ^ getFieldName().hashCode( ) ^ getFieldValue( ).hashCode( );
    }

    public String toString( )
    {
        String fieldName = getFieldName( );
        return getType( ).getName( ) + ".get" + fieldName.toUpperCase( ).charAt( 0 )
               + fieldName.substring( 1 ) + "(\"" + getFieldValue( ) + "\")";
    }
}
