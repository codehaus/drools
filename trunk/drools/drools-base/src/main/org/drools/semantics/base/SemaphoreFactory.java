package org.drools.semantics.base;

/*
 * $Id: SemaphoreFactory.java,v 1.3 2005-04-19 22:34:31 mproctor Exp $
 *
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
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

import java.util.Set;

import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.ObjectType;
import org.drools.spi.RuleBaseContext;

public class SemaphoreFactory
    implements
    ObjectTypeFactory
{
    private static final SemaphoreFactory INSTANCE = new SemaphoreFactory( );

    public static SemaphoreFactory getInstance()
    {
        return INSTANCE;
    }

    public ObjectType newObjectType(RuleBaseContext context,
                                    Configuration config,
                                    Set imports) throws FactoryException
    {
        String className = "org.drools.semantics.base." + config.getAttribute( "type" ) + "Semaphore";
        String fieldName = "identifier";
        String fieldValue = config.getAttribute( "identifier" );

        if ( className == null || className.trim( ).equals( "" ) )
        {
            throw new FactoryException( "no Semaphore type specified" );
        }

        if ( fieldValue == null || fieldValue.trim( ).equals( "" ) )
        {
            throw new FactoryException( "no Semaphore identifier specified" );
        }

        try
        {
            ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );

            Class clazz = null;
            /* first try loading className */
            try
            {
                clazz = cl.loadClass( className );
            }
            catch ( ClassNotFoundException e )
            {
                // Semaphore type does not exist
                throw new FactoryException( "Unable create Semaphore for type [" + config.getAttribute( "type" ) + "]" );
            }

            // make sure field getter exists
            clazz.getMethod( "get" + fieldName.toUpperCase( ).charAt( 0 ) + fieldName.substring( 1 ),
                             ( Class[] ) null );

            return new ClassFieldObjectType( clazz,
                                             fieldName,
                                             fieldValue );
        }
        catch ( SecurityException e )
        {
            throw new FactoryException( "Field " + fieldName + " is not accessible for Class " + className );
        }
        catch ( NoSuchMethodException e )
        {
            throw new FactoryException( "Field " + fieldName + " does not exist for Class " + className );
        }
    }

}
