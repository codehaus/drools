package org.drools.examples.house;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;


public class HouseExample
{
    public static void main(String[] args)
    {
        try
        {
            Room lounge = new Room("lounge");
            Room bedroom = new Room("bedroom");
            Room bathroom = new Room("bathroom");
            Room kitchen = new Room("kitchen");
            
            Heating heating = new Heating();            
            
            RuleBase ruleBase = RuleBaseLoader.loadFromUrl( HouseExample.class.getResource( "house.drl" ) );
            // Dumper dumper = new Dumper(ruleBase);
            // dumper.dumpRete(System.err);

            WorkingMemory workingMemory;

            System.err.println( "\nFirst run - code compiled on the fly" );
            workingMemory = ruleBase.newWorkingMemory( );                        
            
            lounge.setTemperature(292);
            bedroom.setTemperature(292);

            FactHandle loungeHandle = workingMemory.assertObject( lounge );
            FactHandle bedroomHandle = workingMemory.assertObject( bedroom );
            FactHandle heatingHandle = workingMemory.assertObject( heating );            
            workingMemory.fireAllRules( );

            lounge.setTemperature(295);
            bedroom.setTemperature(295);

            workingMemory.modifyObject( loungeHandle, lounge );
            workingMemory.modifyObject( bedroomHandle, bedroom );            
            workingMemory.fireAllRules( );            
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
