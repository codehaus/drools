package org.drools.io;

/*
 * $Id: RuleSetReader.java,v 1.46 2005-02-04 02:13:38 mproctor Exp $
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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.DefaultSemanticsRepository;
import org.drools.smf.NoSuchSemanticModuleException;
import org.drools.smf.SemanticModule;
import org.drools.smf.SemanticsRepository;
import org.drools.spi.RuleBaseContext;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <code>RuleSet</code> loader.
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * 
 * @version $Id: RuleSetReader.java,v 1.46 2005-02-04 02:13:38 mproctor Exp $
 */
public class RuleSetReader extends DefaultHandler
{
    // ----------------------------------------------------------------------
    // Constants
    // ----------------------------------------------------------------------

    /** Namespace URI for the general tags. */
    public static final String  RULES_NAMESPACE_URI  = "http://drools.org/rules";

    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String W3C_XML_SCHEMA       = "http://www.w3.org/2001/XMLSchema";    

    // ----------------------------------------------------------------------
    // Instance members
    // ----------------------------------------------------------------------
    /** SAX parser. */
    private SAXParser           parser;
    
    /** isValidating */
    private boolean             isValidating = true;

    /** Locator for errors. */
    private Locator             locator;

    /** Repository of semantic modules. */
    private SemanticsRepository repo;
    // private Map repo;

    /** Stack of configurations. */
    private LinkedList          configurationStack;

    /** Current configuration text. */
    private StringBuffer        characters;

    private Map                 handlers;

    private boolean             lastWasEndElement;

    private LinkedList          parents;

    private Object              peer;

    private Object              current;

    private RuleSet             ruleSet;

    private RuleBaseContext     factoryContext;

    private MessageFormat       message              = new MessageFormat( "({0}: {1}, {2}): {3}" );

    // ----------------------------------------------------------------------
    // Constructors
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
        // init
        this.configurationStack = new LinkedList( );
        this.parents = new LinkedList( );

        this.handlers = new HashMap( );
        this.handlers.put( "RuleSet",
                           new RuleSetHandler( this ) );
        this.handlers.put( "ImportEntry",
                           new ImportHandler( this ) );
        this.handlers.put( "ApplicationData",
                           new ApplicationDataHandler( this ) );
        this.handlers.put( "Functions",
                           new FunctionsHandler( this ) );
        this.handlers.put( "Rule",
                           new RuleHandler( this ) );
        this.handlers.put( "Parameter",
                           new ParameterHandler( this ) );
        // localNameMap.put( "declaration", new DeclarationHandler( this ) );
        this.handlers.put( "ObjectType",
                           new ObjectTypeHandler( this ) );
        this.handlers.put( "Condition",
                           new ConditionHandler( this ) );
        this.handlers.put( "Duration",
                           new DurationHandler( this ) );
        this.handlers.put( "Consequence",
                           new ConsequenceHandler( this ) );

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
    public RuleSetReader(SAXParser parser)
    {
        this( );
        this.parser = parser;
    }

    /**
     * Construct.
     * 
     * @param repo
     *            The semantics repository.
     */
    public RuleSetReader(SemanticsRepository repo)
    {
        this( );
        this.repo = repo;
    }

    /**
     * Construct.
     * 
     * @param factoryContext
     */
    public RuleSetReader(RuleBaseContext factoryContext)
    {
        this( );
        this.factoryContext = factoryContext;
    }

    /**
     * Construct.
     * 
     * @param repo
     *            The semantics repository.
     * @param parser
     *            The SAX parser.
     */
    public RuleSetReader(SemanticsRepository repo,
                         SAXParser parser)
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
    public RuleSetReader(SemanticsRepository repo,
                         RuleBaseContext context)
    {
        this( );
        this.repo = repo;
        this.factoryContext = context;
    }

    /**
     * Construct.
     * 
     * @param parser
     * 
     * @param repo
     *            The semantics repository.
     */
    public RuleSetReader(SAXParser parser,
                         SemanticsRepository repo)
    {
        this( );
        this.parser = parser;
        this.repo = repo;
    }

    /**
     * Construct.
     * 
     * @param parser
     * @param context
     */
    public RuleSetReader(SAXParser parser,
                         RuleBaseContext context)
    {
        this( );
        this.parser = parser;
        this.factoryContext = context;
    }

