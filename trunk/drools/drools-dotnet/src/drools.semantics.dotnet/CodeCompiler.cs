using System;
using System.Reflection;
using System.CodeDom;
using System.CodeDom.Compiler;
using Microsoft.CSharp;
using System.IO;
using System.Text;
using System.Configuration;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// Compiles .NET code
	/// </summary>
	public class CodeCompiler
	{
        public static Assembly Compile(CodeCompileUnit code)
        {
            return Compile(code, true, false, false,null);
        }

		public static Assembly Compile
            (CodeCompileUnit code, bool generateInMemory, 
            bool includeDebug, bool generateExecutable, 
            string outputAssembly)
		{
			CodeDomProvider provider = GetProvider();
			CompilerParameters compilerParameters = new CompilerParameters();
			compilerParameters.GenerateInMemory = generateInMemory;
			compilerParameters.IncludeDebugInformation = includeDebug;
			compilerParameters.GenerateExecutable = generateExecutable;
            if (!generateInMemory) compilerParameters.OutputAssembly = outputAssembly;
            
            
			CompilerResults results = provider.CompileAssemblyFromDom(compilerParameters,
				new CodeCompileUnit[1] { code });            
			if (results.Errors.Count > 0)
			{
				StringBuilder sb = new StringBuilder();
				sb.Append("Unable to compile source code.  See below for errors and source.");
				sb.Append(Environment.NewLine);
				sb.Append(Environment.NewLine);
				sb.Append("Compiler Errors:");
				sb.Append(Environment.NewLine);

				//Append error list
				foreach (CompilerError error in results.Errors)
				{
					sb.Append(error.ToString());
					sb.Append(Environment.NewLine);
				}

				//Append source
				StringWriter sr = new StringWriter();
				provider.GenerateCodeFromCompileUnit(code, sr, new CodeGeneratorOptions());
				string source = sr.ToString();
				sr.Close();

				sb.Append(Environment.NewLine);
				sb.Append("Source Code:");
				sb.Append(Environment.NewLine);
				sb.Append(source);

				throw new CodeCompilationException(sb.ToString());
			}
			return results.CompiledAssembly;
		}

		private static CodeDomProvider GetProvider()
		{
			string providerName = ConfigurationManager.AppSettings["drools.dotnet.codedomprovider"];
			if (providerName != null && providerName.Trim().Length > 0)
			{
				Type providerType = Type.GetType(providerName);
				if (providerType == null)
				{
					throw new CodeGenerationException("Unable to find CodeDomProvider [" +
						providerName + "]");
				}
				return (CodeDomProvider) Activator.CreateInstance(providerType);
			}
			else
			{
				return new CSharpCodeProvider();
			}

		}
	}
}
