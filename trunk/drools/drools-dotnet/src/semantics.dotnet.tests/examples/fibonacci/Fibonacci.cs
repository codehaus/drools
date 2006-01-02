using System;

namespace org.drools.semantics.dotnet.tests.examples.fibonacci
{
	public class Fibonacci
	{
		private int _sequence;
		private long _value;

		public Fibonacci(int sequence)
		{
			_sequence = sequence;
			_value = -1;
		}

		public virtual int Sequence
		{
			get{ return _sequence; }	
		}

		public virtual long Value
		{
			get { return _value; }
			set{ _value = value; }			
		}

		public override string ToString()
		{
			return "Fibonacci(" + _sequence + "/" + _value + ")";
		}
	}
}