
import junit.framework.TestCase;
import org.apache.commons.jelly.*;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.StringReader;

/**
 * Reproduces an OutOfMemoryError when the a script is parsed and run multiple times.
 *
 * Condition is, that the root tag has at least one child.
 *
 * The complex network of ScriptBlock, TagScript, Tag, cannot be reclaimed from the
 * ThreadLocal.ThreadLocalMap
 *
 * Maybe we should use JellyContext.cacheTags everywhere a ThreadLocal is used - better not use it at all
 */
public class JellyOutOfMemoryTest extends TestCase
{
    public void testJelly() throws Exception
    {
        int i = 0;
        while (System.getProperty("JellyOutOfMemoryTest.finalizeOk") == null)
        {
            System.err.println( "run: " + i );
            runJelly();
            i++;
        }
    }

    private void runJelly() throws Exception
    {

        JellyContext context = new JellyContext();
        XMLParser parser = new XMLParser();
        parser.setContext( context );

        context.registerTagLibrary( "http://drools.org/rules", new TestTagLibrary() );

        // parse the script
        StringReader strReader = new StringReader( "<parent xmlns='http://drools.org/rules'> <child/> </parent>" );
        Script script = parser.parse( strReader );

        // run the script
        XMLOutput output = XMLOutput.createXMLOutput( System.err, false );
        script.run( context, output );
    }

    private static class TestTagLibrary extends TagLibrary
    {

        public TestTagLibrary()
        {
            registerTag( "parent", ParentTag.class );
            registerTag( "child", ChildTag.class );
        }
    }

    public static class ParentTag extends TagSupport
    {

        private byte[] buffer = new byte[1024 * 1024];

        public ParentTag()
        {
            super( true );
            System.err.println( "new " + this );
        }

        public void doTag( XMLOutput xmlOutput ) throws MissingAttributeException, JellyTagException
        {
            System.err.println( "doTag: " + this );
            getBody().run( context, xmlOutput );
        }

        protected void finalize() throws Throwable
        {
            super.finalize();
            System.err.println( "finalize: " + this );
            System.setProperty("JellyOutOfMemoryTest.finalizeOk", "true");
        }
    }

    public static class ChildTag extends TagSupport
    {

        private byte[] buffer = new byte[1024 * 1024];

        public ChildTag()
        {
            super( true );
            System.err.println( "new " + this );
        }

        public void doTag( XMLOutput xmlOutput ) throws MissingAttributeException, JellyTagException
        {
            System.err.println( "doTag: " + this );
            getBody().run( context, xmlOutput );
        }

        protected void finalize() throws Throwable
        {
            super.finalize();
            System.err.println( "finalize: " + this );
            System.setProperty("JellyOutOfMemoryTest.finalizeOk", "true");
        }
    }
}
