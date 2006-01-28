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
        private LineNumberInfo _info = null;

        public DotNetBlockConsequence(Rule rule, int id, string expression, LineNumberInfo info)
		{
			_id = id;
			_rule = rule;
			_expression = expression;
			_className = "Consequence_" + id;
			_assembly = Compile();
            _info = info;
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

		public Assembly Compile()
		{
			//Generate Code
			CodeCompileUnit code = CodeGenerator.CreateConsequence(this.GetType().Namespace,
				_className, _methodName, (Declaration[]) 
				_rule.getParameterDeclarations().toArray(new Declaration[]{}),
				_expression, _rule.getImporter() as DotNetImporter,
                _rule.getRuleSet().getFunctions("dotnet") as DotNetFunctions, _info);

            // Prepare to generate IL
            RuleBaseContext ctx = _rule.getRuleSet().getRuleBaseContext();
            string assemblyPrefix =
                ctx.get("AssemblyPrefix") as string;
            bool isPrecompiled = false;
            if (ctx.get("IsPrecompiled") != null)
                isPrecompiled = (bool)ctx.get("IsPrecompiled");

			//Generate IL
            if (isPrecompiled)
            {
                return CodeCompiler.Compile
                    (code, false, true, false, assemblyPrefix + "_" + _className + "_" + _methodName + ".dll");
            }
			//Generate IL
			return CodeCompiler.Compile(code);
		}

        public void ResetAssembly(Assembly assembly)
        {
            this._assembly = assembly;
        }


		public override string ToString()
		{
			return "[Consequence: " + _expression + "]";
		}
	}
}
