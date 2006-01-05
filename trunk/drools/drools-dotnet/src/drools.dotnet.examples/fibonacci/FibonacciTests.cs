using System;
using NUnit.Framework;
using org.drools.@event;
using org.drools.semantics.dotnet;
using System.IO;

namespace org.drools.dotnet.examples.fibonacci
{
	[TestFixture]
	public class FibonacciTests : ExampleBase
	{
		[Test]
		public void TestFibonacciExample()
		{
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/fibonacci.csharp.drl.xml", UriKind.Relative));
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