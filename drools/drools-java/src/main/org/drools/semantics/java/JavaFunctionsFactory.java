package org.drools.semantics.java;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureClassLoader;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.FunctionsFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Functions;

import net.janino.ClassLoaderIClassLoader;
import net.janino.Java.CompileException;
import net.janino.Parser.ParseException;
import net.janino.Scanner.ScanException;

public class JavaFunctionsFactory implements FunctionsFactory
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
            return new JavaFunctions( ruleSet, config.getText( ) );
        }
        catch (IOException e)
        {
            throw new FactoryException(e);   
        }
        catch (ScanException e)
        {
            throw new FactoryException(e);
        }   
        catch (CompileException e)
        {
            throw new FactoryException(e);   
        }
        catch (ParseException e)
        {
            throw new FactoryException(e);
        }          
    }
}