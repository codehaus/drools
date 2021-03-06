using System;
using NUnit.Framework;
using System.IO;
using System.Threading;
using org.drools.dotnet.io;

namespace org.drools.dotnet.examples.escalation
{
	[TestFixture]
	public class EscalationTests
	{
		[Test]
		public void TestEscalationExample()
		{
#if FRAMEWORK11
			RuleBase ruleBase = RuleBaseLoader.LoadFromRelativeUri(
				"./drls/escalation.csharp.drl.xml");
#else
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/escalation.csharp.drl.xml", UriKind.Relative));
#endif
			WorkingMemory workingMemory = ruleBase.GetNewWorkingMemory();
			//TODO: workingMemory.addEventListener(new DebugWorkingMemoryEventListener());

			TroubleTicket bobTicket = new TroubleTicket("bob");
			TroubleTicket daveTicket = new TroubleTicket("dave");

			Console.WriteLine("----------------------------------------");
			Console.WriteLine("    PRE");
			Console.WriteLine("----------------------------------------");
			Console.WriteLine(bobTicket.ToString());
			Console.WriteLine(daveTicket.ToString());
			Console.WriteLine("----------------------------------------");

			workingMemory.AssertObject(daveTicket);
			workingMemory.AssertObject(bobTicket);
			Console.WriteLine("----------------------------------------");
			Console.WriteLine("    POST ASSERT");
			Console.WriteLine("----------------------------------------");
			Console.WriteLine(bobTicket.ToString());
			Console.WriteLine(daveTicket.ToString());
			Console.WriteLine("----------------------------------------");

			workingMemory.FireAllRules();
			Thread.Sleep(1000);
			Console.WriteLine("----------------------------------------");
			Console.WriteLine("    POST FIRE ALL RULES");
			Console.WriteLine("----------------------------------------");
			Console.WriteLine(bobTicket.ToString());
			Console.WriteLine(daveTicket.ToString());
			Console.WriteLine("----------------------------------------");
			Assert.AreEqual(Status.Notified, bobTicket.Status);
			Assert.AreEqual(Status.New, daveTicket.Status);

			Thread.Sleep(3000);
			Console.WriteLine("----------------------------------------");
			Console.WriteLine("    POST FIRE 1st SLEEP");
			Console.WriteLine("----------------------------------------");
			Console.WriteLine(bobTicket.ToString());
			Console.WriteLine(daveTicket.ToString());
			Console.WriteLine("----------------------------------------");
			Assert.AreEqual(Status.Notified, bobTicket.Status);
			Assert.AreEqual(Status.Notified, daveTicket.Status);

			Thread.Sleep(3000);
			Console.WriteLine("----------------------------------------");
			Console.WriteLine("    POST SLEEP 2nd SLEEP");
			Console.WriteLine("----------------------------------------");
			Console.WriteLine(bobTicket.ToString());
			Console.WriteLine(daveTicket.ToString());
			Console.WriteLine("----------------------------------------");
			Assert.AreEqual(Status.Completed, bobTicket.Status);
			Assert.AreEqual(Status.Completed, daveTicket.Status);
		}
	}
}




