using System;
using org.drools.spi;
using org.drools.rule;
using System.Reflection.Emit;
using org.drools.semantics.@base;
using System.Collections.Generic;
using Microsoft.CSharp;
using System.CodeDom.Compiler;
using System.CodeDom;
using System.Text;
using System.Reflection;
using System.IO;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// .NET Condition
	/// </summary>
	public class DotNetCondition : Condition
	{
		private Declaration[] _requiredParameters;
		private int _id;
		private Rule _rule;
		private string _expression;
		private Assembly _assembly;
		private string _className;
		private string _methodName = "Invoke";

		public DotNetCondition(Rule rule, int id, string expression)
		{
			_id = id;
			_rule = rule;
			_expression = expression;
			_className = "Condition_" + id;

			//TODO: Determine required parameters instead of defaulting to all parameters
			_requiredParameters = (Declaration[]) rule.getParameterDeclarations().toArray(
				new Declaration[]{ });
			_assembly = Compile();
		}

		public org.drools.rule.Declaration[] getRequiredTupleMembers()
		{
			return _requiredParameters;
		}

		public bool isAllowed(Tuple t)
		{
			try
			{
				List<object> parameters = new List<object>();
				foreach (Declaration d in _requiredParameters)
				{
					parameters.Add(t.get(d));
				}
				parameters.Add(new DefaultKnowledgeHelper(_rule, t));
				object o = _assembly.CreateInstance(this.GetType().Namespace + "." + _className);
				if (o == null) throw new ConditionException("Unable to find class: " + _className + " in " + this.ToString(), _rule, String.Empty);
				return (bool)o.GetType().GetMethod(_methodName).Invoke(o, parameters.ToArray());
			}
			catch (Exception e)
			{
				throw new ConditionException(e, _rule, "Error executing " + this.ToString());
			}
		}

		private Assembly Compile()
		{
			//Generate Code
			CodeCompileUnit code = CodeGenerator.CreateCondition(this.GetType().Namespace, 
				_className, _methodName, _requiredParameters, _expression, _rule.getImporter() as DotNetImporter,
				_rule.getRuleSet().getFunctions("dotnet") as DotNetFunctions);

			//Generate IL
			return CodeCompiler.Compile(code);
		}

		public override string ToString()
		{
			return "[Condition: " + _expression + "]";
		}
	}
}
