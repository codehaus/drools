using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif
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
