package org.drools.io;

/*
 * $Id: RuleSetReader.java,v 1.31 2004-11-16 13:37:53 simon Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
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

import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.DefaultSemanticsRepository;
import org.drools.smf.NoSuchSemanticModuleException;
import org.drools.smf.SemanticModule;
import org.drools.smf.SemanticsRepository;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * <code>RuleSet</code> loader.
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 *
 * @version $Id: RuleSetReader.java,v 1.31 2004-11-16 13:37:53 simon Exp $
 */
public class RuleSetReader extends DefaultHandler
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Namespace URI for the general tags. */
    public static final String RULES_NAMESPACE_URI = "http://drools.org/rules";

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------
    /** SAX parser. */
    private SAXParser parser;

    /** Locator for errors. */
    private Locator locator;

    /** Repository of semantic modules. */
    private SemanticsRepository repo;
    //private Map repo;

    /** Stack of configurations. */
    private LinkedList configurationStack;

    /** Current configuration text. */
    private StringBuffer characters2;

    private Map localNameMap;

    private boolean lastWasEndElement;

    private LinkedList parents;

    private Object peer;

    private Object current;

    private RuleSet ruleSet;

    private MessageFormat message = new MessageFormat( "({0}: {1}, {2}): {3}" );

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /**
     * Construct.
     *
     * <p>
     * Uses the default JAXP SAX parser and the default classpath-based
     * <code>DefaultSemanticModule</code>.
     * </p>
     */
    public RuleSetReader()
    {
        //init
        this.configurationStack = new LinkedList( );
        this.parents = new LinkedList( );

        this.localNameMap = new HashMap( );
        localNameMap.put( "rule-set", new RuleSetHandler( this ) );
        localNameMap.put( "import", new ImportHandler( this ) );
        localNameMap.put( "rule", new RuleHandler( this ) );
        localNameMap.put( "parameter", new ParameterHandler( this ) );
        localNameMap.put( "declaration", new DeclarationHandler( this ) );
        localNameMap.put( "class", new ObjectTypeHandler( this ) );
        localNameMap.put( "class-field", new ObjectTypeHandler( this ) );
        localNameMap.put( "condition", new ConditionHandler( this ) );
        localNameMap.put( "duration", new DurationHandler( this ) );
        localNameMap.put( "extractor", new ExtractionHandler( this ) );
        localNameMap.put( "consequence", new ConsequenceHandler( this ) );

    }

    /**
     * Construct.
     *
     * <p>
     * Uses the default classpath-based <code>DefaultSemanticModule</code>.
     * </p>
     *
     * @param parser
     *            The SAX parser.
     */
    public RuleSetReader( SAXParser parser )
    {
        this( );
        this.parser = parser;
    }

    /**
     * Construct.
     *
     * @param repo
     *            The semantics repository.
     * @param parser
     *            The SAX parser.
     */
    public RuleSetReader( SemanticsRepository repo, SAXParser parser )
    {
        this( parser );
        this.repo = repo;
    }

    /**
     * Construct.
     *
     * @param repo
     *            The semantics repository.
     */
    public RuleSetReader( SemanticsRepository repo )
    {
        this( );
        this.repo = repo;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /**
     * Read a <code>RuleSet</code> from a <code>URL</code>.
     *
     * @param url
     *            The rule-set URL.
     *
     * @return The rule-set.
     *
     * @throws Exception
     *             If an error occurs during the parse.
     */
    public RuleSet read( URL url ) throws Exception
    {
        return read( new InputSource( url.toExternalForm( ) ) );
    }

    /**
     * Read a <code>RuleSet</code> from a <code>Reader</code>.
     *
     * @param reader
     *            The reader containing the rule-set.
     *
     * @return The rule-set.
     *
     * @throws Exception
     *             If an error occurs during the parse.
     */
    public RuleSet read( Reader reader ) throws Exception
    {
        return read( new InputSource( reader ) );
    }

    /**
     * Read a <code>RuleSet</code> from an <code>InputStream</code>.
     *
     * @param inputStream
     *            The input-stream containing the rule-set.
     *
     * @return The rule-set.
     *
     * @throws Exception
     *             If an error occurs during the parse.
     */
    public RuleSet read( InputStream inputStream ) throws Exception
    {
        return read( new InputSource( inputStream ) );
    }

    /**
     * Read a <code>RuleSet</code> from a URL.
     *
     * @param url
     *            The rule-set URL.
     *
     * @return The rule-set.
     *
     * @throws Exception
     *             If an error occurs during the parse.
     */
    public RuleSet read( String url ) throws Exception
    {
        return read( new InputSource( url ) );
    }

    /**
     * Read a <code>RuleSet</code> from an <code>InputSource</code>.
     *
     * @param in
     *            The rule-set input-source.
     *
     * @return The rule-set.
     *
     * @throws Exception
     *             If an error occurs during the parse.
     */
    public RuleSet read( InputSource in ) throws Exception
    {
        SAXParser parser = null;
        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );

        if ( cl == null )
        {
            cl = RuleSetReader.class.getClassLoader( );
        }

        if ( this.parser == null )
        {
            SAXParserFactory factory = SAXParserFactory.newInstance( );
            factory.setNamespaceAware( true );
            String isValidating = System
                    .getProperty( "drools.schema.validating" );
            if ( isValidating == null ) isValidating = "true";

            factory.setValidating( Boolean.valueOf( isValidating )
                    .booleanValue( ) );
            parser = factory.newSAXParser( );
            try
            {
                parser.setProperty( JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA );
            }
            catch ( SAXNotRecognizedException e )
            {
                System.err
                        .println( "Your SAX parser is not JAXP 1.2 compliant." );
            }
        }
        else
        {
            parser = this.parser;
        }

        if ( !parser.isNamespaceAware( ) )
        {
            throw new ParserConfigurationException(
                    "parser must be namespace-aware" );
        }

        if ( this.repo == null )
        {
            try
            {
                this.repo = DefaultSemanticsRepository.getInstance( );
            }
            catch ( Exception e )
            {
                throw new SAXException("Unable to reference a Semantics Repository");
            }
        }

        parser.parse( in, this );

        return this.ruleSet;
    }

    public SemanticsRepository getSemanticsRepository()
    {
        return this.repo;
    }

    void setRuleSet( RuleSet ruleSet )
    {
        this.ruleSet = ruleSet;
    }

    public RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    /**
     * @see org.xml.sax.ContentHandler
     */
    public void setLocator( Locator locator )
    {
        this.locator = locator;
    }

    /**
     * Get the <code>Locator</code>.
     *
     * @return The locator.
     */
    public Locator getLocator()
    {
        return this.locator;
    }

    public void startDocument()
    {
        this.ruleSet = null;
        this.current = null;
        this.peer = null;
        this.lastWasEndElement = false;
        this.parents.clear();
        this.characters2 = null;
        this.configurationStack.clear();
    }

    /**
     * @see org.xml.sax.ContentHandler
     */
    public void startElement( String uri,
                             String localName,
                             String qname,
                             Attributes attrs ) throws SAXException
    {
        //going down so no peer
        if ( this.lastWasEndElement == false )
        {
            this.peer = null;
        }
        Handler handler = getHandler( localName );

        validate( localName, handler );

        Object node = handler.start( uri, localName, attrs );

        if ( node != null )
        {
            parents.add( node );
            this.current = node;
        }
        this.lastWasEndElement = false;
    }

    /**
     * @see org.xml.sax.ContentHandler
     */
    public void endElement( String uri, String localName, String qname ) throws SAXException
    {
        Handler handler = getHandler( localName );

        this.current = getParent( handler.generateNodeFor( ) );

        Object node = handler.end( uri, localName );

        //next
        if ( (node != null) && (this.lastWasEndElement == false) )
        {
            this.peer = node;
        }
        //up or no children
        else if ( (this.lastWasEndElement == true)
                || (this.parents.getLast( )).getClass( )
                        .isInstance( this.current ) )
        {
            this.peer = this.parents.removeLast( );
        }

        this.lastWasEndElement = true;
    }

    private void validate( String localName, Handler handler ) throws SAXParseException
    {
        boolean validParent = false;
        boolean validPeer = false;
        boolean invalidNesting = false;

        Set validParents = handler.getValidParents( );
        Set validPeers = handler.getValidPeers( );
        boolean allowNesting = handler.allowNesting( );

        //get parent
        Object parent;
        if ( this.parents.size( ) != 0 )
        {
            parent = this.parents.getLast( );
        }
        else
        {
            parent = null;
        }

        // check valid parents
        // null parent means localname is rule-set
        // dont process if elements are the same
        // instead check for allowed nesting
        Class nodeClass = getHandler( localName ).generateNodeFor( );
        if ( !nodeClass.isInstance( parent ) )
        {
            Object allowedParent;
            Iterator it = validParents.iterator( );
            while ( (validParent == false) && it.hasNext( ) )
            {
                allowedParent = it.next( );
                if ( (parent == null) && (allowedParent == null) )
                {
                    validParent = true;
                }
                else if ( (allowedParent != null)
                        && ((Class) allowedParent).isInstance( parent ) )
                {
                    validParent = true;
                }
            }
            if ( !validParent )
            {
                throw new SAXParseException( "<" + localName
                        + "> has an invalid parent element", getLocator( ) );
            }
        }

        // check valid peers
        // null peer means localname is rule-set
        Object peer = this.peer;

        Object allowedPeer;
        Iterator it = validPeers.iterator( );
        while ( (validPeer == false) && it.hasNext( ) )
        {
            allowedPeer = it.next( );
            if ( (peer == null) && (allowedPeer == null) )
            {
                validPeer = true;
            }
            else if ( (allowedPeer != null)
                    && ((Class) allowedPeer).isInstance( peer ) )
            {
                validPeer = true;
            }
        }
        if ( !validPeer )
        {
            throw new SAXParseException( "<" + localName
                    + "> is after an invalid element", getLocator( ) );
        }

        if ( allowNesting == false )
        {
            it = this.parents.iterator( );
            while ( (invalidNesting == false) && it.hasNext( ) )
            {
                if ( nodeClass.isInstance( it.next( ) ) )
                {
                    invalidNesting = true;
                }
            }
        }
        if ( invalidNesting )
        {
            throw new SAXParseException( "<" + localName
                    + ">  may not be nested", getLocator( ) );
        }

    }

    /**
     * Start a configuration node.
     *
     * @param name
     *            Tag name.
     * @param attrs
     *            Tag attributes.
     *
     * @throws SAXException
     *             If an error occurs during parse.
     */
    protected void startConfiguration( String name, Attributes attrs ) throws SAXException
    {
        this.characters2 = new StringBuffer( );

        DefaultConfiguration config = new DefaultConfiguration( name );

        int numAttrs = attrs.getLength( );

        for ( int i = 0; i < numAttrs; ++i )
        {
            config.setAttribute( attrs.getLocalName( i ), attrs.getValue( i ) );
        }

        if ( this.configurationStack.isEmpty( ) )
        {
            this.configurationStack.addLast( config );
        }
        else
        {
            ((DefaultConfiguration) this.configurationStack.getLast( ))
                    .addChild( config );
        }
    }

    /**
     * @see org.xml.sax.ContentHandler
     */
    public void characters( char[] chars, int start, int len ) throws SAXException
    {
        if ( this.characters2 != null )
        {
            this.characters2.append( chars, start, len );
        }
    }

    /**
     * End a configuration node.
     *
     * @return The configuration.
     */
    protected Configuration endConfiguration()
    {
        DefaultConfiguration config = (DefaultConfiguration) this.configurationStack
                .removeLast( );

        config.setText( this.characters2.toString( ) );

        this.characters2 = null;

        return config;
    }

    SemanticModule lookupSemanticModule( String uri, String localName ) throws SAXParseException
    {
        SemanticModule module;
        try
        {
            module = this.repo.lookupSemanticModule( uri );
        }
        catch ( NoSuchSemanticModuleException e )
        {
            throw new SAXParseException( "no semantic module for namespace '"
                    + uri + "' (" + localName + ")", getLocator( ) );
        }
        return module;
    }

    private Handler getHandler( String localName ) throws SAXParseException
    {
        Handler handler = (Handler) this.localNameMap.get( localName );
        if ( handler == null )
        {
            throw new SAXParseException( "unable to handle element <"
                    + localName + ">", getLocator( ) );
        }
        return handler;
    }

    LinkedList getParents()
    {
        return this.parents;
    }

    Object getParent( Class parent )
    {
        ListIterator it = this.parents.listIterator( parents.size( ) );
        Object node = null;
        while ( it.hasPrevious( ) )
        {
            node = it.previous( );
            if ( parent.isInstance( node ) ) break;
        }
        return node;
    }

    Object getPeer()
    {
        return this.peer;
    }

    Object getCurrent()
    {
        return this.current;
    }

    public InputSource resolveEntity(String publicId,
                                     String systemId)
      throws SAXException
    {
        //Schema files must end with xsd
        if (!systemId.toLowerCase( ).endsWith("xsd"))
        {
            return null;
        }

        //try the actual location given by systemId
        try
        {
            URL url = new URL(systemId);
            return new InputSource(url.openStream());
        }
        catch (Exception e)
        {
        }

        //Try and get the index for the filename, else return null
        int index = systemId.lastIndexOf("/");
        if (index == -1)
        {
          index = systemId.lastIndexOf("\\");
        }
        if (index == -1)
        {
            return null;
        }

        String xsd = systemId.substring(index+1);
        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );

        if ( cl == null )
        {
            cl = RuleSetReader.class.getClassLoader( );
        }

        //Try looking in META-INF
        try
        {
            return new InputSource(cl.getResourceAsStream("META-INF/" + xsd));
        }
        catch (Exception e)
        {
        }

        //Try looking at root of classpath
        try
        {
            return new InputSource("/" + cl.getResourceAsStream(xsd));
        }
        catch (Exception e)
        {
        }

        //Try current working directory
        try
        {
            return new InputSource(new BufferedInputStream(new FileInputStream(xsd)));
        }
        catch (Exception e)
        {
        }
        return null;
    }

    private void print( SAXParseException x )
    {
        String msg = message.format( new Object[]{x.getSystemId( ),
                new Integer( x.getLineNumber( ) ),
                new Integer( x.getColumnNumber( ) ), x.getMessage( )} );
        System.out.println( msg );
    }

    public void warning( SAXParseException x )
    {
        print( x );
    }

    public void error( SAXParseException x )
    {
        print( x );
    }

    public void fatalError( SAXParseException x ) throws SAXParseException
    {
        print( x );
        throw x;
    }

}
