package org.drools.io;

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
import org.drools.smf.ConfigurableConsequence;
import org.drools.smf.ConfigurationException;
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

public class RuleSetReader
    extends DefaultHandler
{
    public static final String RULES_NAMESPACE_URI = "http://drools.org/rules";

    private SAXParser parser;
    private Locator locator;

    private RuleSet ruleSet;
    private Rule rule;
    private Declaration declaration;
    private ObjectType objectType;
    private Extraction extraction;
    private Condition condition;
    private Consequence consequence;

    private LinkedList configurationStack;
    private StringBuffer characters;

    private Map starters;
    private Map enders;

    private SemanticsRepository repo;

    public RuleSetReader(SemanticsRepository repo,
                         SAXParser parser)
    {
        this( repo );

        this.parser = parser;
    }

    public RuleSetReader(SemanticsRepository repo)
    {
        this.repo               = repo;
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

    public RuleSet read(URL url)
        throws Exception
    {
        return read( new InputSource( url.toExternalForm() ) );
    }

    public RuleSet read(Reader reader)
        throws Exception
    {
        return read( new InputSource( reader ) );
    }

    public RuleSet read(InputStream inputStream)
        throws Exception
    {
        return read( new InputSource( inputStream ) );
    }

    public RuleSet read(String url)
        throws Exception
    {
        return read( new InputSource( url ) );
    }

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

        parser.parse( in,
                      this );

        return this.ruleSet;
    }

    public void setLocator(Locator locator)
    {
        this.locator = locator;
    }

    public Locator getLocator()
    {
        return this.locator;
    }

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

    protected void endRuleSet()
        throws SAXException
    {
        // nothing
    }
    
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

    protected void startParameter(Attributes attrs)
        throws SAXException
    {
        startParameterOrDeclaration( "parameter",
                                     attrs );
    }

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

    protected void startDeclaration(Attributes attrs)
        throws SAXException
    {
        startParameterOrDeclaration( "declaration",
                                     attrs );
    }

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

    protected void startExtraction(String uri,
                                   String localName,
                                   Attributes attrs)
        throws SAXException
    {

    }

    protected void endExtraction()
        throws SAXException
    {

    }

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

    protected Configuration endConfiguration()
    {
        // System.err.println( "endConfiguration() " + this.configurationStack.size() );
        DefaultConfiguration config = (DefaultConfiguration) this.configurationStack.removeLast();
        config.setText( this.characters.toString() );
        this.characters = null;
        return config;
    }

    Starter lookupStarter(String uri,
                          String name)
    {
        return (Starter) this.starters.get( uri + ">" + name );
    }

    void bindStarter(String uri,
                     String name,
                     Starter starter)
    {
        this.starters.put( uri + ">" + name,
                           starter );
    }

    Ender lookupEnder(String uri,
                      String name)
    {
        return (Ender) this.enders.get( uri + ">" + name );
    }

    void bindEnder(String uri,
                   String name,
                   Ender ender)
    {
        this.enders.put( uri + ">" + name,
                         ender );
    }

    static abstract class Starter
    {
        abstract void start(Attributes attrs)
            throws SAXException;
    }

    static abstract class Ender
    {
        abstract void end()
            throws SAXException;
    }
}
