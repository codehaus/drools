using System;
using System.Collections.Generic;
using System.Text;
using org.drools.semantics.dotnet;

namespace org.drools.dotnet.rule
{
	public class RuleSet
	{
		private string _name;
		private string _documentation;
		private ICollection<Rule> _rules = new List<Rule>();
		private ICollection<ApplicationDataDeclaration> _applicationDataDeclarations = 
			new List<ApplicationDataDeclaration>();
		private ICollection<string> _functions = new List<string>();
		private ICollection<string> _imports = new List<string>();

		public RuleSet(string name)
		{
			_name = name;
		}

		public string Name
		{
			get { return _name;  }
		}

		public string Documentation
		{
			get { return _documentation; }
			set { _documentation = value; }
		}

		public ICollection<String> Functions
		{
			get { return _functions; }
		}

		public ICollection<String> Imports
		{
			get { return _imports; }
		}

		public ICollection<ApplicationDataDeclaration> ApplicationDataDeclarations
		{
			get
			{
				throw new NotImplementedException(
					"The .NET semantics module does not support application data.");
				//return _applicationDataDeclarations;
			}
		}

		public ICollection<Rule> Rules
		{
			get { return _rules; }
		}

		internal org.drools.rule.RuleSet InternalRuleSet
		{
			get
			{
				org.drools.rule.RuleSet ruleset = new org.drools.rule.RuleSet(_name);
				ruleset.setImporter(new DotNetImporter());
				ruleset.setDocumentation(_documentation);

				//TODO: Readd this when the semantics module supports it.
				//Application Data
				//foreach(ApplicationDataDeclaration add in _applicationDataDeclarations)
				//{
				//    ruleset.addApplicationData(new org.drools.rule.ApplicationData(
				//        ruleset, add.Name, java.lang.Class.forName(
				//        add.Type.AssemblyQualifiedName)));
				//}

				//Functions
				StringBuilder functionsText = new StringBuilder();
				foreach (string function in _functions)
				{
					functionsText.Append(function);
				}
				ruleset.addFunctions(new DotNetFunctions(functionsText.ToString()));
				

				//Rules
				foreach (Rule rule in _rules)
				{
					ruleset.addRule(CreateRule(rule, ruleset));
				}
				return ruleset;
			}
		}

		private org.drools.rule.Rule CreateRule(Rule rule, org.drools.rule.RuleSet ruleset)
		{
			org.drools.rule.Rule javaRule = new org.drools.rule.Rule(rule.Name, ruleset);

			//These values can be set in any order
			javaRule.setDocumentation(rule.Documentation);
			javaRule.setDuration(rule.Duration);
			javaRule.setNoLoop(rule.IsNoLoop);
			javaRule.setXorGroup(rule.XorGroup);
			javaRule.setSalience(rule.Salience);

			//The order these values are set in is VERY important!
			DotNetImporter importer = new DotNetImporter();
			foreach(string import in _imports)
			{
				importer.addImport(new org.drools.semantics.@base.BaseImportEntry(import));
			}
			javaRule.setImporter(importer);

			//Parameters
			foreach (ParameterDeclaration pd in rule.ParameterDeclarations)
			{
				javaRule.addParameterDeclaration(pd.Name, new DotNetObjectType(pd.Type));
			}

			//Conditions
			int count = 0;
			foreach (string condition in rule.Conditions)
			{
				javaRule.addCondition(new DotNetCondition(javaRule, count++, condition,null));
			}

			//Consequence
			javaRule.setConsequence(new DotNetBlockConsequence(javaRule, 0, rule.Consequence,null));

			return javaRule;
		}
	}
}
