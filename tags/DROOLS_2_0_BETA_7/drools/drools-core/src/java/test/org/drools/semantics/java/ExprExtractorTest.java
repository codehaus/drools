package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.spi.MockTuple;
import org.drools.spi.ExtractionException;

import bsh.EvalError;

import junit.framework.TestCase;

public class ExprExtractorTest extends TestCase
{
    public ExprExtractorTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testExtractFact_NoText()
    {
        ExprExtractor extractor = new ExprExtractor();

        MockTuple tuple = new MockTuple();

        try
        {
            extractor.extractFact( tuple );
        }
        catch (ExtractionException e)
        {
            // expected and correct
            NullPointerException npe = (NullPointerException) e.getRootCause();
        }
    }

    public void testExtractFact_MissingObject()
    {
        ExprExtractor extractor = new ExprExtractor();
        extractor.setExpression( "a.length()" );

        MockTuple tuple = new MockTuple();

        try
        {
            extractor.extractFact( tuple );
        }
        catch (ExtractionException e)
        {
            // expected and correct
            EvalError ee = (EvalError) e.getRootCause();
        }
    }

    public void testExtractFact_Valid() throws Exception
    {
        ExprExtractor extractor = new ExprExtractor();
        extractor.setExpression( "a.length()" );

        MockTuple tuple = new MockTuple();

        String value = "I like cheese";

        tuple.put( new Declaration( new ClassObjectType( java.lang.String.class ),
                                    "a" ),
                   value );

        Object extractedValue = extractor.extractFact( tuple );

        assertEquals( new Integer(value.length()),
                      extractedValue );
    }
}

