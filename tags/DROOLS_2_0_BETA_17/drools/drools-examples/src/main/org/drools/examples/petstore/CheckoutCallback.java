package org.drools.examples.petstore;

/*
 $Id: CheckoutCallback.java,v 1.1 2004-07-07 04:45:22 dbarnett Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
 statements and notices.  Redistributions must also contain a
 copy of this document.

 2. Redistributions in binary form must reproduce the
 above copyright notice, this list of conditions and the
 following disclaimer in the documentation and/or other
 materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
 products derived from this Software without prior written
 permission of The Werken Company.  For written permission,
 please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
 nor may "drools" appear in their names without prior written
 permission of The Werken Company. "drools" is a trademark of
 The Werken Company.

 5. Due credit should be given to The Werken Company.
 (http://werken.com/)

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import java.util.List;

import javax.swing.JFrame;

import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
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
		ShoppingCart cart = new ShoppingCart( );

		//Iterate through list and add to cart
		for ( int i = 0; i < items.size( ); i++ )
		{
			cart.addItem( (CartItem) items.get( i ) );
		}

		//add the JFrame to the ApplicationData to allow for user interaction
		WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
		workingMemory.setApplicationData( "frame", frame );
		workingMemory.assertObject( cart );
		workingMemory.fireAllRules( );

		//returns the state of the cart
		return cart.toString( );
	}
}
