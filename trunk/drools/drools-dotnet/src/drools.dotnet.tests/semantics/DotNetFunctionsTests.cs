using System;
using NUnit.Framework;
using NMock;
using org.drools.semantics.dotnet;
using org.drools.smf;
using org.drools.spi;

namespace org.drools.dotnet.tests.semantics
{
	[TestFixture]
	public class DotNetFunctionsTests
	{
		[Test]
		public void TestClass()
		{
			Functions function = new DotNetFunctions("test function text",null);
			Assert.AreEqual("test function text", function.getText());
			Assert.AreEqual("dotnet", function.getSemantic());
		}

		[Test]
		public void TestFactory()
		{
			Mock configMock = new DynamicMock(typeof(Configuration));
			Configuration config = (Configuration) configMock.MockInstance;
			configMock.ExpectAndReturn("getText", "test function text", null);

			FunctionsFactory factory = new DotNetFunctionsFactory();
			Functions function = factory.newFunctions(null, null, config);
			configMock.Verify();

			Assert.AreEqual("test function text", function.getText());
			Assert.AreEqual("dotnet", function.getSemantic());
		}
	}
}
