
package org.drools.semantic.java.io;

import org.drools.spi.RuleSet;
import org.drools.spi.Rule;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.net.MalformedURLException;

public class RuleSetReader
{
    private SAXReader reader;

    private ImportManager importManager;
    private RuleSet       ruleSet;
    private Rule          currentRule;

    public RuleSetReader()
    {
        this.reader        = new SAXReader();
        this.importManager = new ImportManager();
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

    public RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    public RuleSet read(URL url)
    {
        this.importManager.reset();

        return null;
    }

    public RuleSet read(File file) throws MalformedURLException
    {
        return read( file.toURL() );
    }
}
