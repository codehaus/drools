using System;
using NUnit.Framework;
using System.IO;
using org.drools.dotnet.io;

namespace org.drools.dotnet.examples.helloworld
{
	[TestFixture]
	public class HelloWorldTests
	{
		[Test]
		public void TestHelloWorldExample()
		{
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/helloworld.csharp.drl.xml", UriKind.Relative));
			WorkingMemory workingMemory = ruleBase.GetNewWorkingMemory();
			//TODO: workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
			workingMemory.AssertObject("Hello");
			workingMemory.FireAllRules();
			workingMemory.AssertObject("Goodbye");
			workingMemory.FireAllRules();
		}
	}
}
