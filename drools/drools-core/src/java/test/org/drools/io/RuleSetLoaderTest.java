package org.drools.io;

import org.drools.rule.Declaration;
import org.drools.rule.Extraction;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;

import org.drools.semantics.java.ClassObjectType;
import org.drools.semantics.java.ExprExtractor;
import org.drools.semantics.java.ExprCondition;
import org.drools.semantics.java.BlockConsequence;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class RuleSetLoaderTest extends TestCase
{
    private RuleSetLoader loader;

    public RuleSetLoaderTest(String name)
    {
        super( name );
    }

    public void setUp() 
    {
        this.loader = new RuleSetLoader();
    }

    public void tearDown()
    {
        this.loader = new RuleSetLoader();
    }

    public void testLoad_Invalid() throws Exception
    {
        try
        {
            this.loader.load( "file:///i/am/on/the/road/to/nowhere.drl" );
            fail( "Should have thrown IOException" );
        }
        catch (IOException e)
        {
            // expected and correct
        }
    }

    public void testLoad_ValidEmpty() throws Exception
    {
        List ruleSets = this.loader.load( getClass().getResource( "RuleSetLoaderTest-1.drl" ) );

        assertNotNull( ruleSets );

        assertTrue( ruleSets.isEmpty() );
    }

    public void testLoad_ValidNonEmpty() throws Exception
    {
        List ruleSets = this.loader.load( getClass().getResource( "RuleSetLoaderTest-2.drl" ) );

        assertNotNull( ruleSets );

        assertEquals( 3,
                      ruleSets.size() );

        RuleSet ruleSet    = null;
        List    rules      = null;
        Set     paramDecls = null;
        Rule    rule       = null;

        Declaration decl = null;
        ClassObjectType type = null;

        Extraction extraction = null;
        ExprExtractor extractor = null;

        ExprCondition condition = null;

        BlockConsequence consequence = null;

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
        //     ruleset.1
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

        ruleSet = (RuleSet) ruleSets.get( 0 );

        assertEquals( "ruleset.1",
                      ruleSet.getName() );

        rules = ruleSet.getRules();

        assertNotNull( rules );

        assertEquals( 1,
                      rules.size() );

        rule = (Rule) ruleSet.getRules().get( 0 );

        assertNotNull( rule );

        assertEquals( "rule.1.1",
                      rule.getName() );

        decl = rule.getDeclaration( "str" );

        assertNotNull( decl );

        assertSame( decl,
                    rule.getParameterDeclaration( "str" ) );

        type = (ClassObjectType) decl.getObjectType();

        assertNotNull( type );

        assertTrue( type.getType() == String.class );

        decl = rule.getDeclaration( "maxLen" );

        assertNotNull( decl );

        assertSame( decl,
                    rule.getParameterDeclaration( "maxLen" ) );

        type = (ClassObjectType) decl.getObjectType();

        assertNotNull( type );

        assertTrue( type.getType() == Integer.class );

        decl = rule.getDeclaration( "strLen" );

        assertNotNull( decl );

        assertNull( rule.getParameterDeclaration( "strLen" ) );

        type = (ClassObjectType) decl.getObjectType();

        assertNotNull( type );

        assertTrue( type.getType() == Integer.class );

        assertEquals( 1,
                      rule.getExtractions().size() );

        extraction = (Extraction) rule.getExtractions().iterator().next();

        assertNotNull( extraction );

        assertEquals( rule.getDeclaration( "strLen" ),
                      extraction.getTargetDeclaration() );

        extractor = (ExprExtractor) extraction.getExtractor();

        assertNotNull( extractor );

        assertEquals( "str.length()",
                      extractor.getExpression() );

        Set conditionExprs = new HashSet();
        conditionExprs.add( "strLen > 5" );
        conditionExprs.add( "strLen < maxLen" );

        assertEquals( 2,
                      rule.getConditions().size() );

        Iterator conditionIter = rule.getConditions().iterator();

        condition = (ExprCondition) conditionIter.next();

        assertTrue( conditionExprs.remove( condition.getExpression() ) );

        condition = (ExprCondition) conditionIter.next();

        assertTrue( conditionExprs.remove( condition.getExpression() ) );

        assertTrue( conditionExprs.isEmpty() );

        consequence = (BlockConsequence) rule.getConsequence();

        assertNotNull( consequence );

        assertEquals( "System.err.println( \"str: \" + str );\n"
                      + "          System.err.println( \"strLen: \" + strLen );\n"
                      + "          System.err.println( \"maxLen: \" + maxLen );",
                      consequence.getText() );
        

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
        //     ruleset.2
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

        ruleSet = (RuleSet) ruleSets.get( 1 );

        assertEquals( "ruleset.2",
                      ruleSet.getName() );

        assertTrue( ruleSet.getRules().isEmpty() );
    }
}
