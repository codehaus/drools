using System;
using System.IO;
using NUnit.Framework;
using org.drools.dotnet.io;

namespace org.drools.dotnet.examples.state
{
	[TestFixture]
	public class StateTests
	{
		[Test]
		public void TestStateExample()
		{
#if FRAMEWORK11
			RuleBase ruleBase = RuleBaseLoader.LoadFromRelativeUri(
				"./drls/state.csharp.drl.xml");
#else
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/state.csharp.drl.xml", UriKind.Relative));
#endif

			WorkingMemory workingMemory = ruleBase.GetNewWorkingMemory();
			//TODO: workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
			State a = new State("A");
			State b = new State("B");
			State c = new State("C");
			State d = new State("D");

			workingMemory.AssertObject(a);
			workingMemory.AssertObject(b);
			workingMemory.AssertObject(c);
			workingMemory.AssertObject(d);

			workingMemory.FireAllRules();

			Assert.AreEqual(Status.Finished, a.Status);
			Assert.AreEqual(Status.Finished, b.Status);
			Assert.AreEqual(Status.Finished, c.Status);
			Assert.AreEqual(Status.Finished, d.Status);
		}
	}
}
