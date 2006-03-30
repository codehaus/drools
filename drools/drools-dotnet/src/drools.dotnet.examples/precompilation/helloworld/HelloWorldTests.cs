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
            Assembly assembly = null;
#if FRAMEWORK11
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase(
				new Uri(baseUri, "./drls/helloworld.csharp.drl.xml"), "helloWorld.dll", out assembly);
#else
			RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase(
                new Uri("./drls/helloworld.csharp.drl.xml", UriKind.Relative), "helloWorld.dll", out assembly);
#endif
			WorkingMemory workingMemory = ruleBase.GetNewWorkingMemory();
			//TODO: workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
			workingMemory.AssertObject("Hello");
			workingMemory.FireAllRules();
			workingMemory.AssertObject("Goodbye");
			workingMemory.FireAllRules();
		}
	}
}
