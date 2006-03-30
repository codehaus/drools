using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif
using org.drools.dotnet.rule;

namespace org.drools.dotnet.events
{
	/// <summary>
	/// Provides data for the <see cref="WorkingMemory.ConditionTested"/> event. 
	/// </summary>
	public class ConditionTestedEventArgs : EventArgs
	{
		//TODO: Finish this class
		public ConditionTestedEventArgs()
		{
		}

		//public ConditionTestedEventArgs(Rule rule, string condition, 
		//    IDictionary<string, object> parameters, bool passed)
		//{
		//}

		public Rule Rule
		{
			get { throw new NotImplementedException(); }
		}

		public string Condition
		{
			get { throw new NotImplementedException(); }
		}

#if FRAMEWORK11
		public IDictionary Parameters
#else
		public IDictionary<string, object> Parameters
#endif
		{
			get { throw new NotImplementedException(); }
		}

		public bool Passed
		{
			get { throw new NotImplementedException(); }
		}




	}
}
