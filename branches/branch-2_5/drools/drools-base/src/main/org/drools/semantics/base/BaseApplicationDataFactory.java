package org.drools.semantics.base;

/*
 * $Id: BaseApplicationDataFactory.java,v 1.3 2005-07-30 16:37:40 brownj Exp $
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

import org.drools.rule.ApplicationData;
import org.drools.rule.RuleSet;
import org.drools.smf.ApplicationDataFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.spi.Importer;
import org.drools.spi.RuleBaseContext;

public class BaseApplicationDataFactory
    implements
    ApplicationDataFactory
{
    public ApplicationData newApplicationData(RuleSet ruleSet,
                                              RuleBaseContext context,
                                              Configuration config ) throws FactoryException

    {
        String className = config.getText( ).trim( );
        
        Class clazz = null;
        try
        {
            ClassLoader cl = (ClassLoader) context.get( "smf-classLoader" );
            if ( cl == null )
            {
                cl = Thread.currentThread( ).getContextClassLoader( );
                context.put( "smf-classLoader",
                             cl );
            }

            if ( cl == null )
            {
                cl = getClass( ).getClassLoader( );
                context.put( "smf-classLoader",
                             cl );
            }

            Importer importer = ruleSet.getImporter( );
            clazz = importer.importClass( cl,
                                          className );
        }
        catch ( ClassNotFoundException e )
        {
            throw new FactoryException( e.getMessage( ) );
        }
        catch ( Error e )
        {
            throw new FactoryException( e.getMessage( ) );
        }

        return new ApplicationData( ruleSet,
                                    config.getAttribute( "identifier" ),
                                    clazz );
    }

}
