using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif
using System.Text;

namespace org.drools.dotnet.examples.manners
{
	public class Guest
	{
		private string _name;
		private Sex _sex;
#if FRAMEWORK11
		private IList _hobbies;
#else
		private IList<string> _hobbies;
#endif

#if FRAMEWORK11
		public Guest(string name, Sex sex, IList hobbies)
#else
		public Guest(string name, Sex sex, IList<string> hobbies)
#endif
		{
			_name = name;
			_sex = sex;
			_hobbies = hobbies;
		}

		public virtual string Name
		{
			get{ return _name; }
		}

#if FRAMEWORK11
		public virtual IList Hobbies
#else
		public virtual IList<string> Hobbies
#endif
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