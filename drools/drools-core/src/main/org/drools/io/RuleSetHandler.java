package org.drools.io;

import org.drools.rule.RuleSet;
import org.drools.smf.SemanticModule;
import org.drools.smf.SemanticsRepository;
import org.drools.smf.NoSuchSemanticModuleException;

import org.dom4j.Element;
import org.dom4j.ElementPath;

import java.util.List;
import java.util.ArrayList;

class RuleSetHandler
    extends AbstractHandler
{
    private SemanticsRepository semanticsRepo;
    private RuleSet ruleSet;
    private List errors;

    RuleSetHandler(RuleSet ruleSet,
                   SemanticsRepository semanticsRepo,
                   List errors)
    {
        this.ruleSet       = ruleSet;
        this.semanticsRepo = semanticsRepo;
        this.errors        = errors;
    }

    SemanticsRepository getSemanticsRepository()
    {
        return this.semanticsRepo;
    }

    RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    public void onStart(ElementPath path)
    {
        Element elem = path.getCurrent();

        if ( ! elem.getNamespace().getURI().equals( RuleSetReader.RULES_NAMESPACE ) )
        {
            addError( new SyntaxException( "<rule-set> must be in namespace '" + RuleSetReader.RULES_NAMESPACE + "'" ) );
        }
        else
        {
            String ruleSetName = elem.attributeValue( "name" );
            
            this.ruleSet.setName( ruleSetName );
        }
    }

    SemanticModule lookupSemanticModule(String nsUri)
        throws NoSuchSemanticModuleException
    {
        return getSemanticsRepository().lookupSemanticModule( nsUri );
    }

    void addError(Throwable error)
    {
        this.errors.add( error );
    }

    List getErrors()
    {
        return this.errors;
    }
}
