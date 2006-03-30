using org.drools.spi;
using java.util;
using java.lang;
using System;
using System.Collections.Generic;
using System.Reflection;

namespace org.drools.semantics.dotnet
{
	/// <summary>
	/// This class should be implemented once it can be used thru configuration 
	/// rather than having to set it explicitly.
	/// </summary>
	public class DotNetImporter : Importer
	{
		private Set _importEntrySet = Collections.EMPTY_SET;
		private Set _importSet = Collections.EMPTY_SET;

		public DotNetImporter()
		{
		}

		public DotNetImporter(Set importEntries)
		{
			_importEntrySet = importEntries;
		}

		public void addImport(ImportEntry ie)
		{
			if (_importEntrySet == Collections.EMPTY_SET)
			{
				_importEntrySet = new HashSet();
			}
			_importEntrySet.add(ie);
		}

		public Set getImportEntries()
		{
			return _importEntrySet;
		}

		public Set getImports()
		{
			if (!_importEntrySet.isEmpty())
			{
				if (_importSet == Collections.EMPTY_SET)
				{
					_importSet = new HashSet();
				}

				Iterator i = _importEntrySet.iterator();
				while (i.hasNext())
				{
					_importSet.add(((ImportEntry)i.next()).getImportEntry());
				}
			}
			return _importSet;
		}

		public Type importType(string typeName)
		{
            string typeNameWithoutAssembly = typeName;
			//Try loading the type - this is necessary so the IKVM class loader works.
			Type type = Type.GetType(typeName);

            //If the rulebase is being loaded from a decision table then create correct type name 
            //In decision table the 
            if (typeName.IndexOf(".ASSEMBLY") != -1)
            {
                string correctTypeName = typeName.Replace(".ASSEMBLY.", ",");
                type = Type.GetType(correctTypeName);
            }

            if (type == null && typeName.IndexOf(",") != -1)
            {
                typeNameWithoutAssembly = typeName.Substring(0, typeName.IndexOf(","));
                type = Type.GetType(typeNameWithoutAssembly);
            }

			if (type == null)
			{
                //Try to get the type for fully qualified class names
                List<Type> validTypes = new List<Type>();
                foreach (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies())
                {
                    Type validType = assembly.GetType(typeName);
                    if (validType == null) validType = assembly.GetType(typeNameWithoutAssembly);
                    if (validType != null) validTypes.Add(validType);
                }

                //Try with the import entries
                if (validTypes.Count == 0)
                {
                    Iterator i = getImports().iterator();

                    while (i.hasNext())
                    {
                        string testTypeName = (string)i.next() + "." + typeName;
                        foreach (Assembly assembly in AppDomain.CurrentDomain.GetAssemblies())
                        {
                            Type validType = assembly.GetType(testTypeName);
                            if (validType != null) validTypes.Add(validType);
                        }
                    }
                }

				if (validTypes.Count == 1)
				{
					type = validTypes[0];
				}
				else if (validTypes.Count > 1)
				{
					System.Text.StringBuilder sb = new System.Text.StringBuilder();
					foreach (Type t in validTypes)
					{
						if (sb.Length > 0) sb.Append(", ");
						sb.Append(t.FullName);
					}
					throw new Error("Unable to find unambiguously defined class '" +
						typeName + "', candidates are: [" + sb.ToString() + "]");
				}
			}

			//Unable to find type
			if (type == null)
			{
				throw new System.Exception("Unable to find type '" + typeName + "'");
			}

			//Type found
			return type;
		}

		public Class importClass(ClassLoader cl, string className)
		{
			Type type = importType(className);
			Class clazz = null;

			try
			{
				clazz = cl.loadClass(type.AssemblyQualifiedName);
			}
			catch (ClassNotFoundException e)
			{
				throw new ClassNotFoundException("Unable to find class '" + className + "'", e);
			}
			return clazz;
		}

		/// <summary>
		/// Prepare a type of be loaded thru the IKVM class loader as partial assembly names are 
		/// not allowed to be specified as part of the type name 
		/// (i.e. "mynamespace.myentity, myassembly") so the assembly must be explicitly 
		/// loaded into the application domain and the type name must be stripped of the 
		/// assembly name.
		/// </summary>
		/// <param name="type">Name of the .NET type of load</param>
		/// <returns>Type name suitable for use with the IKVM class loader</returns>
		internal static string PrepareTypeForIKVMClassLoader(string typeName)
		{
			string assemblyName = typeName.IndexOf(',') > 0 ?
				typeName.Substring(typeName.IndexOf(',') + 1).Trim() : null;

			string clTypeName = typeName;
			if (assemblyName != null && assemblyName.Length > 0)
			{
			    clTypeName = typeName.Substring(0, typeName.IndexOf(','));
			}

			return "cli." + clTypeName;
		}

		public bool isEmpty()
		{
			return _importEntrySet.isEmpty();
		}
	}
}
