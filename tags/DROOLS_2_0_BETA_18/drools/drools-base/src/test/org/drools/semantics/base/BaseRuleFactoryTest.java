package org.drools.semantics.base;

/*
 * $Id: BaseRuleFactoryTest.java,v 1.2 2004-09-17 00:24:38 mproctor Exp $
 * 
 * Copyright 2004-2004 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a trademark of The Werken Company.
 * 
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
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

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.MissingAttributeException;

public class BaseRuleFactoryTest extends TestCase
{
    public BaseRuleFactoryTest(String arg0)
    {
        super( arg0 );
    }

    public final void testBaseRuleFactory()
    {
        BaseRuleFactory baseRuleFactory = new BaseRuleFactory( );
        assertNotNull( baseRuleFactory );
    }

    public final void testNewRule()
    {
        BaseRuleFactory baseRuleFactory = new BaseRuleFactory( );
        assertNotNull( baseRuleFactory );

        String ruleName = "TEST_RULE_NAME";
        validRuleNameTest( "valid name", baseRuleFactory, ruleName, ruleName );

        String whitespace = " \n \t \r \n\r \r\n ";
        validRuleNameTest( "valid name with whitespace", baseRuleFactory,
                           whitespace + ruleName + whitespace, ruleName );

        invalidRuleNameTest( "null name", baseRuleFactory, null );
        invalidRuleNameTest( "empty string name", baseRuleFactory, "" );
        invalidRuleNameTest( "blank string name", baseRuleFactory, whitespace );
    }

    private void validRuleNameTest(String testDesc,
                                   BaseRuleFactory baseRuleFactory,
                                   String ruleNameIn,
                                   String ruleNameOut)
    {
        Configuration config = new MockRuleConfiguration( ruleNameIn );
        Rule rule = null;
        try
        {
            rule = baseRuleFactory.newRule( config );
        }
        catch ( FactoryException e )
        {
            fail( testDesc );
        }
        assertNotNull( testDesc, rule );
        assertEquals( testDesc, ruleNameOut, rule.getName( ) );
    }

    private void invalidRuleNameTest(String testDesc,
                                     BaseRuleFactory baseRuleFactory,
                                     String ruleName)
    {
        Configuration config = new MockRuleConfiguration( ruleName );
        Rule rule = null;
        try
        {
            rule = baseRuleFactory.newRule( config );
            fail( testDesc );
        }
        catch ( FactoryException e )
        {
            if ( !( e instanceof MissingAttributeException ) )
            {
                fail( testDesc );
            }
        }
        assertNull( testDesc, rule );
    }
}

