using org.drools.spi;
using org.drools.rule;
using System.Reflection;
using System.CodeDom;
using Microsoft.CSharp;
using System.Collections.Generic;
using System;
using System.CodeDom.Compiler;
using System.IO;
using System.Text;

namespace org.drools.semantics.dotnet
{
	public class DotNetBlockConsequence : Consequence
	{
		private int _id;
		private Rule _rule;
		private string _expression;
		private Assembly _assembly;
		private string _className;
		private string _methodName = "Invoke";

		public DotNetBlockConsequence(Rule rule, int id, string expression)
		{
			_id = id;
			_rule = rule;
			_expression = expression;
			_className = "Consequence_" + id;
			_assembly = Compile();
		}

		public Assembly Assembly
		{
			get { return _assembly; }
		}

		public void invoke(Tuple t)
		{
			try
			{
				List<object> parameters = new List<object>();
				foreach (Declaration d in _rule.getParameterDeclarations().toArray(new Declaration[] { }))
				{
					parameters.Add(t.get(d));
				}
				parameters.Add(new DefaultKnowledgeHelper(_rule, t));
				object o = _assembly.CreateInstance(this.GetType().Namespace + "." + _className);
				if (o == null) throw new ConsequenceException("Unable to find class: " + _className + " in " + this.ToString(), _rule);
				o.GetType().GetMethod(_methodName).Invoke(o, parameters.ToArray());
			}
			catch (Exception e)
			{
				throw new ConsequenceException(e.GetBaseException(), _rule, "Error executing " + this.ToString());
			}
		}

		private Assembly Compile()
		{
			DotNetFunctions functions = null;
			if (_rule.getRuleSet() != null)
			{
				functions = _rule.getRuleSet().getFunctions("dotnet") as DotNetFunctions;
			}

			DotNetImporter importer = _rule.getImporter() as DotNetImporter;
			if (importer == null) importer = new DotNetImporter();

			//Generate Code
			CodeCompileUnit code = CodeGenerator.CreateConsequence(this.GetType().Namespace,
				_className, _methodName, (Declaration[]) 
				_rule.getParameterDeclarations().toArray(new Declaration[]{}),
				_expression, importer, functions);

			//Generate IL
			return CodeCompiler.Compile(code);
		}

		public override string ToString()
		{
			return "[Consequence: " + _expression + "]";
		}
	}
}
