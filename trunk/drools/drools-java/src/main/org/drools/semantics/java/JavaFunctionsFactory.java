package org.drools.semantics.java;

/*
 * $Id: JavaFunctionsFactory.java,v 1.8 2005-05-08 19:54:48 mproctor Exp $
 *
 * Copyright 2004-2005 (C) The Werken Company. All Rights Reserved.
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

import java.io.IOException;

import org.codehaus.janino.Java.CompileException;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.FunctionsFactory;
import org.drools.spi.Functions;
import org.drools.spi.RuleBaseContext;

public class JavaFunctionsFactory
    implements
    FunctionsFactory
{

    public Functions newFunctions(RuleSet ruleSet,
                                  RuleBaseContext context,
                                  Configuration config) throws FactoryException
    {
        try
        {
            Integer id = (Integer) context.get( "java-functions-id" );
            if ( id == null )
            {
                id = new Integer( 0 );
            }
            context.put( "java-functions-id",
                         new Integer( id.intValue( ) + 1 ) );  
            
            return new JavaFunctions( ruleSet,
                                      id.intValue( ),
                                      config.getText( ) );
        }
        catch ( Exception e )
        {
            throw new FactoryException( e );
        }

    }
}
