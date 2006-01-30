using System;
using org.drools.rule;
using org.drools.smf;
using org.drools.spi;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// .NET Function Factory
	/// </summary>
	public class DotNetFunctionsFactory : FunctionsFactory
	{
		public Functions newFunctions(RuleSet rs, RuleBaseContext rbc, Configuration c)
		{
			try
			{
                return new DotNetFunctions(c.getText(), LineNumberInfo.Retrieve(c));
			}
			catch (Exception e)
			{
				throw new FactoryException(e);
			}
		}
	}
}
