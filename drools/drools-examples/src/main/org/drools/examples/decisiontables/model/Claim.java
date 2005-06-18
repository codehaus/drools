/*
 * Created on 30/05/2005
 */
package org.drools.examples.decisiontables.model;

import java.util.Date;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * Represents a claim to be processed. Need to work out which team to allocate it to.
 * That is what the ruleset is for.
 */
public class Claim {
	
	/**
	 * @return Returns the allocationCode.
	 */
	public String getAllocationCode() {
		return allocationCode;
	}
	/**
	 * @param allocationCode The allocationCode to set.
	 */
	public void setAllocationCode(String allocationCode) {
		this.allocationCode = allocationCode;
	}
	/**
	 * @return Returns the catastrophic.
	 */
	public boolean isCatastrophic() {
		return catastrophic;
	}
	/**
	 * @param catastrophic The catastrophic to set.
	 */
	public void setCatastrophic(boolean catastrophic) {
		this.catastrophic = catastrophic;
	}
	/**
	 * @return Returns the claimType.
	 */
	public String getClaimType() {
		return claimType;
	}
	/**
	 * @param claimType The claimType to set.
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	/**
	 * @return Returns the dateOfAccident.
	 */
	public Date getDateOfAccident() {
		return dateOfAccident;
	}
	/**
	 * @param dateOfAccident The dateOfAccident to set.
	 */
	public void setDateOfAccident(Date dateOfAccident) {
		this.dateOfAccident = dateOfAccident;
	}
	/**
	 * @return Returns the insuranceClass.
	 */
	public String getInsuranceClass() {
		return insuranceClass;
	}
	/**
	 * @param insuranceClass The insuranceClass to set.
	 */
	public void setInsuranceClass(String insuranceClass) {
		this.insuranceClass = insuranceClass;
	}
	/**
	 * @return Returns the insuredVehicleOwner.
	 */
	public String getInsuredVehicleOwner() {
		return insuredVehicleOwner;
	}
	/**
	 * @param insuredVehicleOwner The insuredVehicleOwner to set.
	 */
	public void setInsuredVehicleOwner(String insuredVehicleOwner) {
		this.insuredVehicleOwner = insuredVehicleOwner;
	}
	private boolean catastrophic;
	private String allocationCode;
	private String claimType;
	private String insuranceClass;
	private Date dateOfAccident;
	private String insuredVehicleOwner;
	

}