    /**
     * Construct.
     * 
     * @param repo
     *            The semantics repository.
     * @param parser
     *            The SAX parser.
     */
    public RuleSetReader(SemanticsRepository repo,
                         SAXParser parser,
                         RuleBaseContext context)
    {
        this( parser );
        this.repo = repo;
        this.factoryContext = context;
    }

    // ----------------------------------------------------------------------
    // Instance methods
    // ----------------------------------------------------------------------

    /**
     * Read a <code>RuleSet</code> from a <code>URL</code>.
     * 
     * @param url
     *            The rule-set URL.
     * 
     * @return The rule-set.
     */
    public RuleSet read(URL url) throws SAXException,
                                IOException
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
     */
    public RuleSet read(Reader reader) throws SAXException,
                                      IOException
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
     */
    public RuleSet read(InputStream inputStream) throws SAXException,
                                                IOException
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
     */
    public RuleSet read(String url) throws SAXException,
                                   IOException
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
     */
    public RuleSet read(InputSource in) throws SAXException,
                                       IOException
    {
        SAXParser localParser = null;
        if ( this.parser == null )
        {
            SAXParserFactory factory = SAXParserFactory.newInstance( );

            factory.setNamespaceAware( true );                       
            
            String isValidatingString = System.getProperty( "drools.schema.validating" );
            if ( System.getProperty( "drools.schema.validating" ) != null )
            {
                this.isValidating = Boolean.getBoolean("drools.schema.validating");
            }
            
            if (this.isValidating == true)
            {
                factory.setValidating( true );
                try
                {
                    localParser = factory.newSAXParser( );
                }
                catch ( ParserConfigurationException e )
                {
                    throw new RuntimeException( e.getMessage( ) );
                }      
                
                try
                {
                    localParser.setProperty( JAXP_SCHEMA_LANGUAGE,
                                             W3C_XML_SCHEMA );
                }
                catch ( SAXNotRecognizedException e )
                {
                    System.err.println( "Your SAX parser is not JAXP 1.2 compliant - turning off validation." );
                    localParser = null;
                }  
            }

            if ( localParser == null )
            {
                // not jaxp1.2 compliant so turn off validation
                try
                {
                    this.isValidating = false;
                    factory.setValidating( this.isValidating ); 
                    localParser= factory.newSAXParser( );
                }
                catch ( ParserConfigurationException e )
                {
                    throw new RuntimeException( e.getMessage( ) );
                }     
            }
            
        }
        else
        {
            localParser = this.parser;
        }

        if ( !localParser.isNamespaceAware( ) )
        {
            throw new RuntimeException( "parser must be namespace-aware" );
        }

        if ( this.repo == null )
        {
            try
            {
                this.repo = DefaultSemanticsRepository.getInstance( );
            }
            catch ( Exception e )
            {
                throw new SAXException( "Unable to reference a Semantics Repository" );
            }
        }

        localParser.parse( in,
                           this );

        return this.ruleSet;
    }

    public SemanticsRepository getSemanticsRepository()
    {
        return this.repo;
    }

    void setRuleSet(RuleSet ruleSet)
    {
        this.ruleSet = ruleSet;
    }

    public RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    public RuleBaseContext getFactoryContext()
    {
        return this.factoryContext;
    }

