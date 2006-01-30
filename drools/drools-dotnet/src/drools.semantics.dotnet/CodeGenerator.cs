using System;
using System.CodeDom;
using org.drools.rule;
using System.Collections.Generic;
using org.drools.spi;
using System.Reflection;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// Generates .NET code using CodeDOM
	/// </summary>
	public class CodeGenerator
	{
		public static CodeCompileUnit CreateCondition(string namespaceName, string className, 
			string methodName, Declaration[] parameters, string expression, 
			DotNetImporter importer, DotNetFunctions functions,LineNumberInfo info)
		{
			try
			{
				CodeCompileUnit code = GenerateClassCode(namespaceName, className, methodName, parameters,
					typeof(bool));
				CodeMemberMethod method = (CodeMemberMethod)code.Namespaces[0].Types[0].Members[0];                
				CodeStatement returnStatement = new CodeMethodReturnStatement(
					new CodeSnippetExpression(expression));
                if (info != null && info.FileName != null && info.StartLine != -1)
                {
                    int count = GetCountOfStartingLineFeeds(expression);
                    returnStatement.LinePragma = new CodeLinePragma(info.FileName, info.StartLine+count);
                }                
				method.Statements.Add(returnStatement);

				code = AddFunctions(code, functions);
				code = AddReferencedAssemblies(code);
				code = AddImports(code, importer);

				return code;
			}
			catch (Exception e)
			{
				throw new CodeGenerationException("Unable to create condition for expression [" + 
					expression + "]", e);
			}
		}

		public static CodeCompileUnit CreateConsequence(string namespaceName, string className,
			string methodName, Declaration[] parameters, string expression,
			DotNetImporter importer, DotNetFunctions functions,LineNumberInfo info)
		{
			try
			{
				CodeCompileUnit code = GenerateClassCode(namespaceName, className, methodName, parameters,
					typeof(void));
				CodeMemberMethod method = (CodeMemberMethod)code.Namespaces[0].Types[0].Members[0];
                CodeSnippetExpression expr = new CodeSnippetExpression(expression);
                method.Statements.Add(expr); 
                if (info != null && info.FileName != null && info.StartLine != -1)                    
                {
                    if (method.Statements.Count > 0)
                    {
                        //int count = GetCountOfStartingLineFeeds(expression);
                        method.Statements[0].LinePragma
                            = new CodeLinePragma(info.FileName, info.StartLine);
                    }                    
                }                                
				code = AddFunctions(code, functions);
				code = AddReferencedAssemblies(code);
				code = AddImports(code, importer);

				return code;
			}
			catch (Exception e)
			{
				throw new CodeGenerationException("Unable to create consequence for expression [" +
					expression + "]", e);
			}
		}

        private static int GetCountOfStartingLineFeeds(string text)
        {
            int count = 0;
            int loopIndex = text.IndexOf("\n");
            String loopText = text;
            while (loopIndex != -1)
            {
                String text1 = loopText.Substring(0, loopIndex);
                if (text1.Trim().Length == 0)
                {
                    count++;
                }
                else
                    break;
                loopText = loopText.Substring(loopIndex + 1);
                loopIndex = loopText.IndexOf("\n");
            }
            return count;
        }

		private static CodeCompileUnit GenerateClassCode(string namespaceName, string className,
			string methodName, Declaration[] parameters, Type returnType)
		{
			//Generate class code
			CodeCompileUnit code = new CodeCompileUnit();
			CodeNamespace namespaceCode = new CodeNamespace(namespaceName);
			code.Namespaces.Add(namespaceCode);
			CodeTypeDeclaration classCode = new CodeTypeDeclaration(className);
			namespaceCode.Types.Add(classCode);
			CodeMemberMethod methodCode = new CodeMemberMethod();
			classCode.Members.Add(methodCode);
			methodCode.Name = methodName;
			methodCode.ReturnType = new CodeTypeReference(returnType);
			methodCode.Attributes = MemberAttributes.Public;

			foreach (Declaration d in parameters)
			{
				DotNetObjectType o = d.getObjectType() as DotNetObjectType;
				if (o == null)
				{
					throw new Exception("Parameters must be of type " + typeof(DotNetObjectType).FullName + ".");
				}
				CodeParameterDeclarationExpression parameter = new
					CodeParameterDeclarationExpression(o.Type, d.getIdentifier());
				parameter.Direction = FieldDirection.In;
				methodCode.Parameters.Add(parameter);
			}

			CodeParameterDeclarationExpression droolsParam = new CodeParameterDeclarationExpression(
				typeof(KnowledgeHelper), "drools");
			droolsParam.Direction = FieldDirection.In;
			methodCode.Parameters.Add(droolsParam);            
			return code;
		}

		private static CodeCompileUnit AddFunctions(CodeCompileUnit code, DotNetFunctions functions)
		{
			if (functions != null)
			{
				CodeSnippetTypeMember functionMember = new CodeSnippetTypeMember(functions.getText());
                if (functions.LineNumberInfo != null)
                {
                    functionMember.LinePragma = new CodeLinePragma
                        (functions.LineNumberInfo.FileName, functions.LineNumberInfo.StartLine);
                }
				code.Namespaces[0].Types[0].Members.Add(functionMember);
			}
			return code;
		}

		private static CodeCompileUnit AddImports(CodeCompileUnit code, DotNetImporter importer)
		{
			IList<string> imports = new List<string>();

			//Look thru class for types to import
			foreach (CodeTypeMember member in code.Namespaces[0].Types[0].Members)
			{
				CodeMemberMethod method = member as CodeMemberMethod;
				if (method != null)
				{
					//Return Type
					string returnTypeNS = GetNamespace(method.ReturnType);
					if (! imports.Contains(returnTypeNS)) imports.Add(returnTypeNS);

					//Parameters
					foreach(CodeParameterDeclarationExpression parameter in method.Parameters)
					{
						string parameterNS = GetNamespace(parameter.Type);
						if (! imports.Contains(parameterNS)) imports.Add(parameterNS);
					}
				}
			}

			//Import any types specified by importer
            if (importer != null)
            {
                foreach (string entry in importer.getImports().toArray(new string[] { }))
                {
                    if (!imports.Contains(entry)) imports.Add(entry);
                }
            }

			//Add to code
			foreach (string import in imports)
			{
				code.Namespaces[0].Imports.Add(new CodeNamespaceImport(import));
			}
			return code;
		}

		private static CodeCompileUnit AddReferencedAssemblies(CodeCompileUnit code)
		{
			foreach (CodeTypeMember member in code.Namespaces[0].Types[0].Members)
			{
				CodeMemberMethod method = member as CodeMemberMethod;
				if (method != null)
				{
					//Return Type
					string returnTypeAssembly = GetAssemblyReference(method.ReturnType);
					if (! code.ReferencedAssemblies.Contains(returnTypeAssembly))
						code.ReferencedAssemblies.Add(returnTypeAssembly);

					//Parameters
					foreach (CodeParameterDeclarationExpression parameter in method.Parameters)
					{
						string parameterAssembly = GetAssemblyReference(parameter.Type);
						if (! code.ReferencedAssemblies.Contains(parameterAssembly))
							code.ReferencedAssemblies.Add(parameterAssembly);
					}
				}
			}
			return code;
		}

		private static string GetNamespace(CodeTypeReference typeRef)
		{
			return SearchAppDomainForType(typeRef.BaseType).Namespace;
		}

		private static string GetAssemblyReference(CodeTypeReference typeRef)
		{
			return SearchAppDomainForType(typeRef.BaseType).Assembly.Location;
		}

		private static Type SearchAppDomainForType(string typeName)
		{
			Type type = null;
			foreach (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies())
			{
				type = assembly.GetType(typeName);
				if (type != null) return type;
			}
			if (type == null) throw new Exception("Unable to find type [" + typeName + "].");
			return type;
		}
	}
}
