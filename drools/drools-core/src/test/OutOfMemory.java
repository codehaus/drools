
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
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
public class OutOfMemory
{
    private static final String jelly = "<parent xmlns='http://drools.org/rules'> <child/> </parent>";


    public static void main(String[] args) throws Exception
    {
        // read number of runs from command line
        int maxrun = 1;
        if (args.length > 0)
        {
            maxrun = new Integer(args[0]).intValue();
        }

        for (int i = 0; i < maxrun; i++)
        {
            System.err.println("run: " + i);
            System.err.flush();
            OutOfMemory test = new OutOfMemory();
            test.runTest();
        }
    }

    private void runTest() throws Exception
    {

        JellyContext context = new JellyContext();
        XMLParser parser = new XMLParser();
        parser.setContext(context);

        context.registerTagLibrary("http://drools.org/rules", new TestTagLibrary());

        // parse the script
        StringReader strReader = new StringReader(jelly);
        Script script = parser.parse(strReader);

        // run the script
        XMLOutput output = XMLOutput.createXMLOutput(System.err, false);
        script.run(context, output);
    }

    private static class TestTagLibrary extends TagLibrary
    {

        public TestTagLibrary()
        {
            registerTag("parent", ParentTag.class);
            registerTag("child", ChildTag.class);
        }
    }

    public static class ParentTag extends TagSupport
    {

        private byte[] buffer = new byte[1024 * 1024];

        public ParentTag()
        {
            super(true);
            System.err.println("new " + this);
        }

        public void doTag(XMLOutput xmlOutput) throws MissingAttributeException, JellyTagException
        {
            System.err.println("doTag: " + this);
            getBody().run( context, xmlOutput );
        }

        protected void finalize() throws Throwable
        {
            super.finalize();
            System.err.println("finalize: " + this);
        }
    }

    public static class ChildTag extends TagSupport
    {

        private byte[] buffer = new byte[1024 * 1024];

        public ChildTag()
        {
            super(true);
            System.err.println("new " + this);
        }

        public void doTag(XMLOutput xmlOutput) throws MissingAttributeException, JellyTagException
        {
            System.err.println("doTag: " + this);
            getBody().run( context, xmlOutput );
        }

        protected void finalize() throws Throwable
        {
            super.finalize();
            System.err.println("finalize: " + this);
        }
    }
}
