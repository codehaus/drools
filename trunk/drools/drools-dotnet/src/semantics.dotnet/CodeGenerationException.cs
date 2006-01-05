using System;
using System.Collections.Generic;
using System.Text;

namespace org.drools.semantics.dotnet
{
	public class CodeGenerationException : Exception
	{
		public CodeGenerationException(string message)
			: base(message)
		{
		}

		public CodeGenerationException(string message, Exception innerException)
			: base(message, innerException)
		{
		}
	}
}
