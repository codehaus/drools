using System;
using System.Collections.Generic;
using org.drools.dotnet.util;

namespace org.drools.dotnet
{
	/// <summary>
	/// Represents one or more rulesets loaded in the Drools engine.
	/// </summary>
	public class RuleBase
	{
		private org.drools.RuleBase _javaRuleBase;

		/// <summary>
		/// Create a new instance of RuleBase
		/// </summary>
		/// <param name="javaRuleBase"><see cref="org.drools.RuleBase"/></param>
		internal RuleBase(org.drools.RuleBase javaRuleBase)
		{
			_javaRuleBase = javaRuleBase;
		}

		/// <summary>
		/// Get a new instance of working memory
		/// </summary>
		/// <returns></returns>
		public WorkingMemory GetNewWorkingMemory()
		{
			return new WorkingMemory(_javaRuleBase.newWorkingMemory());
		}

		/// <summary>
		/// a READ-ONLY list of all the RuleSets that make up the RuleBase
		/// </summary>
		public IList<org.drools.rule.RuleSet> RuleSets
		{
			get{ return new ReadOnlyList<org.drools.rule.RuleSet>(_javaRuleBase.getRuleSets()); }
		}
	}
}
