package org.drools.io;

/*
 $Id: RuleSetReader.java,v 1.7 2003-11-21 04:18:13 bob Exp $

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
import org.drools.smf.Configuration;
import org.drools.smf.SemanticModule;
import org.drools.smf.SemanticsRepository;
import org.drools.smf.ConfigurableObjectType;
import org.drools.smf.ConfigurableCondition;
import org.drools.smf.ConfigurableExtractor;
import org.drools.smf.ConfigurableConsequence;
import org.drools.smf.ConfigurationException;
import org.drools.smf.DefaultSemanticsRepository;
import org.drools.smf.NoSuchSemanticModuleException;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.Reader;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/** <code>RuleSet</code> loader.
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *
 *  @version $Id: RuleSetReader.java,v 1.7 2003-11-21 04:18:13 bob Exp $
 */
public class RuleSetReader
    extends DefaultHandler
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Namespace URI for the general tags. */
    public static final String RULES_NAMESPACE_URI = "http://drools.org/rules";

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

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

    /** Current object-type. */
    private ObjectType objectType;

    /** Current extraction. */
    private Extraction extraction;

    /** Current condition. */
    private Condition condition;

    /** Current consequence. */
    private Consequence consequence;

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
                     "rule",
                     new Starter()
                     {
                         void start(Attributes attrs)
                             throws SAXException
                         {
                             startRule( attrs );
                         }
                     } );

        bindEnder( RULES_NAMESPACE_URI,
                   "rule",
                   new Ender()
                   {
                       void end()
                           throws SAXException
                       {
                           endRule();
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
                             startParameter( attrs );
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

        if ( this.parser == null )
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            
            factory.setNamespaceAware( true );
            
            parser = factory.newSAXParser();
        }
        else
        {
            parser = this.parser;
        }

        if ( this.repo == null )
        {
            this.repo = DefaultSemanticsRepository.getInstance();
        }

        parser.parse( in,
                      this );

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
        if ( this.configurationStack.size() > 1 )
        {
            endConfiguration();
        }
        else if ( this.declaration != null )
        {
            startObjectType( uri,
                             localName,
                             attrs );
        }
        else
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

                    if ( module.getConditionNames().contains( localName ) )
                    {
                        startCondition( module,
                                        localName,
                                        attrs );
                    }
                    else if ( module.getExtractorNames().contains( localName ) )
                    {
                        startExtraction( module,
                                         localName,
                                         attrs );
                    }
                    else if ( module.getConsequenceNames().contains( localName ) )
                    {
                        startConsequence( module,
                                          localName,
                                          attrs );
                    }
                }
                catch (NoSuchSemanticModuleException e)
                {
                    throw new SAXParseException( "no semantic module for namespace '" + uri + "' (" + localName + ")",
                                                 getLocator() );
                }
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
            // System.err.println( localName + " ///// " + this.configurationStack.size() );
            if ( this.configurationStack.size() == 1 )
            {
                if ( this.objectType != null )
                {
                    endObjectType();
                }
                else if ( this.condition != null )
                {
                    endCondition();
                }
                else if ( this.extraction != null )
                {
                    endExtraction();
                }
                else if ( this.consequence != null )
                {
                    endConsequence();
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

        if ( ruleSetName == null
             ||
             ruleSetName.trim().equals( "" ) )
        {
            throw new SAXParseException( "<rule-set> requires a 'name' attribute",
                                         getLocator() );
        }

        this.ruleSet = new RuleSet( ruleSetName.trim() );
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
    
    /** Start a &lt;rule&gt;.
     *
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startRule(Attributes attrs)
        throws SAXException
    {
        if ( this.rule != null )
        {
            throw new SAXParseException( "<rule> may not be nested",
                                         getLocator() );
        }

        if ( this.ruleSet == null )
        {
            throw new SAXParseException( "<rule> must occur within a <rule-set>",
                                         getLocator() );
        }

        String ruleName = attrs.getValue( "name" );

        if ( ruleName == null
             ||
             ruleName.trim().equals( "" ) )
        {
            throw new SAXParseException( "<rule> requires a 'name' attribute",
                                         getLocator() );
        }

        this.rule = new Rule( ruleName.trim() );

        String salienceStr = attrs.getValue( "salience" );

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
        // System.err.println( "endParameter() " + this.configurationStack.size() );
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
        // System.err.println( "endDeclaration() " + this.configurationStack.size() );
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
        // System.err.println( "startParamOrDecl(" + tagName + ") " + this.configurationStack.size() );
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
     *
     *  @param uri Tag URI.
     *  @param localName Tag name.
     *  @param attrs Tag attributes.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void startObjectType(String uri,
                                   String localName,
                                   Attributes attrs)
        throws SAXException
    {
        // System.err.println( "startObjectType(" + uri + ", " + localName + ") " + this.configurationStack.size() );
        try
        {
            SemanticModule module = this.repo.lookupSemanticModule( uri );

            if ( ! module.getObjectTypeNames().contains( localName ) )
            {
                throw new SAXParseException( "no object type '" + localName + "'",
                                             getLocator() );
            }

            Class objectTypeClass = module.getObjectType( localName );

            try
            {
                this.objectType = (ObjectType) objectTypeClass.newInstance();
                
                startConfiguration( localName,
                                    attrs );
            }
            catch (InstantiationException e)
            {
                throw new SAXParseException( "instantiation exception '" + objectTypeClass.getName() + "'",
                                             getLocator(),
                                             e );
            }
            catch (IllegalAccessException e)
            {
                throw new SAXParseException( "illegal access exception '" + objectTypeClass.getName() + "'",
                                             getLocator(),
                                             e );
            }
        }
        catch (NoSuchSemanticModuleException e)
        {
            throw new SAXParseException( "no semantic module for namespace '" + uri + "'",
                                         getLocator() );
        }
    }

    /** End object-type.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endObjectType()
        throws SAXException
    {
        // System.err.println( "endObjectType() " + this.configurationStack.size() );
        this.declaration.setObjectType( this.objectType );

        Configuration config = endConfiguration();

        // System.err.println( "typeinfo: " + config.getText() );

        // System.err.println( "objectType: " + this.objectType );

        if ( this.objectType instanceof ConfigurableObjectType )
        {
            try
            {
                ((ConfigurableObjectType)this.objectType).configure( config );
            }
            catch (ConfigurationException e)
            {
                throw new SAXParseException( "configuration exception",
                                             getLocator(),
                                             e );
            }
        }
        
        this.objectType = null;
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
        Class extractorClass = module.getExtractor( localName );

        if ( ! Extractor.class.isAssignableFrom( extractorClass ) )
        {
            throw new SAXParseException( extractorClass.getName() + " is not a valid extractor",
                                         getLocator() );
        }
        
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

        try
        {
            Extractor extractor = (Extractor) extractorClass.newInstance();

            this.extraction = new Extraction( targetDecl,
                                              extractor );

            startConfiguration( localName,
                                attrs );
        }
        catch (InstantiationException e)
        {
            throw new SAXParseException( "instantiation exception '" + extractorClass.getName() + "'",
                                         getLocator(),
                                         e );
        }
        catch (IllegalAccessException e)
        {
            throw new SAXParseException( "illegal access exception '" + extractorClass.getName() + "'",
                                         getLocator(),
                                         e );
        }
    }

    /** End an extraction.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endExtraction()
        throws SAXException
    {
        this.rule.addExtraction( this.extraction );

        Configuration config = endConfiguration();

        if ( this.extraction.getExtractor() instanceof ConfigurableExtractor )
        {
            try
            {
                ((ConfigurableExtractor)this.extraction.getExtractor()).configure( config,
                                                                                  this.rule.getAllDeclarations() );
            }
            catch (ConfigurationException e)
            {
                e.printStackTrace();
                throw new SAXParseException( "configuration exception",
                                             getLocator(),
                                             e );
            }
        }

        this.extraction = null;
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
        // System.err.println( "startCondition(" + name + ", ...) " + this.configurationStack.size() );
        Class conditionClass = module.getCondition( name );

        if ( ! Condition.class.isAssignableFrom( conditionClass ) )
        {
            throw new SAXParseException( conditionClass.getName() + " is not a valid condition",
                                         getLocator() );
        }

        try
        {
            this.condition = (Condition) conditionClass.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new SAXParseException( "instantiation exception '" + conditionClass.getName() + "'",
                                         getLocator(),
                                         e );
        }
        catch (IllegalAccessException e)
        {
            throw new SAXParseException( "illegal access exception '" + conditionClass.getName() + "'",
                                         getLocator(),
                                         e );
        }

        startConfiguration( name,
                            attrs );
    }

    /** End a condition.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endCondition()
        throws SAXException
    {
        // System.err.println( "endCondition() " + this.configurationStack.size() );
        this.rule.addCondition( this.condition );

        Configuration config = endConfiguration();

        if ( this.condition instanceof ConfigurableCondition )
        {
            try
            {
                ((ConfigurableCondition)this.condition).configure( config,
                                                                   this.rule.getAllDeclarations() );
            }
            catch (ConfigurationException e)
            {
                e.printStackTrace();
                throw new SAXParseException( "configuration exception",
                                             getLocator(),
                                             e );
            }
        }

        this.condition = null;
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
        // System.err.println( "startConsequence(" + name + ", ...) " + this.configurationStack.size() );
        Class consequenceClass = module.getConsequence( name );

        if ( ! Consequence.class.isAssignableFrom( consequenceClass ) )
        {
            throw new SAXParseException( consequenceClass.getName() + " is not a valid consequence",
                                         getLocator() );
        }

        try
        {
            this.consequence = (Consequence) consequenceClass.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new SAXParseException( "instantiation exception '" + consequenceClass.getName() + "'",
                                         getLocator(),
                                         e );
        }
        catch (IllegalAccessException e)
        {
            throw new SAXParseException( "illegal access exception '" + consequenceClass.getName() + "'",
                                         getLocator(),
                                         e );
        }

        startConfiguration( name,
                            attrs );
    }

    /** End a consequence.
     *
     *  @throws SAXException If an error occurs during parse.
     */
    protected void endConsequence()
        throws SAXException
    {
        // System.err.println( "endConsequence() " + this.configurationStack.size() );
        this.rule.setConsequence( this.consequence );

        Configuration config = endConfiguration();

        if ( this.consequence instanceof ConfigurableConsequence )
        {
            try
            {
                ((ConfigurableConsequence)this.consequence).configure( config,
                                                                       this.rule.getAllDeclarations() );
            }
            catch (ConfigurationException e)
            {
                throw new SAXParseException( "configuration exception",
                                             getLocator(),
                                             e );
            }
        }

        this.consequence = null;
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
        // System.err.println( "startConfiguration(" + name + ",...) " + this.configurationStack.size() );
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

        //this.configurationStack.addLast( config );
    }

    /** @see org.xml.sax.ContentHandler
     */
    public void characters(char[] chars,
                           int start,
                           int len)
        throws SAXException
    {
        // System.err.println( "chars: " + new String( chars, start, len ) + " // " + ( this.characters != null ) );
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
        // System.err.println( "endConfiguration() " + this.configurationStack.size() );
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
