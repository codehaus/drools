using System;
using NUnit.Framework;
using org.drools.dotnet.events;
using org.drools.dotnet.io;

namespace org.drools.dotnet.tests
{
	[TestFixture]
	public class WorkingMemoryEventTests
	{
		private int _objectAssertCount;
		private int _objectModifiedCount;
		private int _objectRetractedCount;
		private EventArgs _eventArgs;
		private object _sender;
		private WorkingMemory _workingMemory;

		[SetUp]
		public void Initialize()
		{
			_objectAssertCount = 0;
			_objectModifiedCount = 0;
			_objectRetractedCount = 0;
			_eventArgs = null;
			_sender = null;
			RuleBase rb = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
			_workingMemory = rb.GetNewWorkingMemory();
			_workingMemory.ObjectAsserted += new EventHandler<ObjectAssertedEventArgs>(
				WorkingMemory_ObjectAsserted);
			_workingMemory.ObjectModified += new EventHandler<ObjectModifiedEventArgs>(
				WorkingMemory_ObjectModified);
			_workingMemory.ObjectRetracted += new EventHandler<ObjectRetractedEventArgs>(
				WorkingMemory_ObjectRetracted);
		}

		[Test]
		public void TestObjectAssertedEvent()
		{
			Object o = new Object();
			_workingMemory.AssertObject(o);
			Assert.AreEqual(1, _objectAssertCount);
			Assert.AreEqual(0, _objectModifiedCount);
			Assert.AreEqual(0, _objectRetractedCount);
			ObjectAssertedEventArgs args = (ObjectAssertedEventArgs)_eventArgs;
			Assert.AreEqual(o, args.Object);
			Assert.AreEqual(_workingMemory, _sender);
		}

		[Test]
		public void TestObjectModifiedEvent()
		{
			Object o = new Object();
			_workingMemory.AssertObject(o);
			_workingMemory.ModifyObject(o);
			Assert.AreEqual(1, _objectAssertCount);
			Assert.AreEqual(1, _objectModifiedCount);
			Assert.AreEqual(0, _objectRetractedCount);
			ObjectModifiedEventArgs args = (ObjectModifiedEventArgs)_eventArgs;
			Assert.AreEqual(o, args.OldObject);
			Assert.AreEqual(o, args.NewObject);
			Assert.AreEqual(_workingMemory, _sender);
		}

		[Test]
		public void TestObjectRetractedEvent()
		{
			Object o = new Object();
			_workingMemory.AssertObject(o);
			_workingMemory.ModifyObject(o);
			_workingMemory.RetractObject(o);
			Assert.AreEqual(1, _objectAssertCount);
			Assert.AreEqual(1, _objectModifiedCount);
			Assert.AreEqual(1, _objectRetractedCount);
			ObjectRetractedEventArgs args = (ObjectRetractedEventArgs)_eventArgs;
			Assert.AreEqual(o, args.Object);
			Assert.AreEqual(_workingMemory, _sender);
		}

		private void WorkingMemory_ObjectAsserted(object sender, ObjectAssertedEventArgs e)
		{
			_objectAssertCount++;
			_eventArgs = e;
			_sender = sender;
		}

		private void WorkingMemory_ObjectModified(object sender, ObjectModifiedEventArgs e)
		{
			_objectModifiedCount++;
			_eventArgs = e;
			_sender = sender;
		}

		private void WorkingMemory_ObjectRetracted(object sender, ObjectRetractedEventArgs e)
		{
			_objectRetractedCount++;
			_eventArgs = e;
			_sender = sender;
		}
	}
}
