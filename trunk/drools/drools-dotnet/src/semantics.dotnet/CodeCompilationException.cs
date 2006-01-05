using System;
using System.CodeDom.Compiler;
using System.Text;

namespace org.drools.semantics.dotnet
{
	public class CodeCompilationException : Exception
	{
		public CodeCompilationException(string message)
			: base(message)
		{
		}

		public CodeCompilationException(string message, Exception innerException)
			: base(message, innerException)
		{

		}
	}
}
