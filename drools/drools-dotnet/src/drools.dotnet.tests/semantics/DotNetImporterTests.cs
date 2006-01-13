using System;
using NUnit.Framework;
using java.lang;
using org.drools.spi;
using org.drools.semantics.@base;
using org.drools.semantics.dotnet;

namespace org.drools.dotnet.tests.semantics
{
	[TestFixture]
	public class DotNetImporterTests
	{
		[Test]
		public void TestInternalQualifiedClass()
		{
			RuleBaseContext context = new RuleBaseContext();
			DotNetImporter importer = new DotNetImporter();
			Class clazz = importer.importClass(context.getClass().getClassLoader(),
				"System.Collections.Hashtable");
			Assert.AreEqual(Class.forName(
				Type.GetType("System.Collections.Hashtable").AssemblyQualifiedName), clazz);
		}

		[Test]
		public void TestExternalQualifiedClass()
		{
			string typeName = "System.Data.DataSet, System.Data, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089";
			RuleBaseContext context = new RuleBaseContext();
			DotNetImporter importer = new DotNetImporter();
			Class clazz = importer.importClass(context.getClass().getClassLoader(),
				typeName);
			Assert.AreEqual(Class.forName(typeName), clazz);

			Type type = importer.importType(typeName);
			Assert.AreEqual(Type.GetType(typeName), type);
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
			Assert.AreEqual(Class.forName(
				Type.GetType("System.Collections.Hashtable").AssemblyQualifiedName), clazz);

			Type type = importer.importType("Hashtable");
			Assert.AreEqual(Type.GetType("System.Collections.Hashtable"), type);
		}

		[Test]
		[ExpectedException(typeof(Error), "Unable to find unambiguously defined class 'Hashtable', candidates are: [org.drools.dotnet.tests.semantics.Hashtable, System.Collections.Hashtable]")]
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
			Assert.AreEqual(Class.forName(
				Type.GetType("System.Collections.Hashtable").AssemblyQualifiedName), clazz);

			clazz = importer.importClass(context.getClass().getClassLoader(), "Hashtable");
			Assert.AreEqual(Class.forName(Type.GetType("System.Collections.Hashtable").AssemblyQualifiedName), clazz);
		}
	}

	/// <summary>
	/// Test Hashtable class
	/// </summary>
	public class Hashtable
	{
	}
}
