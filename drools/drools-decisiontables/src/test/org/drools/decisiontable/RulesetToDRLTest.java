package org.drools.decisiontable;


/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
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



import java.io.StringReader;

import junit.framework.TestCase;

import org.drools.RuleBase;
import org.drools.decisiontable.model.Condition;
import org.drools.decisiontable.model.Consequence;
import org.drools.decisiontable.model.Parameter;
import org.drools.decisiontable.model.Rule;
import org.drools.decisiontable.model.Ruleset;
import org.drools.io.RuleBaseLoader;
import org.drools.io.RuleSetLoader;
import org.xml.sax.SAXParseException;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Basic test to cover using ruleset to DRL conversion.
 */
public class RulesetToDRLTest extends TestCase
{

    public void testBuildRuleBaseBasicParsing() throws Exception
    {
        String ruleText = getTestRuleSet( ).toXML( );                
                
        RuleSetLoader ruleSetLoader = new RuleSetLoader();           
        ruleSetLoader.addFromReader( new StringReader( ruleText ) );            
        
        RuleBaseLoader ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
        RuleBase ruleBase = ruleBaseLoader.buildRuleBase();
        assertNotNull( ruleBase );
    }

    public static Ruleset getTestRuleSet()
    {
        Ruleset ruleSet = new Ruleset( "name" ); //$NON-NLS-1$
        Rule rule = new Rule( "rule", new Integer(1) ); //$NON-NLS-1$
        rule.setComment( "a comment" ); //$NON-NLS-1$

        Condition cond = new Condition( );
        cond.setSnippet( "System.currentTimeMillis() > 42 /* comment here */" );
        rule.addCondition( cond );

        Consequence cons = new Consequence( );
        cons.setSnippet( "System.out.println(\"yeah\");" );
        rule.addConsequence( cons );

        Parameter param = new Parameter( );
        param.setClassName( "java.lang.String" ); //$NON-NLS-1$
        param.setIdentifier( "s" );
        param.setComment( "param comment" ); //$NON-NLS-1$

        rule.addParameter( param );
        ruleSet.addRule( rule );

        return ruleSet;
    }

    public void testBuildRuleBaseBadFormedXML() throws Exception
    {
        Rule rule = new Rule( "rule",
                              new Integer(1) );
        rule.setComment( "a comment" );
        Condition cond = new Condition( );

        cond.setSnippet( "<badformedxml>" );
        rule.addCondition( cond );

        Ruleset ruleset = new Ruleset( "xyz" );
        ruleset.addRule( rule );
        try
        {           
            RuleSetLoader ruleSetLoader = new RuleSetLoader();           
            ruleSetLoader.addFromReader(new StringReader( ruleset.toXML( ) ) );            
            
            RuleBaseLoader ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
            RuleBase ruleBase = ruleBaseLoader.buildRuleBase();            
            
            fail( );
        }
        catch ( SAXParseException e )
        {
            System.out.println( e.getMessage( ) );
        }
    }

    public void testBuildRuleBaseBadSnippet()
    {
        Rule rule = new Rule( "rule", new Integer(1) ); //$NON-NLS-1$
        rule.setComment( "a comment" ); //$NON-NLS-1$
        Condition cond = new Condition( );

        // TODO: find out why run out of memory when use the // comment notation
        cond.setSnippet( "this.is.not.java /*this.is.not.java*/" ); //$NON-NLS-1$
        rule.addCondition( cond );

        Ruleset ruleset = new Ruleset( "xyz" ); //$NON-NLS-1$
        ruleset.addRule( rule );

        try
        {
            RuleSetLoader ruleSetLoader = new RuleSetLoader();           
            ruleSetLoader.addFromReader(new StringReader( ruleset.toXML( ) ) );            
            
            RuleBaseLoader ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
            RuleBase ruleBase = ruleBaseLoader.buildRuleBase();      
            fail( );
        }
        catch ( Exception e )
        {
            System.out.println( e.toString( ) );
        }
    }

}

