package org.drools.examples.java.petstore;

import java.util.List;
import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;

import javax.swing.JFrame;
/**
 * @author mproctor
 *
 * This callback is called when the user pressed the checkout button. It is
 * responsible for adding the items to the shopping cart, asserting the shopping
 * cart and then firing all rules.
 *
 * A reference to the JFrame is also passed so the rules can launch dialog boxes
 * for user interaction. It uses the ApplicationData feature for this.
 *
 */
public class CheckoutCallback
{
	RuleBase ruleBase;
	public CheckoutCallback( RuleBase ruleBase )
	{
		this.ruleBase = ruleBase;
	}

	/**
	 * Populate the cart and assert into working memory Pass Jframe reference
	 * for user interaction
	 *
	 * @param frame
	 * @param items
	 * @return cart.toString();
	 */
	public String checkout( JFrame frame, List items ) throws FactException
	{
		ShoppingCart cart = new ShoppingCart();

		//Iterate through list and add to cart
		for ( int i = 0; i < items.size(); i++ )
		{
			cart.addItem( (CartItem) items.get( i ) );
		}

		//add the JFrame to the ApplicationData to allow for user interaction
		WorkingMemory workingMemory = ruleBase.newWorkingMemory();
		workingMemory.setApplicationData( "frame", frame );
		workingMemory.assertObject( cart );
		workingMemory.fireAllRules();

		//returns the state of the cart
		return cart.toString();
	}
}