using System;
using NUnit.Framework;
using System.IO;

namespace org.drools.semantics.dotnet.tests.examples.nodesharing
{
	[TestFixture]
	public class NodeSharingTests
	{
		[Test]
		public void TestNodeSharingExample()
		{
			RuleBase ruleBase = DotNetRuleBaseLoader.Load(
				new FileInfo("./examples/nodesharing/nodesharing.dotnet.drl.xml"));
			WorkingMemory workingMemory = ruleBase.newWorkingMemory();

			for (int i = 0; i < 10; i++)
			{
				workingMemory.assertObject(i);
			}
			workingMemory.fireAllRules();

			//TODO: Add asserts to verify output
		}
	}
}
