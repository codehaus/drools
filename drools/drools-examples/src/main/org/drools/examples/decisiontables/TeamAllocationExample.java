/*
 * Created on 2/06/2005
 */
package org.drools.examples.decisiontables;



import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.drools.FactException;
import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.decisiontable.DecisionTableLoader;
import org.drools.examples.decisiontables.model.Claim;
import org.drools.examples.decisiontables.model.Team;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 *
 * Show off a more complex example.
 * Runs a few different scenarios through the same rule base, but only building the rule base once.
 * 
 */
public class TeamAllocationExample {
	
	private RuleBase _ruleBase;
	

	
	private void loadRuleBase() throws SAXException, IOException, IntegrationException {
		InputStream stream = this.getClass().getResourceAsStream("TeamAllocationExample.xls");
		//MN this is all you have to do to get decision tables working...
		_ruleBase = DecisionTableLoader.loadFromInputStream(stream);
	}


	public static void main(String[] args) throws Exception {
		
		TeamAllocationExample example = new TeamAllocationExample();
		example.loadRuleBase();	
		example.teamAllocationBasic();
		example.teamAllocationLongtail1();
		example.teamAllocationLongtail2();
	}	
	
	
	public void teamAllocationBasic() throws Exception {
				
		Claim cat = getCatastrophic();
		Team team = new Team();
		
		executeRules(cat, team);
		System.out.println(team.getName());
	}
	
	public void teamAllocationLongtail1() throws Exception {
		Claim claim = getBetweenSomeDateLongtail1();
		Team team = new Team();
				
		executeRules(claim, team);		
		System.out.println(team.getName());
	}
	
	public void teamAllocationLongtail2() throws Exception {
		Claim claim = getBetweenSomeDateLongtail2();
		Team team = new Team();

				
		executeRules(claim, team);
		
		System.out.println(team.getName());

	}
	
	

	private void executeRules(Claim claim, Team team) throws SAXException, IOException, IntegrationException, FactException {
		WorkingMemory engine = _ruleBase.newWorkingMemory();
		engine.assertObject(claim);
		engine.assertObject(team);
		engine.fireAllRules();
	}
	
	
	//set up the example scenarios below
	private Claim getBetweenSomeDateLongtail1() {
		Claim claim = new Claim();
		claim.setCatastrophic(false);
		Calendar cal = Calendar.getInstance();
		cal.set(2001, 1, 4);
		claim.setDateOfAccident(cal.getTime());
		claim.setInsuranceClass("1");
		claim.setAllocationCode("S1");
		claim.setClaimType("C");
		claim.setInsuredVehicleOwner("Personal");
		return claim;
		
	}
	
	private Claim getBetweenSomeDateLongtail2() {
		Claim claim = new Claim();
		claim.setCatastrophic(false);
		Calendar cal = Calendar.getInstance();
		cal.set(2002, 1, 4);
		claim.setDateOfAccident(cal.getTime());
		claim.setInsuranceClass("1");
		claim.setAllocationCode("S1");
		claim.setClaimType("C");
		claim.setInsuredVehicleOwner("Personal");
		return claim;
		
	}	

	private Claim getCatastrophic() {
		Claim claim = new Claim();
		claim.setCatastrophic(true);
		claim.setDateOfAccident(Calendar.getInstance().getTime());
		claim.setInsuranceClass("1");
		claim.setAllocationCode("S1");
		claim.setClaimType("1");
		claim.setInsuredVehicleOwner("Personal");
		return claim;
	}
	
}
