using System;
using NUnit.Framework;
using System.IO;
using System.Threading;
using org.drools.@event;
using org.drools.dotnet.examples.escalation;
using org.drools.dotnet.io;
using System.Reflection;

namespace org.drools.dotnet.examples.precompilation.escalation
{
	[TestFixture]
	public class EscalationTests
	{
        [Test]
        public void TestPrecompilation()
        {            
            /*
            string path = System.Environment.CurrentDirectory + "\\drls\\escalation.csharp.drl.xml";
            FileStream stream = File.OpenRead(path);
            RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase
                (stream, "escalation.dll");            
            stream.Close();            
             */
            Assembly assembly = null;
#if FRAMEWORK11
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase(
				new Uri(baseUri, "./drls/escalation.csharp.drl.xml"), "escalation.dll", out assembly);
#else
			RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase(
                new Uri("./drls/escalation.csharp.drl.xml", UriKind.Relative), "escalation.dll", out assembly);
#endif
        }
        [Test]
        public void TestEscalationExample()
        {
            /*
            string path = System.Environment.CurrentDirectory + "\\drls\\escalation.csharp.drl.xml";
            FileStream stream = File.OpenRead(path);
            RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase
                (stream, "escalation.dll");
            stream.Close();            
             */
            Assembly assembly = null;
#if FRAMEWORK11
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase(
				new Uri(baseUri, "./drls/escalation.csharp.drl.xml"), "escalation.dll", out assembly);
#else
			RuleBase ruleBase = RuleBaseLoader.LoadPrecompiledRulebase(
                new Uri("./drls/escalation.csharp.drl.xml", UriKind.Relative), "escalation.dll", out assembly); 
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