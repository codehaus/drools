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
            WorkingMemory workingMemory = ruleBase.GetNewWorkingMemory();
            workingMemory.AssertObject("Ready");
            workingMemory.FireAllRules();
			Assert.AreEqual(1, ruleBase.RuleSets.Count);
		}

		[Test]
		public void TestUrlLoaderWithRelativePath()
		{
#if FRAMEWORK11
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				baseUri, "./drls/csharp.drl.xml"));
#else
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/csharp.drl.xml", UriKind.Relative));
#endif
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

		[Test]
		public void TestReaderLoader()
		{
			StreamReader reader = new StreamReader("./drls/csharp.drl.xml");
			RuleBase ruleBase = RuleBaseLoader.LoadFromReader(reader);
			Assert.AreEqual(1, ruleBase.RuleSets.Count);
		}
	}
}
