using System;
using Claim = org.drools.dotnet.examples.decisiontables.model.Claim;
using Team = org.drools.dotnet.examples.decisiontables.model.Team;
using NUnit.Framework;
using org.drools.dotnet.io;
namespace org.drools.dotnet.examples.decisiontables
{

    [TestFixture]
	public class TeamAllocationExample
	{
		private Claim BetweenSomeDateLongtail1
		{
			//set up the example scenarios below
			
			get
			{
				Claim claim = new Claim();
				claim.Catastrophic = false;
				System.Globalization.Calendar cal = new System.Globalization.GregorianCalendar();
				claim.DateOfAccident = cal.ToDateTime(2001, 1, 4, 0, 0, 0, 0, 0);
				claim.InsuranceClass = "1";
				claim.AllocationCode = "S1";
				claim.ClaimType = "C";
				claim.InsuredVehicleOwner = "Personal";
				return claim;

			}
			
		}
		private Claim BetweenSomeDateLongtail2
		{
			get
			{
				Claim claim = new Claim();
				claim.Catastrophic = false;
				System.Globalization.Calendar cal = new System.Globalization.GregorianCalendar();
				claim.DateOfAccident = cal.ToDateTime(2002, 1, 4, 0, 0, 0, 0, 0);
				claim.InsuranceClass = "1";
				claim.AllocationCode = "S1";
				claim.ClaimType = "C";
				claim.InsuredVehicleOwner = "Personal";
				return claim;
			}
			
		}
		private Claim Catastrophic
		{
			get
			{
				Claim claim = new Claim();
				claim.Catastrophic = true;
				claim.DateOfAccident = DateTime.Now;
				claim.InsuranceClass = "1";
				claim.AllocationCode = "S1";
				claim.ClaimType = "1";
				claim.InsuredVehicleOwner = "Personal";
				return claim;
			}
			
		}
		
		private RuleBase _ruleBase;
		
		
		
		private void  loadRuleBase()
		{
            _ruleBase = org.drools.dotnet.io.DecisionTableLoader.LoadFromStream(new System.IO.FileStream(System.Environment.CurrentDirectory + "\\drls\\TeamAllocationExample.xls", System.IO.FileMode.Open));
		}
		
		
		[Test]
		public void TestTeamAllocationExample()
        {
			
			
			loadRuleBase();
			teamAllocationBasic();
			teamAllocationLongtail1();
			teamAllocationLongtail2();
		}
		
		
		private void  teamAllocationBasic()
		{
			
			Claim cat = Catastrophic;
			Team team = new Team();
			
			executeRules(cat, team);
            Assert.AreEqual("Team Red", team.Name);
			System.Console.Out.WriteLine(team.Name);
		}
		
		private void  teamAllocationLongtail1()
		{
			Claim claim = BetweenSomeDateLongtail1;
			Team team = new Team();
			
			executeRules(claim, team);
            Assert.AreEqual("Longtail-1", team.Name);
			System.Console.Out.WriteLine(team.Name);
		}
		
		private void  teamAllocationLongtail2()
		{
			Claim claim = BetweenSomeDateLongtail2;
			Team team = new Team();
			
			
			executeRules(claim, team);
            Assert.AreEqual("Longtail-2", team.Name);
			System.Console.Out.WriteLine(team.Name);
		}
		
		
		
		private void  executeRules(Claim claim, Team team)
		{
			WorkingMemory engine = _ruleBase.GetNewWorkingMemory();
            
			engine.AssertObject(claim);
			engine.AssertObject(team);
			engine.FireAllRules();
		}
	}
}