using System;
using System.Collections.Generic;
using org.drools.semantics.dotnet;

namespace org.drools.dotnet.rule
{
	public class Rule
	{
		private string _name;
		private RuleSet _ruleSet;
		private ICollection<string> _conditions = new List<string>();
		private ICollection<ParameterDeclaration> _parameterDeclarations = new List<ParameterDeclaration>();
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
			internal set { _ruleSet = value; }
		}

		public ICollection<string> Conditions
		{
			get { return _conditions; }
		}

		public ICollection<ParameterDeclaration> ParameterDeclarations
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