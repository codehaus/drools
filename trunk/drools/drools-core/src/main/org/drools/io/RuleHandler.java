package org.drools.io;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.rule.Declaration;
import org.drools.rule.DuplicateRuleNameException;
import org.drools.rule.InvalidRuleException;
import org.drools.smf.SemanticModule;
import org.drools.smf.ConfigurableObjectType;
import org.drools.smf.ConfigurableCondition;
import org.drools.smf.ConfigurableConsequence;
import org.drools.smf.NoSuchSemanticModuleException;
import org.drools.smf.ConfigurationException;
import org.drools.spi.ObjectType;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;

import org.dom4j.Element;
import org.dom4j.ElementPath;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class RuleHandler
    extends AbstractHandler
{
    private RuleSetHandler ruleSetHandler;
    private List declarations;

    RuleHandler(RuleSetHandler ruleSetHandler)
    {
        this.ruleSetHandler = ruleSetHandler;
        this.declarations   = new ArrayList();
    }

    RuleSetHandler getRuleSetHandler()
    {
        return this.ruleSetHandler;
    }

    RuleSet getRuleSet()
    {
        return getRuleSetHandler().getRuleSet();
    }

    Declaration[] getAvailableDeclarations()
    {
        return (Declaration[]) this.declarations.toArray( Declaration.EMPTY_ARRAY );
    }

    public void onEnd(ElementPath path)
    {
        Element elem = path.getCurrent();

        if ( ! elem.getNamespace().getURI().equals( RuleSetReader.RULES_NAMESPACE ) )
        {
            addError( new SyntaxException( "<rule> must be in namespace '" + RuleSetReader.RULES_NAMESPACE + "'" ) );
        }
        else
        {
            String ruleName = elem.attributeValue( "name" );
            
            Rule rule = new Rule( ruleName );
            
            for ( Iterator elemIter = elem.elementIterator();
                  elemIter.hasNext(); )
            {
                try
                {
                    add( rule,
                         (Element) elemIter.next() );
                }
                catch (Exception e)
                {
                    addError( e );
                }
            }
            
            try
            {
                getRuleSet().addRule( rule );
            }
            catch (DuplicateRuleNameException e)
            {
                addError( e );
            }
            catch (InvalidRuleException e)
            {
                addError( e );
            }
        }
    }

    void add(Rule rule,
             Element elem)
        throws NoSuchSemanticModuleException, InstantiationException, ConfigurationException, IllegalAccessException
    {
        String nsUri = elem.getNamespaceURI();
        String name  = elem.getName();

        SemanticModule module = lookupSemanticModule( nsUri );

        if ( nsUri.equals( RuleSetReader.RULES_NAMESPACE ) )
        {
            if ( "parameter".equals( name ) )
            {
                addParameterDeclaration( rule,
                                         module,
                                         elem );
            }
            else if ( "declaration".equals( name ) )
            {
                addDeclaration( rule,
                                module,
                                elem );
            }
        }
        else if ( module.getConditionNames().contains( name ) )
        {
            addCondition( rule,
                          module.getCondition( name ),
                          elem  );
        }
        else if ( module.getExtractorNames().contains( name ) )
        {
            addExtraction( rule,
                           module.getExtractor( name ),
                           elem  );
        }
        else if ( module.getConsequenceNames().contains( name ) )
        {
            addConsequence( rule,
                            module.getConsequence( name ),
                            elem );
        }
        else
        {
            addError( new SyntaxException( "<" + elem.getName() + "> in namespace '" + elem.getNamespace().getURI() + "' is unknown" ) );
        }
    }

    void addParameterDeclaration(Rule rule,
                                 SemanticModule module,
                                 Element elem)
        throws InstantiationException, ConfigurationException, IllegalAccessException
    {
        String identifier = elem.attributeValue( "identifier" );

        Element objectTypeElem = (Element) elem.elements().get( 0 );

        ObjectType objectType = parseObjectType( rule,
                                                 module,
                                                 objectTypeElem );

        if ( objectType == null )
        {
            addError( new SyntaxException( "unable to handle <" + objectTypeElem.getName() + "> in namespace '" + objectTypeElem.getNamespace().getURI() + "'" ) );
            return;
        }

        Declaration decl = new Declaration( objectType,
                                            identifier );

        rule.addParameterDeclaration( decl );
    }

    void addDeclaration(Rule rule,
                        SemanticModule module,
                        Element elem)
        throws InstantiationException, ConfigurationException, IllegalAccessException
    {
        String identifier = elem.attributeValue( "identifier" );

        Element objectTypeElem = (Element) elem.elements().get( 0 );

        ObjectType objectType = parseObjectType( rule,
                                                 module,
                                                 objectTypeElem );

        if ( objectType == null )
        {
            addError( new SyntaxException( "unable to handle <" + objectTypeElem.getName() + "> in namespace '" + objectTypeElem.getNamespace().getURI() + "'" ) );
            return;
        }

        Declaration decl = new Declaration( objectType,
                                            identifier );

        rule.addDeclaration( decl );
    }

    ObjectType parseObjectType(Rule rule,
                               SemanticModule module,
                               Element elem)
        throws InstantiationException, ConfigurationException, IllegalAccessException
    {
        String objectTypeName = elem.getName();

        Class objectTypeClass = module.getObjectType( objectTypeName );

        if ( objectTypeClass == null )
        {
            addError( new SyntaxException( "no object type for <" + elem.getName() + "> for namespace '" + elem.getNamespace().getURI() + "'" ) );
            return null;
        }

        if ( ! ObjectType.class.isAssignableFrom( objectTypeClass ) )
        {
            addError( new SyntaxException( "'" + objectTypeClass.getName() + "' is not a valid ObjectType subclass" ) );
            return null;
        }

        ObjectType objectType = (ObjectType) objectTypeClass.newInstance();

        if ( objectType instanceof ConfigurableObjectType )
        {
            ((ConfigurableObjectType)objectType).configure( elem.getText() );
        }

        return objectType;
    }

    void addCondition(Rule rule,
                      Class conditionClass,
                      Element elem)
        throws InstantiationException, ConfigurationException, IllegalAccessException
    {
        if ( conditionClass == null )
        {
            addError( new SyntaxException( "no condition for <" + elem.getName() + "> for namespace '" + elem.getNamespace().getURI() + "'" ) );
            return;
        }

        if ( ! Condition.class.isAssignableFrom( conditionClass ) )
        {
            addError( new SyntaxException( "'" + conditionClass.getName() + "' is not a valid Condition subclass" ) );
            return;
        }

        Condition condition = (Condition) conditionClass.newInstance();

        if ( condition instanceof ConfigurableCondition )
        {
            ((ConfigurableCondition)condition).configure( elem.getText(),
                                                          getAvailableDeclarations() );
        }

        rule.addCondition( condition );
    }


    void addExtraction(Rule rule,
                       Class componentClass,
                       Element elem)
        throws InstantiationException, ConfigurationException, IllegalAccessException
    {

    }

    void addConsequence(Rule rule,
                        Class consequenceClass,
                        Element elem)
        throws InstantiationException, ConfigurationException, IllegalAccessException
    {
        if ( consequenceClass == null )
        {
            addError( new SyntaxException( "no consequence for <" + elem.getName() + "> for namespace '" + elem.getNamespace().getURI() + "'" ) );
            return;
        }

        if ( rule.getConsequence() != null )
        {
            addError( new SyntaxException( rule.getName() + " already has a consequence" ) );
            return;
        }

        if ( ! Consequence.class.isAssignableFrom( consequenceClass ) )
        {
            addError( new SyntaxException( "'" + consequenceClass.getName() + "' is not a valid Consequence subclass" ) );
            return;
        }

        Consequence consequence = (Consequence) consequenceClass.newInstance();

        if ( consequence instanceof ConfigurableConsequence )
        {
            ((ConfigurableConsequence)consequence).configure( elem.getText(),
                                                              getAvailableDeclarations() );
        }

        rule.setConsequence( consequence );
    }

    SemanticModule lookupSemanticModule(String nsUri)
        throws NoSuchSemanticModuleException
    {
        return getRuleSetHandler().lookupSemanticModule( nsUri );
    }

    void addError(Throwable error)
    {
        getRuleSetHandler().addError( error );
    }
}
