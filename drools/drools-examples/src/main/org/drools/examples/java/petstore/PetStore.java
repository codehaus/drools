package org.drools.examples.java.petstore;

import java.net.URL;
import java.util.Vector;

import org.drools.RuleBase;
import org.drools.io.RuleBaseBuilder;

public class PetStore
{
	public static void main( String[] args )
	{
		try
		{
			URL url = PetStore.class.getResource( "petstore.drl" );
			RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( url );

			Vector stock = new Vector();
			stock.add( new CartItem( "Gold Fish", 5 ) );
			stock.add( new CartItem( "Fish Tank", 25 ) );
			stock.add( new CartItem( "Fish Food", 2 ) );

			//The callback is responsible for populating working memory and
			// fireing all rules
			PetStoreUI ui = new PetStoreUI( stock, new CheckoutCallback(
					ruleBase ) );
			ui.createAndShowGUI();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

}