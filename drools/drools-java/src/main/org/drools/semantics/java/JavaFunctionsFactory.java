package org.drools.semantics.java;

import java.io.IOException;

import org.codehaus.janino.Java.CompileException;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.FunctionsFactory;
import org.drools.spi.Functions;
import org.drools.spi.RuleBaseContext;

public class JavaFunctionsFactory
    implements
    FunctionsFactory
{

    private static final FunctionsFactory INSTANCE = new JavaFunctionsFactory( );

    public static FunctionsFactory getInstance()
    {
        return INSTANCE;
    }

    public Functions newFunctions(RuleSet ruleSet,
                                  RuleBaseContext context,
                                  Configuration config) throws FactoryException
    {
        try
        {
            return new JavaFunctions( ruleSet,
                                      config.getText( ) );
        }
        catch ( IOException e )
        {
            throw new FactoryException( e );
        }
        catch ( ScanException e )
        {
            throw new FactoryException( e );
        }
        catch ( CompileException e )
        {
            throw new FactoryException( e );
        }
        catch ( ParseException e )
        {
            throw new FactoryException( e );
        }
    }
}