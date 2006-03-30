using System;
using NUnit.Framework;
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
#if FRAMEWORK11
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri(baseUri, "./drls/csharp.drl.xml"));
#else
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
#endif
			WorkingMemory wm = rb.GetNewWorkingMemory();
			Assert.IsNotNull(wm);
		}

		[Test]
		public void TestRuleSetsProperty()
		{
#if FRAMEWORK11
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri(baseUri, "./drls/csharp.drl.xml"));
#else
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
#endif
			Assert.IsNotNull(rb.RuleSets);
			Assert.AreEqual(1, rb.RuleSets.Count);
		}
	}
}
