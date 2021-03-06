using System;
using org.drools.rule;
using org.drools.smf;
using org.drools.spi;
using org.drools.io;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// .NET Condition Factory
	/// </summary>
	public class DotNetConditionFactory : ConditionFactory
	{
		public Condition[] newCondition(Rule r, RuleBaseContext rbc, Configuration c)
		{
			try
			{               
				int id = 0;
				if (rbc.get("dotnet-condition-id") != null)
				{
					id = (int)rbc.get("dotnet-condition-id");
				}
				id++;
				rbc.put("dotnet-condition-id", id);

                return new Condition[] { new DotNetCondition(r, id, c.getText(), 
                    LineNumberInfo.Retrieve(c)) };
			}
			catch (Exception e)
			{
				throw new FactoryException(e);
			}
		}

        
	}
}
