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

		public DotNetFunctions(String text)
		{
			_text = text;
		}

		public string getSemantic()
		{
			return "dotnet";
		}

		public string getText()
		{
			return _text;
		}

		public override string ToString()
		{
			return "[Function: " + _text + "]";
		}
	}
}
