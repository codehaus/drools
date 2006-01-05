using System;
using org.drools.spi;
using NUnit.Framework;
using org.drools.smf;
using org.drools.rule;
using org.drools.semantics.@base;
using java.lang;

namespace org.drools.semantics.dotnet.tests
{
    [TestFixture]
    public class DotNetObjectTypeTests
    {
        [Test]
        [ExpectedException(typeof(FactoryException), "No type name specified.")]
        public void TestWithMissingType()
        {
            RuleBaseContext ruleBaseContext = new RuleBaseContext();
            DefaultConfiguration configuration = new DefaultConfiguration("test1");
            RuleSet ruleSet = new RuleSet("test RuleSet", ruleBaseContext);
            Rule rule = new Rule("Test Rule 1", ruleSet);;

            Importer importer = new DotNetImporter();
            rule.setImporter(importer);

            ObjectTypeFactory factory = new DotNetObjectTypeFactory();
            ObjectType type = factory.newObjectType(rule, ruleBaseContext, configuration);
        }

        [Test]
        public void TestWithoutImports()
        {
            RuleBaseContext ruleBaseContext = new RuleBaseContext();
            DefaultConfiguration configuration = new DefaultConfiguration("test1");
            configuration.setText("System.Collections.Hashtable");

            RuleSet ruleSet = new RuleSet("test RuleSet", ruleBaseContext);
            Rule rule = new Rule("Test Rule 1", ruleSet);

			Importer importer = new DotNetImporter();
            rule.setImporter(importer);

			ObjectTypeFactory factory = new DotNetObjectTypeFactory();
			DotNetObjectType type = (DotNetObjectType) 
				factory.newObjectType(rule, ruleBaseContext, configuration);
            Assert.AreEqual(configuration.getText(), type.Type.FullName);
			Assert.IsTrue(type.matches(Activator.CreateInstance(Type.GetType(configuration.getText()))));
        }

		[Test]
		public void TestWithImports()
		{
			RuleBaseContext ruleBaseContext = new RuleBaseContext();
			DefaultConfiguration configuration = new DefaultConfiguration("test1");
			configuration.setText("Hashtable");

			RuleSet ruleSet = new RuleSet("test RuleSet", ruleBaseContext);
			Rule rule = new Rule("Test Rule 1", ruleSet);

			Importer importer = new DotNetImporter();
			importer.addImport(new BaseImportEntry("System.Collections"));
			rule.setImporter(importer);

			ObjectTypeFactory factory = new DotNetObjectTypeFactory();
			DotNetObjectType type = (DotNetObjectType) 
				factory.newObjectType(rule, ruleBaseContext, configuration);
			Assert.AreEqual("System.Collections.Hashtable", type.Type.FullName);
			Assert.IsTrue(type.matches(Activator.CreateInstance(Type.GetType("System.Collections.Hashtable"))));
		}
    }
}
