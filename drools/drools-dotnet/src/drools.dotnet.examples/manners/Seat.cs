using System;

namespace org.drools.dotnet.examples.manners
{
	public class Seat
	{
		private int _seatNumber;
		private string _name;

		public Seat(int seatNumber, string name)
		{
			_seatNumber = seatNumber;
			_name = name;
		}

		public virtual string Name
		{
			get{ return _name; }
		}
		
		public virtual int SeatNumber
		{
			get { return _seatNumber; }
		}
	
		public override string ToString()
		{
			return "[SeatNumber=" + _seatNumber + ",Name=" + _name + "]";
		}
	}
}