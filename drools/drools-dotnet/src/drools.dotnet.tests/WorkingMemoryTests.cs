using System;
using NUnit.Framework;
using org.drools.dotnet.io;

namespace org.drools.dotnet.tests
{
	[TestFixture]
	public class WorkingMemoryTests
	{
		[Test]
		[Ignore("Need to implement this unit test.")]
		public void TestFireAllRules()
		{
			throw new NotImplementedException();
		}

		[Test]
		[Ignore("Need to implement this unit test.")]
		public void TestApplicationData()
		{
			throw new NotImplementedException();
		}

		[Test]
		[Ignore("Need to implement this unit test.")]
		public void TestGetObjects()
		{
			throw new NotImplementedException();
		}

		[Test]
		[Ignore("Need to implement this unit test.")]
		public void TestClearAgenda()
		{
			throw new NotImplementedException();
		}

		[Test]
		public void TestAssertObject()
		{
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
			WorkingMemory wm = rb.GetNewWorkingMemory();
			TestClass o = new TestClass("foobar");
			wm.AssertObject(o);
			Assert.AreEqual(1, wm.Objects.Count);
			Assert.AreEqual(o, wm.Objects[0]);
			Assert.IsTrue(wm.ContainsObject(o));
			Assert.AreEqual(1, wm.GetObjects<TestClass>().Count);
			Assert.AreEqual(o, wm.GetObjects<TestClass>()[0]);
		}

		[Test]
		public void TestModifyObject()
		{
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
			WorkingMemory wm = rb.GetNewWorkingMemory();

			TestClass o = new TestClass("foobar");
			wm.AssertObject(o);
			Assert.AreEqual(1, wm.Objects.Count);
			Assert.AreEqual(o, wm.Objects[0]);
			Assert.IsTrue(wm.ContainsObject(o));
			Assert.AreEqual(1, wm.GetObjects<TestClass>().Count);
			Assert.AreEqual(o, wm.GetObjects<TestClass>()[0]);

			o.Name = "foobarnone";
			wm.ModifyObject(o);
			Assert.AreEqual(1, wm.Objects.Count);
			Assert.AreEqual(o, wm.Objects[0]);
			Assert.IsTrue(wm.ContainsObject(o));
			Assert.AreEqual(1, wm.GetObjects<TestClass>().Count);
			Assert.AreEqual(o, wm.GetObjects<TestClass>()[0]);
			Assert.AreEqual("foobarnone", wm.GetObjects<TestClass>()[0].Name);
		}

		[Test]
		public void TestRetractObject()
		{
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
			WorkingMemory wm = rb.GetNewWorkingMemory();
			TestClass o = new TestClass("foobar");
			wm.AssertObject(o);
			Assert.AreEqual(1, wm.Objects.Count);
			Assert.AreEqual(o, wm.Objects[0]);
			Assert.IsTrue(wm.ContainsObject(o));
			Assert.AreEqual(1, wm.GetObjects<TestClass>().Count);
			Assert.AreEqual(o, wm.GetObjects<TestClass>()[0]);

			wm.RetractObject(o);
			Assert.AreEqual(0, wm.Objects.Count);
			Assert.IsFalse(wm.ContainsObject(o));
			Assert.AreEqual(0, wm.GetObjects<TestClass>().Count);
		}

		//Test class
		public class TestClass
		{
			public TestClass(string name)
			{
				_name = name;
			}
			private string _name;
			public string Name
			{
				get { return _name; }
				set { _name = value; }
			}
		}
	}
}