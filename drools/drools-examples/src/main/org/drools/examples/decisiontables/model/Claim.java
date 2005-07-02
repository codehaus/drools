package org.drools.examples.decisiontables.model;


/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
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

