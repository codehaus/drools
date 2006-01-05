using System;
using System.Reflection;

using java.lang;
using org.drools.rule;
using org.drools.semantics.@base;
using org.drools.smf;
using org.drools.spi;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// .NET ObjectType Factory
	/// </summary>
	public class DotNetObjectTypeFactory : ObjectTypeFactory
	{
		public ObjectType newObjectType(Rule rule, RuleBaseContext context, Configuration config)
		{
			string typeName = config.getText().Trim();
			if (typeName == null || typeName.Length == 0)
            {
                throw new FactoryException("No type name specified.");
            }

			Type type = null;
			try
			{
				ClassLoader cl = (ClassLoader)context.get("smf-classLoader");
				if (cl == null)
				{
					cl = Thread.currentThread().getContextClassLoader();
					context.put("smf-classLoader", cl);
				}

				if (cl == null)
				{
					cl = context.getClass().getClassLoader();
					context.put("smf-classLoader", cl);
				}

				DotNetImporter importer = rule.getImporter() as DotNetImporter;
				if (importer == null)
				{
					rule.setImporter(new DotNetImporter(rule.getImporter().getImportEntries()));
					importer = (DotNetImporter)rule.getImporter();
					//TODO: Throw this when we use a configurable ImporterFactory
					//throw new System.Exception("Importer for ruleset must be of type " + typeof(DotNetImporter).FullName + ".");
				}
				type = importer.importType(typeName);
			}
			catch (ClassNotFoundException e)
			{
				throw new FactoryException(e.getMessage(), e);
			}
			catch (System.Exception e)
			{
				throw new FactoryException(e.Message, e);
			}
			return new DotNetObjectType(type);
		}
	}
}

