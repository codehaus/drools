using System;
using System.Collections.Generic;
using System.Text;

namespace org.drools.semantics.dotnet.tests.examples.manners
{
	public class Guest
	{
		private string _name;
		private Sex _sex;
		private IList<string> _hobbies;

		public Guest(string name, Sex sex, IList<string> hobbies)
		{
			_name = name;
			_sex = sex;
			_hobbies = hobbies;
		}

		public virtual string Name
		{
			get{ return _name; }
		}

		public virtual IList<string> Hobbies
		{
			get{ return _hobbies; }
		}

		public virtual Sex Sex
		{
			get { return _sex; }
		}

		public virtual bool HasOppositeSex(Guest guest)
		{
			return ! (_sex.Equals(guest.Sex));
		}
		
		public virtual bool HasSameHobby(Guest guest)
		{
			foreach (string hobby in _hobbies)
			{
				if (guest.Hobbies.Contains(hobby)) return true;
			}
			return false;
		}
		
		public override string ToString()
		{
			StringBuilder sb = new StringBuilder();
			foreach (string hobby in _hobbies)
			{
				if (sb.Length > 0) sb.Append(", ");
				sb.Append(hobby);
			}
			return "[Name=" + _name + ",Sex=" + _sex + ",Hobbies=" + sb.ToString() + "]";
		}
	}
}