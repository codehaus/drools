using System;

namespace org.drools.semantics.dotnet.tests.examples.manners
{
	public class Context
	{
		private string _state;
		
		public Context(string state)
		{
			_state = state;
		}

		public virtual string State
		{
			get { return _state; }
			set { _state = value; }
		}
		
		public override string ToString()
		{
			return "[State=" + _state + "]";
		}
	}
}