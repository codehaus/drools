using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif
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
#if FRAMEWORK11
		public ICollection RuleSets
		{
			get{ return new ReadOnlyList(_javaRuleBase.getRuleSets()); }
		}
#else
		public ICollection<org.drools.rule.RuleSet> RuleSets
		{
			get{ return new ReadOnlyList<org.drools.rule.RuleSet>(_javaRuleBase.getRuleSets()); }
		}
#endif
	}
}
