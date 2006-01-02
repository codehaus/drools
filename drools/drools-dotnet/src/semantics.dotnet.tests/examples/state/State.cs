using System;

namespace org.drools.semantics.dotnet.tests.examples.state
{
	public class State
	{
		private string _name;
		private Status _status;

		public State(string name)
		{
			_name = name;
			_status = Status.NotRun;
		}

		public virtual string Name
		{
			get{ return _name; }
		}
		
		public virtual Status Status
		{
			get { return _status; }
			set { _status = value; }
		}
		
		public override string ToString()
		{
			return "[" + _name + ":" + _status + "]";
		}
	}
}