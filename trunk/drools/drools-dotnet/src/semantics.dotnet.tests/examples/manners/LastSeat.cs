using System;

namespace org.drools.semantics.dotnet.tests.examples.manners
{
	public class LastSeat
	{
		private int _seat;

		public LastSeat(int seat)
		{
			_seat = seat;
		}

		public virtual int Seat
		{
			get { return _seat; }
		}
		
		public override string ToString()
		{
			return "[Seat=" + _seat + "]";
		}
	}
}