package org.drools.examples.java.petstore;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class ShoppingCart
{
	private List items;

	private double discount;

	private Map states;

	private static String newline = System.getProperty("line.separator");

	public ShoppingCart()
	{
		states = new HashMap();
		this.items = new ArrayList();
		this.discount = 0;
	}

	public boolean getState( String state )
	{
		if ( states.containsKey( state ) )
		{
			return ((Boolean) states.get( state )).booleanValue();
		} else
		{
			return false;
		}
	}

	public void setState( String state, boolean value )
	{
		states.put( state, new Boolean( value ) );
	}

	public void setDiscount( double discount )
	{
		this.discount = discount;
	}

	public double getDiscount()
	{
		return this.discount;
	}

	public void addItem( CartItem item )
	{
		this.items.add( item );
	}

	public List getItems()
	{
		return this.items;
	}

	public List getItems( String name )
	{
		ArrayList matching = new ArrayList();

		Iterator itemIter = getItems().iterator();
		CartItem eachItem = null;

		while ( itemIter.hasNext() )
		{
			eachItem = (CartItem) itemIter.next();

			if ( eachItem.getName().equals( name ) )
			{
				matching.add( eachItem );
			}
		}

		return matching;
	}

	public double getGrossCost()
	{
		Iterator itemIter = getItems().iterator();
		CartItem eachItem = null;

		double cost = 0.00;

		while ( itemIter.hasNext() )
		{
			eachItem = (CartItem) itemIter.next();

			cost += eachItem.getCost();
		}

		return cost;
	}

	public double getDiscountedCost()
	{
		double cost = getGrossCost();
		double discount = getDiscount();

		double discountedCost = cost * (1 - discount);

		return discountedCost;
	}

	public String toString()
	{
		StringBuffer buf = new StringBuffer();

		buf.append( "ShoppingCart:" + newline );

		Iterator itemIter = getItems().iterator();

		while ( itemIter.hasNext() )
		{
			buf.append( "\t" + itemIter.next() + newline );
		}

		buf.append( "gross total=" + getGrossCost() + newline );
		buf.append( "discounted total=" + getDiscountedCost() + newline );

		return buf.toString();
	}
}