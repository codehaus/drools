using System;
using NUnit.Framework;
using System.IO;
using org.drools.dotnet.io;
using System.Reflection;

namespace org.drools.dotnet.precompilation.examples.helloworld
{
	[TestFixture]
	public class HelloWorldTests
	{
		[Test]
		public void TestHelloWorldExample()
		{
            Assembly ass = null;
            RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase(
                new Uri("./drls/helloworld.csharp.drl.xml", UriKind.Relative), "helloWorld.dll", out ass);
			WorkingMemory workingMemory = ruleBase.GetNewWorkingMemory();
			//TODO: workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
			workingMemory.AssertObject("Hello");
			workingMemory.FireAllRules();
			workingMemory.AssertObject("Goodbye");
			workingMemory.FireAllRules();
		}
	}
}
