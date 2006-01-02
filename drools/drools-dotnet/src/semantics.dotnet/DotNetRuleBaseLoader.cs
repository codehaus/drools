using System.IO;
using System.Reflection;
using org.drools.io;
using java.io;

//TODO: This should go into a managed utilities class
namespace org.drools.semantics.dotnet
{
	/// <summary>
	///	.NET Rule Loader
	/// </summary>
	public class DotNetRuleBaseLoader
	{
		public static RuleBase Load(FileInfo file)
		{
			return RuleBaseLoader.loadFromInputStream(new FileInputStream(file.FullName));
		}

		public static RuleBase Load(TextReader reader)
		{
			string rulesData = reader.ReadToEnd();
			return RuleBaseLoader.loadFromReader(new java.io.StringReader(rulesData));
		}

		public static RuleBase Load(Assembly assembly, string name)
		{
			StreamReader reader = new StreamReader(
				assembly.GetManifestResourceStream(name));
			return Load(reader);
		}
	}
}
