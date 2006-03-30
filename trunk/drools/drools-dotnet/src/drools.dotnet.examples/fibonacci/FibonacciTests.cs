using System;
using NUnit.Framework;
using System.IO;
using org.drools.dotnet.io;

namespace org.drools.dotnet.examples.fibonacci
{
	[TestFixture]
	public class FibonacciTests
	{
		[Test]
		public void TestFibonacciExample()
		{
#if FRAMEWORK11
			RuleBase ruleBase = RuleBaseLoader.LoadFromRelativeUri(
				"./drls/fibonacci.csharp.drl.xml");
#else
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/fibonacci.csharp.drl.xml", UriKind.Relative));
#endif
			WorkingMemory workingMemory = ruleBase.GetNewWorkingMemory();
			//workingMemory.addEventListener(new DebugWorkingMemoryEventListener());

			Fibonacci fibonacci;
			long start;
			long stop;

			fibonacci = new Fibonacci(50);
			start = DateTime.Now.Ticks;
			workingMemory.AssertObject(fibonacci);
			workingMemory.FireAllRules();
			stop = DateTime.Now.Ticks;

			Console.WriteLine("First Run: fibonacci(" + fibonacci.Sequence + ") == " +
				fibonacci.Value + " took " + (stop - start) / 10000 + "ms");
			Assert.AreEqual(50, fibonacci.Sequence);
			Assert.AreEqual(12586269025, fibonacci.Value);

			workingMemory = ruleBase.GetNewWorkingMemory();
			fibonacci = new Fibonacci(50);
			start = DateTime.Now.Ticks;
			workingMemory.AssertObject(fibonacci);
			workingMemory.FireAllRules();
			stop = DateTime.Now.Ticks;
			Console.WriteLine("Second Run: fibonacci(" + fibonacci.Sequence + ") == " +
				fibonacci.Value + " took " + (stop - start) / 10000 + "ms");
			Assert.AreEqual(50, fibonacci.Sequence);
			Assert.AreEqual(12586269025, fibonacci.Value);
		}
	}
}