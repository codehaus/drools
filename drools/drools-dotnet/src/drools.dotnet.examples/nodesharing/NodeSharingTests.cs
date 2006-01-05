using System;
using NUnit.Framework;
using System.IO;

namespace org.drools.dotnet.examples.nodesharing
{
	[TestFixture]
	public class NodeSharingTests : ExampleBase
	{
		[Test]
		public void TestNodeSharingExample()
		{
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/nodesharing.csharp.drl.xml", UriKind.Relative));
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
