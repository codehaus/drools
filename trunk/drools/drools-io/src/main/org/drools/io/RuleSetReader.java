package org.drools.io;

/*
 $Id: RuleSetReader.java,v 1.14 2004-09-14 19:28:46 mproctor Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a trademark of
    The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://werken.com/)

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.rule.Declaration;
import org.drools.rule.Extraction;
import org.drools.rule.RuleConstructionException;
import org.drools.spi.ObjectType;
import org.drools.spi.Extractor;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.Duration;
import org.drools.smf.Configuration;
import org.drools.smf.SemanticModule;
import org.drools.smf.SemanticsRepository;
import org.drools.smf.RuleFactory;
import org.drools.smf.ObjectTypeFactory;
import org.drools.smf.ConditionFactory;
import org.drools.smf.ExtractorFactory;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.DurationFactory;
import org.drools.smf.FactoryException;
import org.drools.smf.DefaultSemanticsRepository;
import org.drools.smf.NoSuchSemanticModuleException;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.Reader;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;


import java.text.MessageFormat;


/** <code>RuleSet</code> loader.
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *
 *  @version $Id: RuleSetReader.java,v 1.14 2004-09-14 19:28:46 mproctor Exp $
 */
public class RuleSetReader
    extends DefaultHandler
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Namespace URI for the general tags. */
    public static final String RULES_NAMESPACE_URI = "http://drools.org/rules";

    public static String SCHEMA_LANGUAGE =
        "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    public static String XML_SCHEMA = 
        "http://www.w3.org/2001/XMLSchema";
    public static String SCHEMA_SOURCE =
        "http://java.sun.com/xml/jaxp/properties/schemaSource";    

    private static final int STATE_NONE        = 0;
    private static final int STATE_OBJECT_TYPE = 2;
    private static final int STATE_CONDITION   = 3;
    private static final int STATE_EXTRACTION  = 4;
    private static final int STATE_CONSEQUENCE = 5;
    private static final int STATE_DURATION    = 6;    

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    private int state;

    /** SAX parser. */
    private SAXParser parser;

    /** Locator for errors. */
    private Locator locator;

    /** Current rule-set being built. */
    private RuleSet ruleSet;

    /** Current rule. */
    private Rule rule;

    /** Current declaration. */
    private Declaration declaration;

    /** Current extraction. */
    private Extraction extraction;

    /** Stack of configurations. */
    private LinkedList configurationStack;

    /** Current configuration text. */
    private StringBuffer characters;

    /** Start-tag functors. */
    private Map starters;

    /** End-tag functors. */
    private Map enders;

    /** Repository of semantic modules. */
    private SemanticsRepository repo;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  <p>
     *  Uses the default JAXP SAX parser and the default classpath-based
     *  <code>DefaultSemanticModule</code>.
     *  </p>
     */
    public RuleSetReader()
    {
        this.state              = STATE_NONE;

        this.starters           = new HashMap();
        this.enders             = new HashMap();
        this.configurationStack = new LinkedList();

        // - - - - - - - - - - - - - - - - - - - -
        // - - - - - - - - - - - - - - - - - - - -

        bindStarter( RULES_NAMESPACE_URI,
                     "rule-set",
                     new Starter()
                     {
                         void start(Attributes attrs)
                             throws SAXException
                         {
                             startRuleSet( attrs );
                         }
                     } );

        bindEnder( RULES_NAMESPACE_URI,
                   "rule-set",
                   new Ender()
                   {
                       void end()
                           throws SAXException
                       {
                           endRuleSet();
                       }
                   } );

        bindStarter( RULES_NAMESPACE_URI,
                     "parameter",
                     new Starter()
                     {
                         void start(Attributes attrs)
                             throws SAXException
                         {
                             startParameter( attrs );
                         }
                     } );

        bindEnder( RULES_NAMESPACE_URI,
                   "parameter",
                   new Ender()
                   {
                       void end()
                           throws SAXException
                       {
                           endParameter();
                       }
                   } );

        bindStarter( RULES_NAMESPACE_URI,
                     "declaration",
                     new Starter()
                     {
                         void start(Attributes attrs)
                             throws SAXException
                         {
                             startDeclaration( attrs );
                         }
                     } );

        bindEnder( RULES_NAMESPACE_URI,
                   "declaration",
                   new Ender()
                   {
                       void end()
                           throws SAXException
                       {
                           endDeclaration();
                       }
                   } );
    }

    /** Construct.
     *
     *  <p>
     *  Uses the default classpath-based <code>DefaultSemanticModule</code>.
     *  </p>
     *
     *  @param parser The SAX parser.
     */
    public RuleSetReader(SAXParser parser)
    {
        this();
        this.parser = parser;
    }

    /** Construct.
     *
     *  @param repo The semantics repository.
     *  @param parser The SAX parser.
     */
    public RuleSetReader(SemanticsRepository repo,
                         SAXParser parser)
    {
        this( parser );
        this.repo = repo;
    }

    /** Construct.
     *
     *  @param repo The semantics repository.
     */
    public RuleSetReader(SemanticsRepository repo)
    {
        this();
        this.repo = repo;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Read a <code>RuleSet</code> from a <code>URL</code>.
     *
     *  @param url The rule-set URL.
     *
     *  @return The rule-set.
     *
     *  @throws Exception If an error occurs during the parse.
     */
    public RuleSet read(URL url)
        throws Exception
    {
        return read( new InputSource( url.toExternalForm() ) );
    }

    /** Read a <code>RuleSet</code> from a <code>Reader</code>.
     *
     *  @param reader The reader containing the rule-set.
     *
     *  @return The rule-set.
     *
     *  @throws Exception If an error occurs during the parse.
     */
    public RuleSet read(Reader reader)
        throws Exception
    {
        return read( new InputSource( reader ) );
    }

    /** Read a <code>RuleSet</code> from an <code>InputStream</code>.
     *
     *  @param inputStream The input-stream containing the rule-set.
     *
     *  @return The rule-set.
     *
     *  @throws Exception If an error occurs during the parse.
     */
    public RuleSet read(InputStream inputStream)
        throws Exception
    {
        return read( new InputSource( inputStream ) );
    }

    /** Read a <code>RuleSet</code> from a URL.
     *
     *  @param url The rule-set URL.
     *
     *  @return The rule-set.
     *
     *  @throws Exception If an error occurs during the parse.
     */
    public RuleSet read(String url)
        throws Exception
    {
        return read( new InputSource( url ) );
    }

    /** Read a <code>RuleSet</code> from an <code>InputSource</code>.
     *
     *  @param in The rule-set input-source.
     *
     *  @return The rule-set.
     *
     *  @throws Exception If an error occurs during the parse.
     */
    public RuleSet read(InputSource in)
        throws Exception
    {
        SAXParser parser = null;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        if ( cl == null )
        {
            cl = RuleSetReader.class.getClassLoader();
        }        

        if ( this.parser == null )
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(true);
            parser = factory.newSAXParser();
            try
            {
               parser.setProperty(SCHEMA_LANGUAGE,XML_SCHEMA);
               InputStream java = cl.getResourceAsStream("java.xsd");
               InputStream python = cl.getResourceAsStream("python.xsd");
               InputStream groovy = cl.getResourceAsStream("groovy.xsd");
               InputStream rules = cl.getResourceAsStream("rules.xsd");
               
               java.util.List schemaList = new java.util.ArrayList();

               if (java != null) schemaList.add(java);
               if (python != null) schemaList.add(python);
               if (groovy != null) schemaList.add(groovy);
               if (rules != null) schemaList.add(rules);
               
               InputStream[] sources = (InputStream[]) schemaList.toArray(new InputStream[0]);
               
               parser.setProperty(SCHEMA_SOURCE, sources);
            }
            catch(SAXNotRecognizedException e)
            {
               e.printStackTrace();
               System.err.println("Your SAX parser is not JAXP 1.2 compliant.");
            }
        }
        else
        {
            parser = this.parser;
        }

        if ( ! parser.isNamespaceAware() )
        {
            throw new ParserConfigurationException( "parser must be namespace-aware" );
        }

        if ( this.repo == null )
        {
            try
            {
                this.repo = DefaultSemanticsRepository.getInstance();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        parser.parse( in,
                      this );

        return this.ruleSet;
    }

    public RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    /** @see org.xml.sax.ContentHandler
     */
    public void setLocator(Locator locator)
    {
        this.locator = locator;
    }

    /** Get the <code>Locator</code>.
     *
     *  @return The locator.
     */
    public Locator getLocator()
    {
        return this.locator;
    }

    /** @see org.xml.sax.ContentHandler
     */
    public void startElement(String uri,
                             String localName,
                             String qname,
                             Attributes attrs)
        throws SAXException
    {
        Starter starter = lookupStarter( uri,
                                         localName );

        if ( starter != null )
        {
            starter.start( attrs );
        }
        else
        {
            try
            {
                SemanticModule module = this.repo.lookupSemanticModule( uri );

                if ( this.rule == null )
                {
                    if ( module.getRuleFactoryNames().contains( localName ) )
                    {
                        startRule( module,
                                   localName,
                                   attrs );
                    }
                    else
                    {
                        throw new SAXParseException( "unknown tag '" + localName + "' in namespace '" + uri + "'",
                                                     getLocator() );
                    }
                }
                else if ( this.declaration != null )
                {
                    if ( module.getObjectTypeFactoryNames().contains( localName ) )
                    {
                        startObjectType( module,
                                         localName,
                                         attrs );
                    }
                    else
                    {
                        throw new SAXParseException( "unknown tag '" + localName + "' in namespace '" + uri + "'",
                                                     getLocator() );
                    }
                }
                else
                {
                   if ( module.getDurationFactoryNames().contains( localName ) )
                    {
                        startDuration(  module,
                                        localName,
                                        attrs );
                    } else if ( module.getConditionFactoryNames().contains( localName ) )
                    {
                        startCondition( module,
                                        localName,
                                        attrs );
                    }
                    else if ( module.getExtractorFactoryNames().contains( localName ) )
                    {
                        startExtraction( module,
                                         localName,
                                         attrs );
                    }
                    else if ( module.getConsequenceFactoryNames().contains( localName ) )
                    {
                        startConsequence( module,
                                          localName,
                                          attrs );
                    }
                    else
                    {
                        throw new SAXParseException( "unknown tag '" + localName + "' in namespace '" + uri + "'",
                                                     getLocator() );
                    }
                }
            }
            catch (NoSuchSemanticModuleException e)
            {
                throw new SAXParseException( "no semantic module for namespace '" + uri + "' (" + localName + ")",
                                             getLocator() );
            }
        }
    }

    /** @see org.xml.sax.ContentHandler
     */
    public void endElement(String uri,
                           String localName,
                           String qname)
        throws SAXException
    {
        Ender ender = lookupEnder( uri,
                                   localName );

        if ( ender != null )
        {
            ender.end();
        }
        else
        {
            if ( this.configurationStack.size() <= 1 )
            {
                try
                {
                    SemanticModule module = this.repo.lookupSemanticModule( uri );

                    switch ( this.state )
                    {
                        case ( STATE_OBJECT_TYPE ):
                        {
                            endObjectType( module,
                                           localName );
                            break;
                        }
                        case ( STATE_CONDITION ):
                        {
                            endCondition( module,
                                          localName );
                            break;
                        }
                        case ( STATE_DURATION ):
                        {
                            endDuration( module,
                                          localName );
                            break;
                        }
                        case ( STATE_EXTRACTION ):
                        {
                            endExtraction( module,
                                           localName );
                            break;
                        }
                        case ( STATE_CONSEQUENCE ):
                        {
                            endConsequence( module,
                                            localName );
                            break;
                        }
                        default:
                        {
                            endRule();
                        }
                    }
                }
                catch (NoSuchSemanticModuleException e)
                {
                    throw new SAXParseException( "no semantic module for namespace '" + uri + "' (" + localName + ")",
                                                 getLocator() );
                }
            }
            else
            {
                endConfiguration();
            }
        }
    }

    /** Start a &lt;rule-set&gt;.
     *
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startRuleSet(Attributes attrs)
        throws SAXException
    {
        if ( this.ruleSet != null )
        {
            throw new SAXParseException( "<rule-set> may not be nested",
                                         getLocator() );
        }

        String ruleSetName = attrs.getValue( "name" );
        String ruleSetDesc = attrs.getValue( "description" );

        if ( ruleSetName == null
             ||
             ruleSetName.trim().equals( "" ) )
        {
            throw new SAXParseException( "<rule-set> requires a 'name' attribute",
                                         getLocator() );
        }

        this.ruleSet = new RuleSet( ruleSetName.trim() );

        if ( ruleSetDesc == null
            ||
            ruleSetDesc.trim().equals( "" ) )
        {
            this.ruleSet.setDocumentation( "" );
        }
        else
        {
            this.ruleSet.setDocumentation( ruleSetDesc );
        }

    }

    /** End a &lt;rule-set&gt;.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endRuleSet()
        throws SAXException
    {
        // nothing
    }

    protected void startRule(SemanticModule module,
                             String localName,
                             Attributes attrs)
        throws SAXException
    {
        RuleFactory factory = module.getRuleFactory( localName );

        startConfiguration( localName,
                            attrs );

        Configuration config = endConfiguration();

        try
        {
            this.rule = factory.newRule( config );

            startRule( rule,
                       attrs );
        }
        catch (FactoryException e)
        {
            throw new SAXParseException( "error constructing rule",
                                         getLocator(),
                                         e );
        }
    }

    protected void startRule(Rule rule,
                             Attributes attrs )
        throws SAXException
    {
        String salienceStr = attrs.getValue( "salience" );
        String ruleDesc = attrs.getValue( "description" );

        if ( ! ( salienceStr == null
                 ||
                 salienceStr.trim().equals( "" ) ) )
        {
            try
            {
                int salience = Integer.parseInt( salienceStr.trim() );

                rule.setSalience( salience );
            }
            catch (NumberFormatException e)
            {
                throw new SAXParseException( "invalid number value for 'salience' attribute: " + salienceStr.trim(),
                                             getLocator() );
            }
        }

        if ( ! ( ruleDesc == null
                 ||
                 ruleDesc.trim().equals( "" ) ) )
        {
            rule.setDocumentation( ruleDesc );
        }
    }

    /** End a &lt;rule&gt;.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endRule()
        throws SAXException
    {
        try
        {
            this.ruleSet.addRule( this.rule );
            this.rule = null;
        }
        catch (RuleConstructionException e)
        {
            throw new SAXParseException( "error adding rule",
                                         getLocator(),
                                         e );
        }
    }

    /** Start a &lt;parameter&gt;.
     *
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startParameter(Attributes attrs)
        throws SAXException
    {
        startParameterOrDeclaration( "parameter",
                                     attrs );
    }

    /** End a &lt;parameter&gt;.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endParameter()
        throws SAXException
    {
        if ( this.declaration.getObjectType() == null )
        {
            throw new SAXParseException( "<parameter> requires an object-type",
                                         getLocator() );
        }

        this.rule.addParameterDeclaration( this.declaration );
        this.declaration = null;
    }

    /** Start a &lt;declaration&gt;.
     *
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startDeclaration(Attributes attrs)
        throws SAXException
    {
        startParameterOrDeclaration( "declaration",
                                     attrs );
    }

    /** End a &lt;declaration&gt;.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endDeclaration()
        throws SAXException
    {
        if ( this.declaration.getObjectType() == null )
        {
            throw new SAXParseException( "<declaration> requires an object-type",
                                         getLocator() );
        }

        this.rule.addDeclaration( this.declaration );
        this.declaration = null;
    }

    /** Start a &lt;parameter&gt; or &lt;declaration&gt;.
     *
     *  @param tagName Tag name.
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    private void startParameterOrDeclaration(String tagName,
                                             Attributes attrs)
        throws SAXException
    {
        if ( this.rule == null )
        {
            throw new SAXParseException( "<" + tagName + "> must occur within a <rule>",
                                         getLocator() );
        }

        String identifier = attrs.getValue( "identifier" );

        if ( identifier == null
             ||
             identifier.trim().equals( "" ) )
        {
            throw new SAXParseException( "<" + tagName + "> requires an 'identifier' attribute",
                                         getLocator() );
        }

        this.declaration = new Declaration( identifier.trim() );
    }

    /** Start an object-type.
     *  @param module SemanticModule.
     *  @param localName Tag name.
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startObjectType(SemanticModule module,
                                   String localName,
                                   Attributes attrs)
        throws SAXException
    {
        this.state = STATE_OBJECT_TYPE;

        startConfiguration( localName,
                            attrs );
    }

    /** End object-type.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endObjectType(SemanticModule module,
                                 String localName)
        throws SAXException
    {
        Configuration config = endConfiguration();

        ObjectTypeFactory factory = module.getObjectTypeFactory( localName );

        try
        {
            ObjectType objectType = factory.newObjectType( config );

            this.declaration.setObjectType( objectType );
        }
        catch (FactoryException e)
        {
            throw new SAXParseException( "error constructing object type",
                                         getLocator(),
                                         e );
        }
        finally
        {
            this.state = STATE_NONE;
        }
    }

    /** Start an extraction.
     *
     *  @param module Semantic module.
     *  @param localName Tag name.
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startExtraction(SemanticModule module,
                                   String localName,
                                   Attributes attrs)
        throws SAXException
    {
        this.state = STATE_EXTRACTION;

        String targetDeclName = attrs.getValue( "target" );

        if ( targetDeclName == null
             ||
             targetDeclName.trim().equals( "" ) )
        {
            throw new SAXParseException( "extraction requires a 'target' attribute",
                                         getLocator() );
        }

        Declaration targetDecl = this.rule.getDeclaration( targetDeclName.trim() );

        if ( targetDecl == null )
        {
            throw new SAXParseException( "'" + targetDeclName + "' is not a valid declaration",
                                         getLocator() );
        }

        this.extraction = new Extraction( targetDecl );

        startConfiguration( localName,
                            attrs );
    }

    /** End an extraction.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endExtraction(SemanticModule module,
                                 String localName)
        throws SAXException
    {
        Configuration config = endConfiguration();

        ExtractorFactory factory = module.getExtractorFactory( localName );

        try
        {
            Extractor extractor = factory.newExtractor( config,
                                                        this.rule.getAllDeclarations() );

            this.extraction.setExtractor( extractor );

            this.rule.addExtraction( this.extraction );

            this.extraction = null;
        }
        catch (FactoryException e)
        {
            throw new SAXParseException( "error constructing extractor",
                                         getLocator(),
                                         e );
        }
        finally
        {
            this.state = STATE_NONE;
        }
    }

    protected void startDuration(SemanticModule module,
    														 String name,
    														 Attributes attrs)
				throws SAXException
		{
        this.state = STATE_DURATION;

        startConfiguration( name,
                            attrs );
		}

    /** End a condition.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endDuration( SemanticModule module,
                                String localName)
        throws SAXException
    {
        Configuration config = endConfiguration();

        DurationFactory factory = module.getDurationFactory( localName );

        try
        {
            Duration duration = factory.newDuration( config,
                                                     this.rule.getAllDeclarations() );

            this.rule.setDuration( duration );
        }
        catch (FactoryException e)
        {
            throw new SAXParseException( "error constructing duration",
                                         getLocator(),
                                         e );
        }
        finally
        {
            this.state = STATE_NONE;
        }
    }


    /** Start a condition.
     *
     *  @param module Semantic module.
     *  @param name Tag name.
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startCondition(SemanticModule module,
                                  String name,
                                  Attributes attrs)
        throws SAXException
    {
        this.state = STATE_CONDITION;

        startConfiguration( name,
                            attrs );
    }

    /** End a condition.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endCondition(SemanticModule module,
                                String localName)
        throws SAXException
    {
        Configuration config = endConfiguration();

        ConditionFactory factory = module.getConditionFactory( localName );

        try
        {
            Condition condition = factory.newCondition( config,
                                                        this.rule.getAllDeclarations() );

            this.rule.addCondition( condition );
        }
        catch (FactoryException e)
        {
            throw new SAXParseException( "error constructing condition",
                                         getLocator(),
                                         e );
        }
        finally
        {
            this.state = STATE_NONE;
        }
    }

    /** Start a consequence.
     *
     *  @param module Semantic module.
     *  @param name Tag name.
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startConsequence(SemanticModule module,
                                    String name,
                                    Attributes attrs)
        throws SAXException
    {
        this.state = STATE_CONSEQUENCE;

        startConfiguration( name,
                            attrs );
    }

    /** End a consequence.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endConsequence(SemanticModule module,
                                  String localName)
        throws SAXException
    {
        Configuration config = endConfiguration();

        ConsequenceFactory factory = module.getConsequenceFactory( localName );

        try
        {
            Consequence consequence = factory.newConsequence( config,
                                                              this.rule.getAllDeclarations() );

            this.rule.setConsequence( consequence );
        }
        catch (FactoryException e)
        {
            throw new SAXParseException( "error constructing consequence",
                                         getLocator(),
                                         e );
        }
        finally
        {
            this.state = STATE_NONE;
        }
    }

    /** Start a configuration node.
     *
     *  @param name Tag name.
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startConfiguration(String name,
                                      Attributes attrs)
        throws SAXException
    {
        this.characters = new StringBuffer();

        DefaultConfiguration config = new DefaultConfiguration( name );

        int numAttrs = attrs.getLength();

        for ( int i = 0 ; i < numAttrs ; ++i )
        {
            config.setAttribute( attrs.getLocalName( i ),
                                 attrs.getValue( i ) );
        }

        if ( this.configurationStack.isEmpty() )
        {
            this.configurationStack.addLast( config );
        }
        else
        {
            ((DefaultConfiguration)this.configurationStack.getLast()).addChild( config );
        }
    }

    /** @see org.xml.sax.ContentHandler
     */
    public void characters(char[] chars,
                           int start,
                           int len)
        throws SAXException
    {
        if ( this.characters != null )
        {
            this.characters.append( chars,
                                    start,
                                    len );
        }
    }

    /** End a configuration node.
     *
     *  @return The configuration.
     */
    protected Configuration endConfiguration()
    {
        DefaultConfiguration config = (DefaultConfiguration) this.configurationStack.removeLast();

        config.setText( this.characters.toString() );

        this.characters = null;

        return config;
    }

    /** Lookup a <code>Starter</code> functor.
     *
     *  @param uri Tag uri.
     *  @param name Tag name.
     *
     *  @return The starter.
     */
    Starter lookupStarter(String uri,
                          String name)
    {
        return (Starter) this.starters.get( uri + ">" + name );
    }

    /** Bind a <code>Starter</code> functor.
     *
     *  @param uri Tag uri.
     *  @param name Tag name.
     *  @param starter Starter.
     */
    void bindStarter(String uri,
                     String name,
                     Starter starter)
    {
        this.starters.put( uri + ">" + name,
                           starter );
    }

    /** Lookup an <code>Ender</code> functor.
     *
     *  @param uri Tag uri.
     *  @param name Tag name.
     *
     *  @return The starter.
     */
    Ender lookupEnder(String uri,
                      String name)
    {
        return (Ender) this.enders.get( uri + ">" + name );
    }

    /** Bind a <code>Ender</code> functor.
     *
     *  @param uri Tag uri.
     *  @param name Tag name.
     *  @param ender Ender.
     */
    void bindEnder(String uri,
                   String name,
                   Ender ender)
    {
        this.enders.put( uri + ">" + name,
                         ender );
    }
    private MessageFormat message =
        new MessageFormat("({0}: {1}, {2}): {3}");

     private void print(SAXParseException x)
     {
        String msg = message.format(new Object[]
                                    {
                                       x.getSystemId(),
                                       new Integer(x.getLineNumber()),
                                       new Integer(x.getColumnNumber()),
                                       x.getMessage()
                                    });
        System.out.println(msg);
     }

     public void warning(SAXParseException x)
     {
        print(x);
     }

     public void error(SAXParseException x)
     {
        print(x);
     }

     public void fatalError(SAXParseException x)
        throws SAXParseException
     {
        print(x);
        throw x;
     }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** Starter functor.
     */
    abstract static class Starter
    {
        /** Start tag.
         *
         *  @param attrs Tag attributes.
         *
         *  @throws SAXException If an error occurs during parse.
         */
        abstract void start(Attributes attrs)
            throws SAXException;
    }

    /** Starter functor.
     */
    abstract static class Ender
    {
        /** End tag.
         *
         *  @throws SAXException If an error occurs during parse.
         */
        abstract void end()
            throws SAXException;
    }

  
}
