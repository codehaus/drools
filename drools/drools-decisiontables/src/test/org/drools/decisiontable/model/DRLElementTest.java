/*
 * Created on 25/05/2005
 */
package org.drools.decisiontable.model;

import org.drools.decisiontable.model.DRLElement;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * 
 */
public class DRLElementTest extends TestCase
{

    public void testEscaping()
    {
        assertNotNull( "this test is not needed, as using CDATA now" );
        String snippet = "user.getAge() >=  20 && user.getAge() <= 31";
        String result = DRLElement.escapeSnippet( snippet );
        assertEquals( "user.getAge() &gt;=  20 &amp;&amp; user.getAge() &lt;= 31",
                      result );

        snippet = "nothing";
        assertEquals( snippet,
                      DRLElement.escapeSnippet( snippet ) );
    }

}
