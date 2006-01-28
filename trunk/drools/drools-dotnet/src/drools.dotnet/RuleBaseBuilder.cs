using System;
using org.drools.dotnet.rule;
using System.Collections.Generic;

namespace org.drools.dotnet
{
	public class RuleBaseBuilder
	{
		private ICollection<RuleSet> _ruleSets = new List<RuleSet>();
		public RuleBaseBuilder()
		{
		}

		public ICollection<RuleSet> RuleSets
		{
			get { return _ruleSets; }
		}

		public RuleBase Build()
		{
			drools.RuleBaseBuilder rbb = new org.drools.RuleBaseBuilder();
			foreach (RuleSet ruleSet in _ruleSets)
			{
				rbb.addRuleSet(ruleSet.InternalRuleSet);
			}
			return new RuleBase(rbb.build());
		}
	}
}
