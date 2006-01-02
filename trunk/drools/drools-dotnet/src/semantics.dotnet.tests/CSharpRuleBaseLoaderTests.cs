using NUnit.Framework;
using System.IO;
using System.Reflection;

namespace org.drools.semantics.csharp.tests
{
	[TestFixture]
	public class CSharpRuleBaseLoaderTests
	{
		[Test]
		public void TestFileLoader()
		{
			RuleBase ruleBase = CSharpRuleBaseLoader.Load(new FileInfo("./examples/drls/file.drl.xml"));
			Assert.AreEqual(1, ruleBase.getRuleSets().size());
		}

		[Test]
		public void TestReaderLoader()
		{
			StreamReader reader = new StreamReader("./examples/drls/file.drl.xml");
			RuleBase ruleBase = CSharpRuleBaseLoader.Load(reader);
			Assert.AreEqual(1, ruleBase.getRuleSets().size());
		}

		[Test]
		public void TestEmbeddedResourceLoader()
		{
			RuleBase ruleBase = CSharpRuleBaseLoader.Load(
				this.GetType().Assembly,
				"org.drools.semantics.csharp.tests.examples.drls.resource.drl.xml");
			Assert.AreEqual(1, ruleBase.getRuleSets().size());
		}
	}
}
