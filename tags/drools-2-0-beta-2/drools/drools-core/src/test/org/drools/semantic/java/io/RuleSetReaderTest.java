
package org.drools.semantic.java.io;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.spi.RuleSet;
import org.drools.reteoo.ReteConstructionException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import junit.framework.TestCase;

import java.io.File;
import java.net.MalformedURLException;

public class RuleSetReaderTest extends TestCase
{
    public static Document RETURN_doc1;
    public static Document RETURN_doc2;

    public RuleSetReaderTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        RETURN_doc1 = null;
        RETURN_doc2 = null;
    }

    public void tearDown()
    {
        RETURN_doc1 = null;
        RETURN_doc2 = null;
    }

    public void testHighLevel()
    {
        RETURN_doc1 = null;
        RETURN_doc2 = null;

        RuleSetReader reader = new RuleSetReader();

        try
        {
            RuleSet ruleSet = reader.read( new File( "test_data/ruleset.xml" ) );
            
            assertNotNull( ruleSet );

            RuleBase ruleBase = new RuleBase();

            ruleBase.addRuleSet( ruleSet );

            SAXReader xmlReader = new SAXReader();

            Document doc1 = xmlReader.read( new File( "test_data/doc1.xml" ) );
            Document doc2 = xmlReader.read( new File( "test_data/doc2.xml" ) );

            WorkingMemory memory = ruleBase.createWorkingMemory();

            memory.assertObject( doc1 );
            memory.assertObject( doc2 );

            assertSame( RETURN_doc1,
                        doc1 );

            assertSame( RETURN_doc2,
                        doc2 );
        }
        catch (MalformedURLException e)
        {
            fail( e.toString() );
        }
        catch (RuleSetReaderException e)
        {
            fail( e.toString() );
        }
        catch (DocumentException e)
        {
            fail( e.toString() );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
        catch (ReteConstructionException e)
        {
            fail( e.toString() );
        }
    }
}