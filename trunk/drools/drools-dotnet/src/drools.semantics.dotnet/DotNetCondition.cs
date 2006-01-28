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
		private string _namespace;
        private LineNumberInfo _info = null;

		public DotNetCondition(Rule rule, int id, string expression, LineNumberInfo info)
		{
			_id = id;
			_rule = rule;
			_expression = expression;
			_className = "Condition_" + id;
			_namespace = this.GetType().Namespace;
            _info = info;

			_requiredParameters = (Declaration[])rule.getParameterDeclarations().toArray(
				new Declaration[] { });			
		}

		public org.drools.rule.Declaration[] getRequiredTupleMembers()
		{
			return _requiredParameters;
		}

		public bool isAllowed(Tuple t)
		{
			try
			{
                if (_assembly == null)
                {
                    _assembly = Compile();
                }
				List<object> parameters = new List<object>();
				foreach (Declaration d in _requiredParameters)
				{
					parameters.Add(t.get(d));
				}
				parameters.Add(new DefaultKnowledgeHelper(_rule, t));
				object o = _assembly.CreateInstance(_namespace + "." + _className);
				if (o == null) throw new ConditionException("Unable to find class: " + _className + " in " + this.ToString(), _rule, String.Empty);
				return (bool)o.GetType().GetMethod(_methodName).Invoke(o, parameters.ToArray());
			}
			catch (Exception e)
			{
				throw new ConditionException(e, _rule, "Error executing " + this.ToString());
			}
		}

		public Assembly Compile()
		{
			//Generate Code
			CodeCompileUnit code = CodeGenerator.CreateCondition(_namespace,
				_className, _methodName, _requiredParameters, _expression, 
                _rule.getImporter() as DotNetImporter,
				_rule.getRuleSet().getFunctions("dotnet") as DotNetFunctions,_info);

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
                    (code, false, true, false, assemblyPrefix+"_"+_className+"_"+_methodName+".dll");
            }
            else
			    return CodeCompiler.Compile(code);
		}

        public void ResetAssembly(Assembly assembly)
        {
            this._assembly = assembly;
        }


		public override string ToString()
		{
			return "[Condition: " + _expression + "]";
		}
	}
}
