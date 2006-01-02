using System;
using org.drools.spi;

namespace org.drools.semantics.dotnet
{
	public class DotNetObjectType : ObjectType
	{
		private Type _type;

		public DotNetObjectType(Type type)
		{
			_type = type;
		}

		public Type Type
		{
			get{ return _type; }
		}

		public bool matches(object obj)
		{
			return _type.IsInstanceOfType(obj);
		}

		public override bool Equals(object obj)
		{
			if (obj == null) return false;
			if (obj == this)return true; 
			if (_type.Equals(obj.GetType())) return false;
			return _type.Equals(((DotNetObjectType)obj).Type);
		}

		public override int GetHashCode()
		{
			return _type.GetHashCode();
		}

		public override string ToString()
		{
			return "[ObjectType: " + _type.FullName + "]";
		}
	}
}
