package org.drools.examples.java.petstore;

public class CartItem
{
	private String name;
	private double cost;

	public CartItem( String name, double cost )
	{
		this.name = name;
		this.cost = cost;
	}

	public String getName()
	{
		return this.name;
	}

	public double getCost()
	{
		return this.cost;
	}

	public String toString()
	{
		return name + " " + this.cost;
	}

}