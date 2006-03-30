using System;
using org.drools.dotnet.rule;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif

namespace org.drools.dotnet
{
	public class RuleBaseBuilder
	{
#if FRAMEWORK11
		private IList _ruleSets = new ArrayList();
#else
		private ICollection<RuleSet> _ruleSets = new List<RuleSet>();
#endif
		public RuleBaseBuilder()
		{
		}

#if FRAMEWORK11
		public IList RuleSets
#else
		public ICollection<RuleSet> RuleSets
#endif
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
