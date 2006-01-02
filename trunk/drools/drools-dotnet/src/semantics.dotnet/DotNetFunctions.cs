using System;
using org.drools.rule;
using org.drools.spi;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// .NET Functions
	/// </summary>
	public class DotNetFunctions : Functions
	{
		private string _text;

		public DotNetFunctions(RuleSet ruleSet, int id, String text)
		{
			_text = text;
			throw new NotImplementedException("Functions are not implemented.");
		}

		public string getSemantic()
		{
			return "DotNet";
		}

		public string getText()
		{
			return _text;
		}
	}
}
