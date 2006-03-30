using System;
using NUnit.Framework;
using System.IO;
using org.drools.dotnet.io;

namespace org.drools.dotnet.examples.nodesharing
{
	[TestFixture]
	public class NodeSharingTests
	{
		[Test]
		public void TestNodeSharingExample()
		{
#if FRAMEWORK11
			RuleBase ruleBase = RuleBaseLoader.LoadFromRelativeUri(
				"./drls/nodesharing.csharp.drl.xml");
#else
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/nodesharing.csharp.drl.xml", UriKind.Relative));
#endif
			WorkingMemory workingMemory = ruleBase.GetNewWorkingMemory();

			for (int i = 0; i < 10; i++)
			{
				workingMemory.AssertObject(i);
			}
			workingMemory.FireAllRules();

			//TODO: Add asserts to verify output
		}
	}
}
