using NUnit.Framework;
using System.IO;
using System.Reflection;
using org.drools.dotnet;
using System;
using org.drools.dotnet.io;

namespace org.drools.dotnet.tests.io
{
	[TestFixture]
	public class RuleBaseLoaderTests
	{
		[Test]
		public void TestStreamLoader()
		{
			RuleBase ruleBase = RuleBaseLoader.LoadFromStream(new FileStream(
				"./drls/csharp.drl.xml", FileMode.Open, FileAccess.Read));
			Assert.AreEqual(1, ruleBase.RuleSets.Count);
		}

		[Test]
		public void TestUrlLoaderWithRelativePath()
		{
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/csharp.drl.xml", UriKind.Relative));
			Assert.AreEqual(1, ruleBase.RuleSets.Count);
		}

		[Test]
		public void TestUrlLoaderWithAbsolutePath()
		{
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				Path.Combine(AppDomain.CurrentDomain.BaseDirectory,
				"./drls/csharp.drl.xml")));
			Assert.AreEqual(1, ruleBase.RuleSets.Count);
		}

		[Test]
		public void TestAssemblyLoader()
		{
			RuleBase ruleBase = RuleBaseLoader.LoadFromAssembly(this.GetType().Assembly);
			Assert.AreEqual(1, ruleBase.RuleSets.Count);
		}
	}
}