    /**
     * @see org.xml.sax.ContentHandler
     */
    public void setLocator(Locator locator)
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
        this.isValidating = true;
        this.ruleSet = null;
        this.current = null;
        this.peer = null;
        this.lastWasEndElement = false;
        this.parents.clear( );
        this.characters = null;
        this.configurationStack.clear( );
        if ( this.factoryContext == null )
        {
            this.factoryContext = new RuleBaseContext( );
        }
    }

    /**
     * @param uri
     * @param localName
     * @param qname
     * @param attrs
     * @throws SAXException
     * @see org.xml.sax.ContentHandler
     * 
     * @todo: better way to manage unhandled elements
     */
    public void startElement(String uri,
                             String localName,
                             String qname,
                             Attributes attrs) throws SAXException
    {
        // going down so no peer
        if ( !this.lastWasEndElement )
        {
            this.peer = null;
        }

        Handler handler = getHandler( uri,
                                      localName );

        if ( handler == null )
        {
            if ( this.parents.getLast( ).getClass( ).isInstance( Rule.class ) || this.parents.getLast( ).getClass( ).isInstance( RuleSet.class ) )
            {
                throw new SAXParseException( "unable to handle element <" + localName + ">",
                                             getLocator( ) );
            }
            // no handler so build up the configuration
            startConfiguration( localName,
                                attrs );
            return;
        }

        validate( uri,
                  localName,
                  handler );

        Object node = handler.start( uri,
                                     localName,
                                     attrs );

        if ( node != null )
        {
            this.parents.add( node );
            this.current = node;
        }
        this.lastWasEndElement = false;
    }

    /**
     * @param uri
     * @param localName
     * @param qname
     * @throws SAXException
     * @see org.xml.sax.ContentHandler
     */
    public void endElement(String uri,
                           String localName,
                           String qname) throws SAXException
    {
        Handler handler = getHandler( uri,
                                      localName );
        if ( handler == null )
        {
            if ( this.configurationStack.size( ) >= 1 )
            {
                endConfiguration( );
            }
            return;
        }

        this.current = getParent( handler.generateNodeFor( ) );

        Object node = handler.end( uri,
                                   localName );

        // next
        if ( node != null && !this.lastWasEndElement )
        {
            this.peer = node;
        }
        // up or no children
        else if ( this.lastWasEndElement || (this.parents.getLast( )).getClass( ).isInstance( this.current ) )
        {
            this.peer = this.parents.removeLast( );
        }

        this.lastWasEndElement = true;
    }

    private void validate(String uri,
                          String localName,
                          Handler handler) throws SAXParseException
    {
        boolean validParent = false;
        boolean validPeer = false;
        boolean invalidNesting = false;

        Set validParents = handler.getValidParents( );
        Set validPeers = handler.getValidPeers( );
        boolean allowNesting = handler.allowNesting( );

        // get parent
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
        Class nodeClass = getHandler( uri,
                                      localName ).generateNodeFor( );
        if ( !nodeClass.isInstance( parent ) )
        {
            Object allowedParent;
            Iterator it = validParents.iterator( );
            while ( !validParent && it.hasNext( ) )
            {
                allowedParent = it.next( );
                if ( parent == null && allowedParent == null )
                {
                    validParent = true;
                }
                else if ( allowedParent != null && ((Class) allowedParent).isInstance( parent ) )
                {
                    validParent = true;
                }
            }
            if ( !validParent )
            {
                throw new SAXParseException( "<" + localName + "> has an invalid parent element",
                                             getLocator( ) );
            }
        }

        // check valid peers
        // null peer means localname is rule-set
        Object peer = this.peer;

        Object allowedPeer;
        Iterator it = validPeers.iterator( );
        while ( !validPeer && it.hasNext( ) )
        {
            allowedPeer = it.next( );
            if ( peer == null && allowedPeer == null )
            {
                validPeer = true;
            }
            else if ( allowedPeer != null && ((Class) allowedPeer).isInstance( peer ) )
            {
                validPeer = true;
            }
        }
        if ( !validPeer )
        {
            throw new SAXParseException( "<" + localName + "> is after an invalid element",
                                         getLocator( ) );
        }

        if ( !allowNesting )
        {
            it = this.parents.iterator( );
            while ( !invalidNesting && it.hasNext( ) )
            {
                if ( nodeClass.isInstance( it.next( ) ) )
                {
                    invalidNesting = true;
                }
            }
        }
        if ( invalidNesting )
        {
            throw new SAXParseException( "<" + localName + ">  may not be nested",
                                         getLocator( ) );
        }

    }

    /**
     * Start a configuration node.
     * 
     * @param name
     *            Tag name.
     * @param attrs
     *            Tag attributes.
     */
    protected void startConfiguration(String name,
                                      Attributes attrs)
    {
        this.characters = new StringBuffer( );

        DefaultConfiguration config = new DefaultConfiguration( name );

        int numAttrs = attrs.getLength( );

        for ( int i = 0; i < numAttrs; ++i )
        {
            config.setAttribute( attrs.getLocalName( i ),
                                 attrs.getValue( i ) );
        }

        if ( this.configurationStack.isEmpty( ) )
        {
            this.configurationStack.addLast( config );
        }
        else
        {
            ((DefaultConfiguration) this.configurationStack.getLast( )).addChild( config );
            this.configurationStack.addLast( config );
        }
    }

    /**
     * @param chars
     * @param start
     * @param len
     * @see org.xml.sax.ContentHandler
     */
    public void characters(char[] chars,
                           int start,
                           int len)
    {
        if ( this.characters != null )
        {
            this.characters.append( chars,
                                    start,
                                    len );
        }
    }

    /**
     * End a configuration node.
     * 
     * @return The configuration.
     */
    protected Configuration endConfiguration()
    {
        DefaultConfiguration config = (DefaultConfiguration) this.configurationStack.removeLast( );
        if ( this.characters != null )
        {
            config.setText( this.characters.toString( ) );
        }

        this.characters = null;

        return config;
    }

    SemanticModule lookupSemanticModule(String uri,
                                        String localName) throws SAXParseException
    {
        SemanticModule module;
        try
        {
            module = this.repo.lookupSemanticModule( uri );
        }
        catch ( NoSuchSemanticModuleException e )
        {
            throw new SAXParseException( "no semantic module for namespace '" + uri + "' (" + localName + ")",
                                         getLocator( ) );
        }
        return module;
    }

    /**
     * Returned Handler can be null.
     * Calling method decides whether to throw an exception or not.
     * @param uri
     * @param localName
     * @return
     */
    private Handler getHandler(String uri,
                               String localName)
    {
        String type = null;
        if ( localName.equals( "rule-set" ) )
        {
            type = "RuleSet";
        }
        else if ( localName.equals( "parameter" ) )
        {
            type = "Parameter";
        }
        else
        {
            SemanticModule module;
            try
            {
                module = this.repo.lookupSemanticModule( uri );
                type = module.getType( localName );
            }
            catch ( NoSuchSemanticModuleException e )
            {
                // swallow as we just return a null handler
            }
        }

        Handler handler = (Handler) this.handlers.get( type );

        return handler;
    }

    LinkedList getParents()
    {
        return this.parents;
    }

    Object getParent(Class parent)
    {
        ListIterator it = this.parents.listIterator( this.parents.size( ) );
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
    {
        // Schema files must end with xsd
        if ( !systemId.toLowerCase( ).endsWith( "xsd" ) )
        {
            return null;
        }

        // try the actual location given by systemId
        try
        {
            URL url = new URL( systemId );
            return new InputSource( url.openStream( ) );
        }
        catch ( Exception e )
        {
        }

        // Try and get the index for the filename, else return null
        String xsd;
        int index = systemId.lastIndexOf( "/" );
        if ( index == -1 )
        {
            index = systemId.lastIndexOf( "\\" );
        }
        if ( index != -1 )
        {
            xsd = systemId.substring( index + 1 );
        }
        else
        {
            xsd = systemId;
        }

        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );

        if ( cl == null )
        {
            cl = RuleSetReader.class.getClassLoader( );
        }

        // Try looking in META-INF
        try
        {
            return new InputSource( cl.getResourceAsStream( "META-INF/" + xsd ) );
        }
        catch ( Exception e )
        {
        }

        // Try looking in /META-INF
        try
        {
            return new InputSource( cl.getResourceAsStream( "/META-INF/" + xsd ) );
        }
        catch ( Exception e )
        {
        }

        // Try looking at root of classpath
        try
        {
            return new InputSource( cl.getResourceAsStream( "/" + xsd ) );
        }
        catch ( Exception e )
        {
        }

        // Try current working directory
        try
        {
            return new InputSource( new BufferedInputStream( new FileInputStream( xsd ) ) );
        }
        catch ( Exception e )
        {
        }
        return null;
    }

    private void print(SAXParseException x)
    {
        String msg = this.message.format( new Object[]{x.getSystemId( ), new Integer( x.getLineNumber( ) ), new Integer( x.getColumnNumber( ) ), x.getMessage( )} );
        System.out.println( msg );
    }

    public void warning(SAXParseException x)
    {
        print( x );
    }

    public void error(SAXParseException x)
    {
        print( x );
    }

    public void fatalError(SAXParseException x) throws SAXParseException
    {
        print( x );
        throw x;
    }

}
