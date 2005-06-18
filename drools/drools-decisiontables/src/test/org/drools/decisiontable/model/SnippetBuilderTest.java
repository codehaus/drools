/*
 * Created on 12/05/2005
 */
package org.drools.decisiontable.model;

import org.drools.decisiontable.model.SnippetBuilder;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 */
public class SnippetBuilderTest extends TestCase
{

    public void testBuildSnippet()
    {
        String snippet = "something.param.getAnother().equals($param);";
        SnippetBuilder snip = new SnippetBuilder( snippet );
        String cellValue = "42";
        String result = snip.build( cellValue );
        assertNotNull( result );

        assertEquals( "something.param.getAnother().equals(42);",
                      result );
    }

    public void testBuildSnippetNoPlaceHolder()
    {
        String snippet = "something.getAnother().equals(blah);";
        SnippetBuilder snip = new SnippetBuilder( snippet );
        String cellValue = "this is ignored...";
        String result = snip.build( cellValue );

        assertEquals( snippet,
                      result );
    }

    public void testSingleParamMultipleTimes()
    {
        String snippet = "something.param.getAnother($param).equals($param);";
        SnippetBuilder snip = new SnippetBuilder( snippet );
        String cellValue = "42";
        String result = snip.build( cellValue );
        assertNotNull( result );

        assertEquals( "something.param.getAnother(42).equals(42);",
                      result );

    }

    public void testMultiPlaceHolder()
    {
        String snippet = "something.getAnother($1,$2).equals($2, '$2');";
        SnippetBuilder snip = new SnippetBuilder( snippet );
        String result = snip.build( "x, y" );
        assertEquals( "something.getAnother(x,y).equals(y, 'y');",
                      result );

    }

    public void testMultiPlaceHolderSingle()
    {
        String snippet = "something.getAnother($1).equals($1);";
        SnippetBuilder snip = new SnippetBuilder( snippet );
        String result = snip.build( "x" );
        assertEquals( "something.getAnother(x).equals(x);",
                      result );

    }

}
