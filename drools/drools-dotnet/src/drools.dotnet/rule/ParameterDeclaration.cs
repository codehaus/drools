using System;

namespace org.drools.dotnet.rule
{
	public class ParameterDeclaration
	{
		private string _name;
		private Type _type;
		public ParameterDeclaration(string name, Type type)
		{
			_type = type;
			_name = name;
		}

		public string Name
		{
			get { return _name; }
		}

		public Type Type
		{
			get { return _type; }
		}
	}
}
