package org.drools.examples.petstore;

/*
 * $Id: PetStore.java,v 1.6 2005-11-25 02:35:33 mproctor Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
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

import java.net.URL;
import java.util.Vector;

import org.drools.RuleBase;
import org.drools.examples.helloworld.HelloWorldExample;
import org.drools.io.RuleBaseLoader;
import org.drools.io.RuleSetLoader;

public class PetStore
{
    public static void main(String[] args)
    {
        if ( args.length != 1 )
        {
            System.out.println( "Usage: " + PetStore.class.getName( )
                                + " [drl file]" );
            return;
        }
        System.out.println( "Using drl: " + args[0] );

        try
        {
            RuleSetLoader ruleSetLoader = new RuleSetLoader();           
            ruleSetLoader.addFromUrl( PetStore.class.getResource( args[0] ) );            
            
            RuleBaseLoader ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
            RuleBase ruleBase = ruleBaseLoader.buildRuleBase();                  
            

            Vector stock = new Vector( );
            stock.add( new CartItem( "Gold Fish", 5 ) );
            stock.add( new CartItem( "Fish Tank", 25 ) );
            stock.add( new CartItem( "Fish Food", 2 ) );

            //The callback is responsible for populating working memory and
            // fireing all rules
            PetStoreUI ui = new PetStoreUI( stock,
                                            new CheckoutCallback( ruleBase ) );
            ui.createAndShowGUI( );
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
    }

}