using System;
using System.Collections.Generic;

namespace org.drools.dotnet.examples.manners
{
	public class Seating
	{
		private int _seat1;
		private int _seat2;
		private Guest _guest2;
		private Guest _guest1;
		private Seating _prevSeat;
		private IList<Guest> _tabooList;

		public Seating(int seat1, Guest guest1, Seating prevSeat)
		{
			_seat1 = seat1;
			_guest1 = guest1;
			_prevSeat = prevSeat;
			_seat2 = seat1 + 1;
			_tabooList = new List<Guest>();

			if (_prevSeat != null)
			{
				foreach (Guest guest in prevSeat.TabooList)
				{
					_tabooList.Add(guest);
				}
			}

			_tabooList.Add(guest1);
		}

		public int Seat1
		{
			get { return _seat1; }
		}

		public int Seat2
		{
			get { return _seat2; }
		}

		public Guest Guest1
		{
			get { return _guest1; }
		}

		public Guest Guest2
		{
			get { return _guest2; }
			set { _guest2 = value; }
		}

		public virtual Seating PrevSeat
		{
			get { return _prevSeat; }
		}

		public virtual IList<Guest> TabooList
		{
			get { return _tabooList; }
		}

		public override string ToString()
		{
			return "[seat1=" + _seat1 + ",guest1=" + _guest1 + ",seat2=" + _seat2 + ",guest2=" + _guest2 + "]";
		}
	}
}