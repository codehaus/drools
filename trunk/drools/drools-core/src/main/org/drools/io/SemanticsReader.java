package org.drools.io;

/*
 $Id: SemanticsReader.java,v 1.2 2003-11-19 21:31:10 bob Exp $

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

import org.drools.smf.SemanticModule;
import org.drools.smf.SimpleSemanticModule;
import org.drools.spi.ObjectType;
import org.drools.spi.Condition;
import org.drools.spi.Extractor;
import org.drools.spi.Consequence;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Enumeration;

public class SemanticsReader
{
    public SemanticsReader()
    {

    }

    public SemanticModule read(URL url)
        throws Exception
    {
        InputStream in = url.openStream();

        try
        {
            return read( in );
        }
        finally
        {
            in.close();
        }
    }

    public SemanticModule read(InputStream in)
        throws Exception
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        if ( cl == null )
        {
            cl = SemanticsReader.class.getClassLoader();
        }

        Properties props = new Properties();

        props.load( in );

        String uri = props.getProperty( "module.uri" );

        if ( uri == null
             ||
             uri.trim().equals( "" ) )
        {
            throw new Exception( "module.uri must be specified" );
        }

        SimpleSemanticModule module = new SimpleSemanticModule( uri.trim() );

        for ( Enumeration propNames = props.propertyNames();
              propNames.hasMoreElements(); )
        {
            String componentName = (String) propNames.nextElement();

            if ( componentName.equals( "module.uri" ) )
            {
                continue;
            }

            String className     = props.getProperty( componentName );

            Class componentClass = cl.loadClass( className );

            if ( ObjectType.class.isAssignableFrom( componentClass ) )
            {
                module.addObjectType( componentName,
                                      componentClass );
            }
            else if ( Condition.class.isAssignableFrom( componentClass ) )
            {
                module.addCondition( componentName,
                                     componentClass );
            }
            else if ( Extractor.class.isAssignableFrom( componentClass ) )
            {
                module.addExtractor( componentName,
                                     componentClass );
            }
            else if ( Consequence.class.isAssignableFrom( componentClass ) )
            {
                module.addConsequence( componentName,
                                       componentClass );
            }
        }

        return module;
    }
}
