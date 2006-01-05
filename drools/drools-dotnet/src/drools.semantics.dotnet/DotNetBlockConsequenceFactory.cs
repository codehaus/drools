using System;
using org.drools.rule;
using org.drools.smf;
using org.drools.spi;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// .NET Block Consequence Factory
	/// </summary>
	public class DotNetBlockConsequenceFactory : ConsequenceFactory
	{
		public Consequence newConsequence(Rule r, RuleBaseContext rbc, Configuration c)
		{
			try
			{
				int id = 0;
				if (rbc.get("dotnet-consequence-id") != null)
				{
					id = (int)rbc.get("dotnet-consequence-id");
				}
				id++;
				rbc.put("dotnet-consequence-id", id);

				return new DotNetBlockConsequence(r, id, c.getText());
			}
			catch (Exception e)
			{
				throw new FactoryException(e);
			}
		}
	}
}