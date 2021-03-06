package org.drools.examples.house;

/*
 * $Id: HouseExample.java,v 1.4 2005-11-25 02:35:33 mproctor Exp $
 *
 * Copyright 2005-2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.examples.helloworld.HelloWorldExample;
import org.drools.io.RuleBaseLoader;
import org.drools.io.RuleSetLoader;

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
            
            RuleSetLoader ruleSetLoader = new RuleSetLoader();           
            ruleSetLoader.addFromUrl( HouseExample.class.getResource( "house.drl" ) );            
            
            RuleBaseLoader ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
            RuleBase ruleBase = ruleBaseLoader.buildRuleBase();            
            
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
