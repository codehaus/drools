
package org.drools;

import org.drools.AssertionException;
import org.drools.reteoo.ReteConstructionException;
import org.drools.rule.Rule;
import org.drools.rule.Declaration;
import org.drools.rule.AssignmentCondition;
import org.drools.rule.DeclarationAlreadyCompleteException;
import org.drools.spi.Tuple;
import org.drools.spi.Action;
import org.drools.spi.FactExtractor;
import org.drools.semantic.java.JavaObjectType;
import org.drools.semantic.xml.Dom4jXmlObjectType;
import org.drools.semantic.xml.Dom4jXPathFactExtractor;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import org.saxpath.SAXPathException;
import org.jaxen.dom4j.Dom4jXPath;

import java.io.File;
import java.net.MalformedURLException;

public class RoughDom4jTest extends TestCase
{
    public static Document RETURN_doc1;
    public static Document RETURN_doc2;
    public static String RETURN_id;

    private Document doc1;
    private Document doc2;

    public RoughDom4jTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        SAXReader reader = new SAXReader();

        try
        {
            this.doc1 = reader.read( new File( "test_data/doc1.xml" ) );
            this.doc2 = reader.read( new File( "test_data/doc2.xml" ) );
        }
        catch (DocumentException e)
        {
            fail( e.toString() );
        }
        catch (MalformedURLException e)
        {
            fail( e.toString() );
        }

        RETURN_doc1 = null;
        RETURN_doc2 = null;
        RETURN_id = null;
    }

    public void tearDown()
    {
        this.doc1 = null;
        this.doc2 = null;

        RETURN_doc1 = null;
        RETURN_doc2 = null;
        RETURN_id = null;
    }

    public void testNothing()
    {
        RETURN_doc1 = null;
        RETURN_doc2 = null;
        RETURN_id = null;

        RuleBase ruleBase = new RuleBase();

        Rule rule = new Rule( "rough" );

        Declaration doc1 = new Declaration( new Dom4jXmlObjectType( "", "a" ),
                                            "doc1" );

        Declaration doc2 = new Declaration( new Dom4jXmlObjectType( "", "b" ),
                                            "doc2" );

        try
        {
            rule.addParameterDeclaration( doc1 );
            rule.addParameterDeclaration( doc2 );

            Declaration id = new Declaration( new JavaObjectType( String.class ),
                                              "id" );
            
            FactExtractor aExtract = new Dom4jXPathFactExtractor( doc1,
                                                                  new Dom4jXPath( "string(/a/@id)" ) );

            FactExtractor bExtract = new Dom4jXPathFactExtractor( doc2,
                                                                  new Dom4jXPath( "string(/b/@id)" ) );
            
            rule.addAssignmentCondition( new AssignmentCondition( id,
                                                                  aExtract ) );
            
            rule.addAssignmentCondition( new AssignmentCondition( id,
                                                                  bExtract ) );
            
            rule.setAction( new MyAction( doc1, doc2, id ) );

            ruleBase.addRule( rule );

            WorkingMemory memory = ruleBase.createWorkingMemory();

            memory.assertObject( this.doc1 );
            memory.assertObject( this.doc2 );

            assertSame( this.doc1,
                        RETURN_doc1 );

            assertSame( this.doc2,
                        RETURN_doc2 );
        }
        catch (DeclarationAlreadyCompleteException e)
        {
            fail( e.toString() );
        }
        catch (SAXPathException e)
        {
            fail( e.toString() );
        }
        catch (ReteConstructionException e)
        {
            fail( e.toString() );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }
}

class MyAction implements Action
{
    private Declaration doc1;
    private Declaration doc2;
    private Declaration id;

    public MyAction(Declaration doc1,
                    Declaration doc2,
                    Declaration id)
    {
        this.doc1 = doc1;
        this.doc2 = doc2;
        this.id   = id;
    }

    public void invoke(Tuple tuple,
                       WorkingMemory workingMemory)
    {
        RoughDom4jTest.RETURN_doc1 = (Document) tuple.get( this.doc1 );
        RoughDom4jTest.RETURN_doc2 = (Document) tuple.get( this.doc2 );
        RoughDom4jTest.RETURN_id   = (String)   tuple.get( this.id );
    }
}

