using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif
using org.drools.semantics.dotnet;

namespace org.drools.dotnet.rule
{
	public class Rule
	{
		private string _name;
		private RuleSet _ruleSet;
#if FRAMEWORK11
		private IList _conditions = new ArrayList();
		private IList _parameterDeclarations = new ArrayList();
#else
		private ICollection<string> _conditions = new List<string>();
		private ICollection<ParameterDeclaration> _parameterDeclarations = new List<ParameterDeclaration>();
#endif
		private string _consequence;
		private string _documentation;
		private int _salience;
		private long _duration;
		private bool _isNoLoop;
		private string _xorGroup;

		public Rule(string name) : this(name, null)
		{
		}

		public Rule(string name, RuleSet ruleSet)
		{
			_name = name;
			if (ruleSet != null)
			{
				_ruleSet = ruleSet;
				_ruleSet.Rules.Add(this);
			}
		}

		public string Name
		{
			get { return _name; }
		}

		public string Documentation
		{
			get { return _documentation; }
			set { _documentation = value; }
		}

		public RuleSet RuleSet
		{
			get { return _ruleSet; }
#if !FRAMEWORK11
			internal set { _ruleSet = value; }
#endif
		}

#if FRAMEWORK11	//1.1 framework doesn't allow internal setters, so declare an internal method for this purpose instead.
		internal void SetRuleSet(RuleSet ruleSet)
		{
			_ruleSet = ruleSet;
		}
#endif

#if FRAMEWORK11
		public IList Conditions
#else
		public ICollection<string> Conditions
#endif
		{
			get { return _conditions; }
		}

#if FRAMEWORK11
		public IList ParameterDeclarations
#else
		public ICollection<ParameterDeclaration> ParameterDeclarations
#endif
		{
			get { return _parameterDeclarations;  }
		}

		public string Consequence
		{
			get { return _consequence;  }
			set { _consequence = value; }
		}

		public int Salience
		{
			get { return _salience; }
			set { _salience = value; }
		}

		public long Duration
		{
			get { return _duration; }
			set { _duration = value; }
		}

		public bool IsNoLoop
		{
			get { return _isNoLoop; }
			set { _isNoLoop = value; }
		}

		public string XorGroup
		{
			get { return _xorGroup; }
			set { _xorGroup = value; }
		}
	}
}