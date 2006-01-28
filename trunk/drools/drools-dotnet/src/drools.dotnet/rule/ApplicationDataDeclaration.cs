using System;

namespace org.drools.dotnet.rule
{
	public class ApplicationDataDeclaration
	{
		private string _name;
		private Type _type;

		public ApplicationDataDeclaration(string name, Type type)
		{
			_name = name;
			_type = type;
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
