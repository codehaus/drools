/*
 * Created on 30/05/2005
 */
package org.drools.examples.decisiontables.model;


/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * Represents a team to allocate the claim to.
 */
public class Team {
		
		
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	private String name;
	

}
