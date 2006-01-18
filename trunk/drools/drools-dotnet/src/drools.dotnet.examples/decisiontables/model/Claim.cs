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
* INTERRU`PTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*
*/
using System;
namespace org.drools.dotnet.examples.decisiontables.model
{
	
	/// <author>  <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
	/// 
	/// Represents a claim to be processed. Need to work out which team to allocate it to.
	/// That is what the ruleset is for.
	/// </author>
	public class Claim
	{
		//UPGRADE_NOTE: Respective javadoc comments were merged.  It should be changed in order to comply with .NET documentation conventions.
		/// <returns> Returns the allocationCode.
		/// </returns>
		/// <param name="allocationCode">The allocationCode to set.
		/// </param>
		virtual public System.String AllocationCode
		{
			get
			{
                
				return allocationCode;
			}
			
			set
			{
				this.allocationCode = value;
			}
			
		}
		//UPGRADE_NOTE: Respective javadoc comments were merged.  It should be changed in order to comply with .NET documentation conventions.
		/// <returns> Returns the catastrophic.
		/// </returns>
		/// <param name="catastrophic">The catastrophic to set.
		/// </param>
		virtual public bool Catastrophic
		{
			get
			{
				return catastrophic;
			}
			
			set
			{
				this.catastrophic = value;
			}
			
		}
		//UPGRADE_NOTE: Respective javadoc comments were merged.  It should be changed in order to comply with .NET documentation conventions.
		/// <returns> Returns the claimType.
		/// </returns>
		/// <param name="claimType">The claimType to set.
		/// </param>
		virtual public System.String ClaimType
		{
			get
			{
				return claimType;
			}
			
			set
			{
				this.claimType = value;
			}
			
		}
		//UPGRADE_NOTE: Respective javadoc comments were merged.  It should be changed in order to comply with .NET documentation conventions.
		/// <returns> Returns the dateOfAccident.
		/// </returns>
		/// <param name="dateOfAccident">The dateOfAccident to set.
		/// </param>
		virtual public System.DateTime DateOfAccident
		{
			get
			{
				return dateOfAccident;
			}
			
			set
			{
				this.dateOfAccident = value;
			}
			
		}
		//UPGRADE_NOTE: Respective javadoc comments were merged.  It should be changed in order to comply with .NET documentation conventions.
		/// <returns> Returns the insuranceClass.
		/// </returns>
		/// <param name="insuranceClass">The insuranceClass to set.
		/// </param>
		virtual public System.String InsuranceClass
		{
			get
			{
				return insuranceClass;
			}
			
			set
			{
				this.insuranceClass = value;
			}
			
		}
		//UPGRADE_NOTE: Respective javadoc comments were merged.  It should be changed in order to comply with .NET documentation conventions.
		/// <returns> Returns the insuredVehicleOwner.
		/// </returns>
		/// <param name="insuredVehicleOwner">The insuredVehicleOwner to set.
		/// </param>
		virtual public System.String InsuredVehicleOwner
		{
			get
			{
				return insuredVehicleOwner;
			}
			
			set
			{
				this.insuredVehicleOwner = value;
			}
			
		}
		private bool catastrophic;
		private System.String allocationCode;
		private System.String claimType;
		private System.String insuranceClass;
		private System.DateTime dateOfAccident;
		private System.String insuredVehicleOwner;
	}
}