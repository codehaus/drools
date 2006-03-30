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
#if FRAMEWORK11
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri(baseUri, "./drls/csharp.drl.xml"));
#else
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
#endif
			WorkingMemory wm = rb.GetNewWorkingMemory();
			TestClass o = new TestClass("foobar");
			wm.AssertObject(o);
			Assert.AreEqual(1, wm.Objects.Count);
			Assert.AreEqual(o, wm.Objects[0]);
			Assert.IsTrue(wm.ContainsObject(o));
#if FRAMEWORK11
			Assert.AreEqual(1, wm.GetObjects(typeof(TestClass)).Count);
			Assert.AreEqual(o, wm.GetObjects(typeof(TestClass))[0]);
#else
			Assert.AreEqual(1, wm.GetObjects<TestClass>().Count);
			Assert.AreEqual(o, wm.GetObjects<TestClass>()[0]);
#endif
		}

		[Test]
		public void TestModifyObject()
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

			TestClass o = new TestClass("foobar");
			wm.AssertObject(o);
			Assert.AreEqual(1, wm.Objects.Count);
			Assert.AreEqual(o, wm.Objects[0]);
			Assert.IsTrue(wm.ContainsObject(o));
#if FRAMEWORK11
			Assert.AreEqual(1, wm.GetObjects(typeof(TestClass)).Count);
			Assert.AreEqual(o, wm.GetObjects(typeof(TestClass))[0]);
#else
			Assert.AreEqual(1, wm.GetObjects<TestClass>().Count);
			Assert.AreEqual(o, wm.GetObjects<TestClass>()[0]);
#endif
			o.Name = "foobarnone";
			wm.ModifyObject(o);
			Assert.AreEqual(1, wm.Objects.Count);
			Assert.AreEqual(o, wm.Objects[0]);
			Assert.IsTrue(wm.ContainsObject(o));
#if FRAMEWORK11
			Assert.AreEqual(1, wm.GetObjects(typeof(TestClass)).Count);
			Assert.AreEqual(o, wm.GetObjects(typeof(TestClass))[0]);
			System.Collections.IList objects = wm.GetObjects(typeof(TestClass));
			Assert.AreEqual("foobarnone", ((TestClass)(objects[0])).Name);
#else
			Assert.AreEqual(1, wm.GetObjects<TestClass>().Count);
			Assert.AreEqual(o, wm.GetObjects<TestClass>()[0]);
			Assert.AreEqual("foobarnone", wm.GetObjects<TestClass>()[0].Name);
#endif
		}

		[Test]
		public void TestRetractObject()
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
			TestClass o = new TestClass("foobar");
			wm.AssertObject(o);
			Assert.AreEqual(1, wm.Objects.Count);
			Assert.AreEqual(o, wm.Objects[0]);
			Assert.IsTrue(wm.ContainsObject(o));
#if FRAMEWORK11
			Assert.AreEqual(1, wm.GetObjects(typeof(TestClass)).Count);
			Assert.AreEqual(o, wm.GetObjects(typeof(TestClass))[0]);
#else
			Assert.AreEqual(1, wm.GetObjects<TestClass>().Count);
			Assert.AreEqual(o, wm.GetObjects<TestClass>()[0]);
#endif

			wm.RetractObject(o);
			Assert.AreEqual(0, wm.Objects.Count);
			Assert.IsFalse(wm.ContainsObject(o));
#if FRAMEWORK11
			Assert.AreEqual(0, wm.GetObjects(typeof(TestClass)).Count);
#else
			Assert.AreEqual(0, wm.GetObjects<TestClass>().Count);
#endif
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