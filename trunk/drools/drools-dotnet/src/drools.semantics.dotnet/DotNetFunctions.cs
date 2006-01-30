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
        private LineNumberInfo _info;

		public DotNetFunctions(String text,LineNumberInfo info)
		{
			_text = text;
            _info = info;
		}

		public string getSemantic()
		{
			return "dotnet";
		}

		public string getText()
		{
			return _text;
		}
        public LineNumberInfo LineNumberInfo
        {
            get { return _info; }
        }

		public override string ToString()
		{
			return "[Function: " + _text + "]";
		}
	}
}
