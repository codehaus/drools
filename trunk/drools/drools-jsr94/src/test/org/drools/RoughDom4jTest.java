
package org.drools;

import org.drools.AssertionException;
import org.drools.spi.Tuple;
import org.drools.spi.Rule;
import org.drools.spi.Action;
import org.drools.spi.Declaration;
import org.drools.spi.FactExtractor;
import org.drools.spi.AssignmentCondition;
import org.drools.spi.DeclarationAlreadyCompleteException;
import org.drools.reteoo.ReteConstructionException;
import org.drools.semantic.java.JavaObjectType;
import org.drools.semantic.xml.Dom4jXmlObjectType;
import org.drools.semantic.xml.Dom4jXPathFactExtractor;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import org.saxpath.SAXPathException;
import org.jaxen.dom4j.XPath;

import java.io.File;
import java.net.MalformedURLException;

public class RoughDom4jTest extends TestCase
{
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
    }

    public void tearDown()
    {
        this.doc1 = null;
        this.doc2 = null;
    }

    public void testNothing()
    {
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
                                              "name" );
            
            FactExtractor aExtract = new Dom4jXPathFactExtractor( doc1,
                                                                  new XPath( "/a/@id" ) );

            FactExtractor bExtract = new Dom4jXPathFactExtractor( doc2,
                                                                  new XPath( "/b/@id" ) );
            
            rule.addAssignmentCondition( new AssignmentCondition( id,
                                                                  aExtract ) );
            
            rule.addAssignmentCondition( new AssignmentCondition( id,
                                                                  bExtract ) );
            
            rule.setAction( new Action() {

                    public void invoke(Tuple tuple,
                                       WorkingMemory workingMemory)
                    {
                        System.err.println( "\n\n\nHI MOM\n\n\n" );
                    }
                });

            ruleBase.addRule( rule );

            WorkingMemory memory = ruleBase.createWorkingMemory();

            memory.assertObject( this.doc1 );
            memory.assertObject( this.doc2 );
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
