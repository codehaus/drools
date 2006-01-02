using System;
using NUnit.Framework;
using java.lang;
using org.drools.spi;
using org.drools.semantics.@base;

namespace org.drools.semantics.dotnet.tests
{
	[TestFixture]
	public class DotNetImporterTest
	{
		[Test]
		public void TestInternalQualifiedClass()
		{
			RuleBaseContext context = new RuleBaseContext();
			DotNetImporter importer = new DotNetImporter();
			Class clazz = importer.importClass(context.getClass().getClassLoader(),
				"System.Collections.Hashtable");
			Assert.AreEqual(Class.forName("cli.System.Collections.Hashtable"), clazz);
		}

		[Test]
		public void TestExternalQualifiedClass()
		{
			RuleBaseContext context = new RuleBaseContext();
			DotNetImporter importer = new DotNetImporter();
			Class clazz = importer.importClass(context.getClass().getClassLoader(),
				"System.Data.DataSet, System.Data, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089");
			Assert.AreEqual("cli.System.Data.DataSet", clazz.getName());

			Type type = importer.importType("System.Data.DataSet, System.Data, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089");
			Assert.AreEqual(Type.GetType("System.Data.DataSet, System.Data, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089"), type);
		}

		[Test]
		[ExpectedException(typeof(System.Exception), "Unable to find type 'Foo.Bar'")]
		public void TestNonExistentClass()
		{
			RuleBaseContext context = new RuleBaseContext();
			DotNetImporter importer = new DotNetImporter();
			Class clazz = importer.importClass(context.getClass().getClassLoader(), "Foo.Bar");
		}

		[Test]
		public void TestInternalUnqualifiedClassWithImports()
		{
			RuleBaseContext context = new RuleBaseContext();
			DotNetImporter importer = new DotNetImporter();
			importer.addImport(new BaseImportEntry("System.Collections"));
			Class clazz = importer.importClass(context.getClass().getClassLoader(), "Hashtable");
			Assert.AreEqual(Class.forName("cli.System.Collections.Hashtable"), clazz);

			Type type = importer.importType("Hashtable");
			Assert.AreEqual(Type.GetType("System.Collections.Hashtable"), type);
		}

		[Test]
		[ExpectedException(typeof(Error), "Unable to find unambiguously defined class 'Hashtable', candidates are: [org.drools.semantics.dotnet.tests.Hashtable, System.Collections.Hashtable]")]
		public void TestInternalUnqualifiedClassWithAmbiguousImports()
		{
			RuleBaseContext context = new RuleBaseContext();
			DotNetImporter importer = new DotNetImporter();
			importer.addImport(new BaseImportEntry("System.Collections"));
			importer.addImport(new BaseImportEntry(this.GetType().Namespace));

			Class clazz = importer.importClass(context.getClass().getClassLoader(), "Hashtable");
		}

		[Test]
		public void TestMultipleCalls()
		{
			RuleBaseContext context = new RuleBaseContext();
			DotNetImporter importer = new DotNetImporter();
			importer.addImport(new BaseImportEntry("System.Collections"));
			Class clazz = importer.importClass(context.getClass().getClassLoader(), "Hashtable");
			Assert.AreEqual(Class.forName("cli.System.Collections.Hashtable"), clazz);

			clazz = importer.importClass(context.getClass().getClassLoader(), "Hashtable");
			Assert.AreEqual(Class.forName("cli.System.Collections.Hashtable"), clazz);
		}
	}

	/// <summary>
	/// Ambiguously gay test class
	/// </summary>
	public class Hashtable
	{
	}
}
