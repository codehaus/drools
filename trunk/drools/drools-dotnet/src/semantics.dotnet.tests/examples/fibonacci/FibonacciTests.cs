using System;
using NUnit.Framework;
using org.drools.@event;
using org.drools.semantics.dotnet;
using System.IO;

namespace org.drools.semantics.dotnet.tests.examples.fibonacci
{
	[TestFixture]
	public class FibonacciTests
	{
		[Test]
		public void TestFibonacciExample()
		{
			RuleBase ruleBase = DotNetRuleBaseLoader.Load(
				new FileInfo("./examples/fibonacci/fibonacci.dotnet.drl.xml"));
			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			workingMemory.addEventListener(new DebugWorkingMemoryEventListener());

			Fibonacci fibonacci;
			long start;
			long stop;

			fibonacci = new Fibonacci(50);
			start = DateTime.Now.Ticks;
			workingMemory.assertObject(fibonacci);
			workingMemory.fireAllRules();
			stop = DateTime.Now.Ticks;

			Console.WriteLine("First Run: fibonacci(" + fibonacci.Sequence + ") == " +
				fibonacci.Value + " took " + (stop - start) / 10000 + "ms");
			Assert.AreEqual(50, fibonacci.Sequence);
			Assert.AreEqual(12586269025, fibonacci.Value);

			workingMemory = ruleBase.newWorkingMemory();
			fibonacci = new Fibonacci(50);
			start = DateTime.Now.Ticks;
			workingMemory.assertObject(fibonacci);
			workingMemory.fireAllRules();
			stop = DateTime.Now.Ticks;
			Console.WriteLine("Second Run: fibonacci(" + fibonacci.Sequence + ") == " +
				fibonacci.Value + " took " + (stop - start) / 10000 + "ms");
			Assert.AreEqual(50, fibonacci.Sequence);
			Assert.AreEqual(12586269025, fibonacci.Value);
		}
	}
}