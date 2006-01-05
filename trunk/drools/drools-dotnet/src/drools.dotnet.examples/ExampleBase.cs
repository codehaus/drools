using System;
using NUnit.Framework;
using org.drools.semantics.dotnet;

namespace org.drools.dotnet.examples
{
	[TestFixture]
	public abstract class ExampleBase
	{
		public ExampleBase()
		{
			//TODO: Remove this once we can figure out how to get IKVM to load fully 
			//qualified types so the semantics assembly can be dynamically loaded thru the
			//the java code.
			DotNetImporter importer = new DotNetImporter();
		}
	}
}
