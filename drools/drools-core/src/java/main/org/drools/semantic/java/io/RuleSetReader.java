
package org.drools.semantic.java.io;

import org.drools.spi.RuleSet;
import org.drools.spi.Rule;
import org.drools.spi.Declaration;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

import java.util.Map;
import java.util.HashMap;

public class RuleSetReader
{
    private SAXReader reader;

    private ImportManager importManager;
    private RuleSet       ruleSet;
    private Rule          currentRule;

    private Map           decls;

    public RuleSetReader()
    {
        this.importManager = new ImportManager();
        this.reader        = new SAXReader();

        this.reader.addHandler( "/ruleset",
                                new RuleSetHandler( this ) );

        this.reader.addHandler( "/ruleset/import",
                                new ImportHandler( this ) );

        this.reader.addHandler( "/ruleset/rule",
                                new RuleHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/param",
                                new ParamHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/decl",
                                new DeclHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/when/cond",
                                new CondHandler( this ) );

        this.reader.addHandler( "/ruleset/rule/then",
                                new ThenHandler( this ) );

        this.decls = new HashMap();
    }

    void addDeclaration(Declaration decl)
    {
        this.decls.put( decl.getIdentifier(),
                             decl );
    }

    Declaration getDeclaration(String identifier)
    {
        return (Declaration) this.decls.get( identifier );
    }

    Rule getCurrentRule()
    {
        return this.currentRule;
    }

    void setCurrentRule(Rule currentRule)
    {
        this.currentRule = currentRule;
    }

    ImportManager getImportManager()
    {
        return this.importManager;
    }

    void setRuleSet(RuleSet ruleSet)
    {
        this.ruleSet = ruleSet;
    }

    RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    public RuleSet read(URL url) throws RuleSetReaderException
    {
        getImportManager().reset();

        setCurrentRule( null );
        setRuleSet( null );

        try
        {
            Document doc = this.reader.read( url );
        }
        catch (DocumentException e)
        {
            throw new RuleSetReaderException( e );
        }

        return getRuleSet();
    }

    public RuleSet read(File file) throws MalformedURLException, RuleSetReaderException
    {
        return read( file.toURL() );
    }
}
