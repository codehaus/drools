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

    public Functions newFunctions(RuleSet ruleSet,
                                  RuleBaseContext context,
                                  Configuration config) throws FactoryException
    {
        try
        {
            Integer id = (Integer) context.get( "java-functions-id" );
            if ( id == null )
            {
                id = new Integer( 0 );
            }
            context.put( "java-functions-id",
                         new Integer( id.intValue( ) + 1 ) );  
            
            return new JavaFunctions( ruleSet,
                                      id.intValue( ),
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