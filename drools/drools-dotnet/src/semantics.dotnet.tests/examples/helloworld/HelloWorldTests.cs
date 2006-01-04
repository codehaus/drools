using System;
using NUnit.Framework;
using System.IO;
using org.drools.@event;

namespace org.drools.semantics.dotnet.tests.examples.helloworld
{
	[TestFixture]
	public class HelloWorldTests
	{
		[Test]
		public void TestHelloWorldExample()
		{
			RuleBase ruleBase = DotNetRuleBaseLoader.Load(
				new FileInfo("./examples/helloworld/helloworld.dotnet.drl.xml"));
			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			workingMemory.addEventListener(new DebugWorkingMemoryEventListener());

			workingMemory.assertObject("Hello");
			workingMemory.fireAllRules();
			workingMemory.assertObject("Goodbye");
			workingMemory.fireAllRules();
		}
	}
}
