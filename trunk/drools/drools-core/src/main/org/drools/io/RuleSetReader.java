package org.drools.io;

import org.drools.rule.RuleSet;
import org.drools.smf.SemanticModule;
import org.drools.smf.SemanticsRepository;
import org.drools.smf.SimpleSemanticsRepository;
import org.drools.smf.NoSuchSemanticModuleException;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.Reader;

import java.util.List; 
import java.util.ArrayList; 

public class RuleSetReader
    implements SemanticsRepository 
{
    public static final String RULES_NAMESPACE = "http://drools.org/rules";

    private static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];

    private SemanticsRepository semanticsRepo;

    public RuleSetReader()
    {
        this.semanticsRepo = new SimpleSemanticsRepository();
    }

    public void registerSemanticModule(SemanticModule module)
    {
        this.semanticsRepo.registerSemanticModule( module );
    }

    public SemanticModule lookupSemanticModule(String uri)
        throws NoSuchSemanticModuleException
    {
        return this.semanticsRepo.lookupSemanticModule( uri );
    }

    public RuleSet read(InputStream in)
        throws ReaderMultiException
    {
        RuleSet ruleSet = new RuleSet( "" );
        List    errors  = new ArrayList();

        SAXReader reader = initSaxReader( ruleSet,
                                          errors );

        try
        {
            reader.read( in );
        }
        catch (DocumentException e)
        {
            errors.add( e );
        }

        if ( errors.isEmpty() )
        {
            return ruleSet;
        }

        return handle( ruleSet,
                       errors );
    }

    public RuleSet read(Reader in)
        throws ReaderMultiException
    {
        RuleSet ruleSet = new RuleSet( "" );
        List    errors  = new ArrayList();

        SAXReader reader = initSaxReader( ruleSet,
                                          errors );

        try
        {
            reader.read( in );
        }
        catch (DocumentException e)
        {
            errors.add( e );
        }

        if ( errors.isEmpty() )
        {
            return ruleSet;
        }

        return handle( ruleSet,
                       errors );
    }

    protected RuleSet handle(RuleSet ruleSet,
                             List errors)
        throws ReaderMultiException
    {
        if ( errors.isEmpty() )
        {
            return ruleSet;
        }

        throw new ReaderMultiException( (Throwable[]) errors.toArray( EMPTY_THROWABLE_ARRAY ) );
    }

    protected SAXReader initSaxReader(RuleSet ruleSet,
                                      List errors)
    {
        SAXReader reader = new SAXReader();

        RuleSetHandler ruleSetHandler = new RuleSetHandler( ruleSet,
                                                            this,
                                                            errors );

        RuleHandler    ruleHandler    = new RuleHandler( ruleSetHandler );

        reader.addHandler( "/rule-set",
                           ruleSetHandler );

        reader.addHandler( "/rule-set/rule",
                           ruleHandler );

        return reader;
    }
}
