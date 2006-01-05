using System;
using NUnit.Framework;
using System.IO;
using org.drools.@event;

namespace org.drools.dotnet.examples.helloworld
{
	[TestFixture]
	public class HelloWorldTests : ExampleBase
	{
		[Test]
		public void TestHelloWorldExample()
		{

			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/helloworld.csharp.drl.xml", UriKind.Relative));
			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			workingMemory.addEventListener(new DebugWorkingMemoryEventListener());

			workingMemory.assertObject("Hello");
			workingMemory.fireAllRules();
			workingMemory.assertObject("Goodbye");
			workingMemory.fireAllRules();
		}
	}
}
