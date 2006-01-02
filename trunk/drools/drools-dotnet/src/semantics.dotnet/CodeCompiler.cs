using System;
using System.Reflection;
using System.CodeDom;
using System.CodeDom.Compiler;
using Microsoft.CSharp;
using System.IO;
using System.Text;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// Compiles .NET code
	/// </summary>
	internal class CodeCompiler
	{
		public static Assembly Compile(CodeCompileUnit code)
		{
			//TODO: Use specific compiler from configuration
			CodeDomProvider provider = new CSharpCodeProvider();
			CompilerParameters compilerParameters = new CompilerParameters();
			compilerParameters.GenerateInMemory = true;
			compilerParameters.IncludeDebugInformation = false;
			compilerParameters.GenerateExecutable = false;

			StringWriter sr = new StringWriter();
			provider.GenerateCodeFromCompileUnit(code, sr, new CodeGeneratorOptions());
			string source = sr.ToString();
			sr.Close();
			 

			CompilerResults results = provider.CompileAssemblyFromDom(compilerParameters,
				new CodeCompileUnit[1] { code });

			if (results.Errors.Count > 0)
			{
				StringBuilder sb = new StringBuilder();
				foreach (CompilerError error in results.Errors)
				{
					sb.Append("Error compiling code:" + Environment.NewLine + source);
					sb.Append(Environment.NewLine);
					sb.Append(error.ToString());
				}
				throw new Exception(sb.ToString());
			}
			return results.CompiledAssembly;
		}
	}
}
