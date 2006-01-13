using System;
using NUnit.Framework;
using System.Collections.Generic;
using org.drools.dotnet.io;
using NMock;
using NMock.Constraints;

namespace org.drools.dotnet.tests
{
	[TestFixture]
	public class RuleBaseTests
	{
		[Test]
		public void TestWorkingMemoryMethod()
		{
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
			WorkingMemory wm = rb.GetNewWorkingMemory();
			Assert.IsNotNull(wm);
		}

		[Test]
		public void TestRuleSetsProperty()
		{
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
			Assert.IsNotNull(rb.RuleSets);
			Assert.AreEqual(1, rb.RuleSets.Count);
		}
	}
}
