package org.drools.io;

import java.util.HashSet;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.smf.ConditionFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.SemanticModule;
import org.drools.spi.Condition;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author mproctor
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
class ConditionHandler extends BaseAbstractHandler
    implements
    Handler
{
    ConditionHandler(RuleSetReader ruleSetReader)
    {
        this.ruleSetReader = ruleSetReader;

        if ( (this.validParents == null) && (validPeers == null) )
        {
            this.ruleSetReader = ruleSetReader;
            this.validParents = new HashSet( );
            this.validParents.add( Rule.class );

            this.validPeers = new HashSet( );
            this.validPeers.add( Declaration.class );
            this.validPeers.add( Condition.class );
            this.validPeers.add( null );

            this.allowNesting = false;
        }
    }

    public Object start(String uri,
                        String localName,
                        Attributes attrs) throws SAXException
    {
        ruleSetReader.startConfiguration( localName,
                                          attrs );
        return null;
    }

    public Object end(String uri,
                      String localName) throws SAXException
    {
        Configuration config = this.ruleSetReader.endConfiguration( );
        SemanticModule module = this.ruleSetReader.lookupSemanticModule( uri,
                                                                         localName );

        ConditionFactory factory = module.getConditionFactory( localName );
        Condition[] conditions;
        try
        {
            Rule rule = (Rule) this.ruleSetReader.getParent( Rule.class );
            conditions = factory.newCondition( rule,
                                              this.ruleSetReader.getFactoryContext( ),
                                              config );

            for (int i = 0; i < conditions.length; i++)
            {
                rule.addCondition( conditions[i] );
            }
        }
        catch ( FactoryException e )
        {		
            throw new SAXParseException( "error constructing condition",
                                         this.ruleSetReader.getLocator( ),
                                         e );
        }
        return conditions[conditions.length-1];
    }

    public Class generateNodeFor()
    {
        return Condition.class;
    }
}
