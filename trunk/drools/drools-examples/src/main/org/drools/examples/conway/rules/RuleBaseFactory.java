/**
 *
 */
package org.drools.examples.conway.rules;

import org.drools.RuleBase;
import org.drools.examples.conway.CellGrid;
import org.drools.examples.helloworld.HelloWorldExample;
import org.drools.io.RuleBaseLoader;
import org.drools.io.RuleSetLoader;

import java.net.URL;

/**
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 * @version $Id: RuleBaseFactory.java,v 1.4 2005-11-25 02:35:33 mproctor Exp $
 */
public class RuleBaseFactory
{

    private static RuleBaseFactory ourInstance      = new RuleBaseFactory( );
    private static final String    DEFAULT_DRL_FILE = "conway.java.drl";

    private RuleBase               ruleBase;

    public static RuleBaseFactory getInstance()
    {
        return ourInstance;
    }

    private RuleBaseFactory()
    {
        try
        {
            String conwayDrlFile = System.getProperty( "conway.drl.file" );
            if ( conwayDrlFile == null )
            {
                System.err.println( "conway.drl.file system property not specified. using default: " + DEFAULT_DRL_FILE );
                conwayDrlFile = DEFAULT_DRL_FILE;
            }
            System.out.println( "loading drl file: " + conwayDrlFile );
            
            RuleSetLoader ruleSetLoader = new RuleSetLoader();           
            ruleSetLoader.addFromUrl( CellGrid.class.getResource( conwayDrlFile ) );            
            
            RuleBaseLoader ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
            ruleBase = ruleBaseLoader.buildRuleBase();                
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Unable To Initialise RuleBaseFactory:\n" + e.getMessage() );
        }
    }

    public static RuleBase getRuleBase()
    {
        return ourInstance.ruleBase;
    }
}
