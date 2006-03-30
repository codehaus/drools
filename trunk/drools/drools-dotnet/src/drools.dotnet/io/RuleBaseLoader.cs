using System;
using System.IO;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections;
	using System.Collections.Generic;
#endif
using System.Reflection;
using org.drools.io;
using org.drools.spi;
using java.io;
using java.net;
using org.drools.conflict;
using ILMerging;
using org.drools.rule;
using org.drools.semantics.dotnet;
using org.drools.dotnet.util;

namespace org.drools.dotnet.io
{
	/// <summary>
	///	Load one or more rules sets for in the Drools engine.
	/// </summary>
#if FRAMEWORK11
	public sealed class RuleBaseLoader
#else
	public static class RuleBaseLoader
#endif
	{
		/// <summary>
		/// Loads a rule set from a stream
		/// </summary>
		/// <param name="stream">Stream to load the ruleset from</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromStream(Stream stream)
		{
			return RuleBaseLoader.LoadFromStream(stream, DefaultConflictResolver.getInstance());
		}

		/// <summary>
		/// Loads a rule set from a stream
		/// </summary>
		/// <param name="streams">Stream to load the ruleset from</param>
		/// <param name="resolver">Conflict resolver</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromStream(Stream stream, ConflictResolver resolver)
		{
			return RuleBaseLoader.LoadFromStream(
				new Stream[] { stream }, resolver);
		}

		/// <summary>
		/// Loads a rule set from multiple steams
		/// </summary>
		/// <param name="stream">Streams to load rulesets from</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromStream(Stream[] streams)
		{
			return RuleBaseLoader.LoadFromStream(streams, DefaultConflictResolver.getInstance());
		}

		/// <summary>
		/// Loads a rule set from multiple streams
		/// </summary>
		/// <param name="streams">Streams to load rulesets from</param>
		/// <param name="resolver">Conflict resolver</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromStream(Stream[] streams, ConflictResolver resolver)
		{
			ByteArrayInputStream[] byteStreams = new ByteArrayInputStream[streams.Length];
			int count = 0;
			foreach (Stream stream in streams)
			{
				byte[] buffer = new byte[stream.Length];
				stream.Read(buffer, 0, buffer.Length);
				byteStreams[count] = new ByteArrayInputStream(buffer);
				count++;
			}
			return new RuleBase(org.drools.io.RuleBaseLoader.loadFromInputStream(byteStreams, resolver));
		}

		/// <summary>
		/// Loads a rule set from a location defined in a uri format
		/// </summary>
		/// <param name="uri">Uri to load the ruleset from</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromUri(Uri uri)
		{
			return RuleBaseLoader.LoadFromUri(uri, DefaultConflictResolver.getInstance());
		}

		/// <summary>
		/// Loads a rule set from a location defined in a uri format
		/// </summary>
		/// <param name="uri">Uri to load the ruleset from</param>
		/// <param name="resolver">Conflict resolver</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromUri(Uri uri, ConflictResolver resolver)
		{
			return RuleBaseLoader.LoadFromUri(
				new Uri[] { uri }, resolver);
		}

		/// <summary>
		/// Loads a rule set from a set of locations defined in a uri format
		/// </summary>
		/// <param name="uris">Uris to load the ruleset from</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromUri(Uri[] uris)
		{
			return RuleBaseLoader.LoadFromUri(uris, DefaultConflictResolver.getInstance());
		}

		/// <summary>
		/// Loads a rule set from a set of locations defined in a uri format
		/// </summary>
		/// <param name="uris">Uris to load the ruleset from</param>
		/// <param name="resolver">Conflict resolver</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromUri(Uri[] uris, ConflictResolver resolver)
		{
#if FRAMEWORK11
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
#else
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\", UriKind.Absolute);
#endif
			URL[] urls = new URL[uris.Length];
			int count = 0;
			foreach (Uri uri in uris)
			{
#if FRAMEWORK11	//The 1.1 framework Uri class only supports absolute uris.
				urls[count] = new URL(uri.AbsoluteUri);
#else
				if (! uri.IsAbsoluteUri)
				{
					urls[count] = new URL(new Uri(baseUri, uri).AbsoluteUri);
				}
				else
				{
					urls[count] = new URL(uri.AbsoluteUri);
				}
#endif
				count++;
			}            
			return new RuleBase(org.drools.io.RuleBaseLoader.loadFromUrl(urls, resolver));
		}

		#region Load from relative uri functions

#if !FRAMEWORK11
		[Obsolete("Don't use LoadFromRelativeUri.  Instead use LoadFromUri and pass in a Uri created using UriKind.Relative", false)]
#endif
		public static RuleBase LoadFromRelativeUri(string uriPath)
		{
			return RuleBaseLoader.LoadFromRelativeUri(
				uriPath, DefaultConflictResolver.getInstance()); 
		}

#if !FRAMEWORK11
		[Obsolete("Don't use LoadFromRelativeUri.  Instead use LoadFromUri and pass in a Uri created using UriKind.Relative", false)]
#endif
		public static RuleBase LoadFromRelativeUri(string uriPath, ConflictResolver resolver)
		{
			return RuleBaseLoader.LoadFromRelativeUri(
				new string[] { uriPath }, resolver);
		}

#if !FRAMEWORK11
		[Obsolete("Don't use LoadFromRelativeUri.  Instead use LoadFromUri and pass in a Uri created using UriKind.Relative", false)]
#endif
		public static RuleBase LoadFromRelativeUri(string[] uriPaths)
		{
			return RuleBaseLoader.LoadFromRelativeUri(
				uriPaths, DefaultConflictResolver.getInstance());
		}

#if !FRAMEWORK11
		[Obsolete("Don't use LoadFromRelativeUri.  Instead use LoadFromUri and pass in a Uri created using UriKind.Relative", false)]
#endif
		public static RuleBase LoadFromRelativeUri(string[] uriPaths, ConflictResolver resolver)
		{
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			Uri[] uris = new Uri[uriPaths.Length];
			for (int i=0; i < uriPaths.Length; i++)
			{
				uris[i] = new Uri(baseUri, uriPaths[i]);
			}
			return RuleBaseLoader.LoadFromUri(uris, resolver);
		}

		#endregion

		/// <summary>
		/// Loads a rule set using all the embedded resources in the assembly that end with .drl.xml
		/// </summary>
		/// <param name="assembly">Assembly to search</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromAssembly(Assembly assembly)
		{
			return LoadFromAssembly(assembly, DefaultConflictResolver.getInstance());
		}

		/// <summary>
		/// Loads a rule set using all the embedded resources in the assembly that end with .drl.xml
		/// </summary>
		/// <param name="assembly">Assembly to search</param>
		/// <param name="resolver">Conflict resolver</param>
		/// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
		public static RuleBase LoadFromAssembly(Assembly assembly, ConflictResolver resolver)
		{
#if FRAMEWORK11
			ArrayList streams = new ArrayList();
#else
			List<Stream> streams = new List<Stream>();
#endif
			foreach (string resource in assembly.GetManifestResourceNames())
			{
				if (resource.EndsWith(".drl.xml"))
				{
					streams.Add(assembly.GetManifestResourceStream(resource));
				}
			}
#if FRAMEWORK11
			Stream[] tempStreams = new Stream[streams.Count];
			streams.CopyTo(tempStreams);
			return LoadFromStream(tempStreams, resolver);
#else
			return LoadFromStream(streams.ToArray(), resolver);
#endif
		}

        /// <summary>
        /// Loads a rule set from a text reader
        /// </summary>
        /// <param name="reader">TextReader</param>
        /// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
        /// 
        public static RuleBase LoadFromReader(System.IO.TextReader reader)
        {
            return RuleBaseLoader.LoadFromReader(reader, DefaultConflictResolver.getInstance());
        }

        /// <summary>
        /// Loads a rule set from a Text Reader
        /// </summary>
        /// <param name="reader">reader to load the ruleset from</param>
        /// <param name="resolver">Conflict resolver</param>
        /// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
        public static RuleBase LoadFromReader(System.IO.TextReader reader, ConflictResolver resolver)
        {
            return RuleBaseLoader.LoadFromReader(new System.IO.TextReader[] { reader }, resolver);
        }

        /// <summary>
        /// Loads a rule set from a set of Text Reader
        /// </summary>
        /// <param name="readers">readers to load the ruleset from</param>
        /// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
        public static RuleBase LoadFromReader(System.IO.TextReader[] readers)
        {
            return RuleBaseLoader.LoadFromReader(readers, DefaultConflictResolver.getInstance());
        }

        /// <summary>
        /// Loads a rule set from a set of Text Reader
        /// </summary>
        /// <param name="readers">readers to load the ruleset from</param>
        /// <param name="resolver">Conflict resolver</param>
        /// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
        public static RuleBase LoadFromReader(System.IO.TextReader[] readers, ConflictResolver resolver)
        {
            InputStreamReader[] streamReaders = new InputStreamReader[readers.Length];
            int count = 0;
            foreach (TextReader reader in readers)
            {
                string readerString = reader.ReadToEnd();
                
                StringBufferInputStream inpStr = new StringBufferInputStream(new String(readerString.ToCharArray()));
                streamReaders[count] = new InputStreamReader(inpStr);
                count++;
            }
            return new RuleBase(drools.io.RuleBaseLoader.loadFromReader(streamReaders, resolver));
        }

        #region Load precompiled rulebase functions

        public static RuleBase LoadPrecompiledRulebase(Stream stream, string precompiledAssembly, out Assembly assembly)
        {
            return RuleBaseLoader.LoadPrecompiledRulebase(stream, DefaultConflictResolver.getInstance(), precompiledAssembly, out assembly);

        }
        public static RuleBase LoadPrecompiledRulebase(Stream stream, ConflictResolver resolver, string precompiledAssembly, out Assembly assembly)
        {
            RuleBase _rulebase = RuleBaseLoader.LoadFromStream(stream, resolver);
            assembly = loadAssembly(_rulebase, precompiledAssembly);
            return _rulebase;
        }
        
        public static RuleBase LoadPrecompiledRulebase(Uri uri, string precompiledAssembly, out Assembly assembly)
        {
            return RuleBaseLoader.LoadPrecompiledRulebase(uri, DefaultConflictResolver.getInstance(), precompiledAssembly, out assembly);
        }

        public static RuleBase LoadPrecompiledRulebase(Uri uri, ConflictResolver resolver, string precompiledAssembly, out Assembly assembly)
        {
            RuleBase _rulebase = RuleBaseLoader.LoadFromUri(uri, resolver);
            assembly = loadAssembly(_rulebase, precompiledAssembly);
            return _rulebase;
        }

        private static void SetRuleSetCtxProperties(RuleBase rb, string assemblyName)
        {
            string assemblyPrefix = "";
            if (assemblyName != null && assemblyName.EndsWith(".dll"))
                assemblyPrefix = assemblyName.Substring(0, assemblyName.IndexOf(".dll"));
            IEnumerator enumer = rb.RuleSets.GetEnumerator();
            while(enumer.MoveNext())
            {
                RuleSet ruleSet = enumer.Current as RuleSet;
                ruleSet.getRuleBaseContext().put("IsPrecompiled", true);
                ruleSet.getRuleBaseContext().put("AssemblyName", assemblyName);
                ruleSet.getRuleBaseContext().put("AssemblyPrefix", assemblyPrefix);
            }
        }

        private static Assembly loadAssembly(RuleBase ruleBase, string assemblyName)
        {
            if (assemblyName.IndexOf(":\\") < 0)
                assemblyName = System.Environment.CurrentDirectory + "\\" + assemblyName;
            
            RuleBaseLoader.SetRuleSetCtxProperties(ruleBase, assemblyName);
            Assembly assembly = null;
            try
            {
                assembly = Assembly.LoadFile(assemblyName);
            }
            catch (Exception ex)
            {
                System.Console.WriteLine(ex.Message + " New Assembly will be created");
            }

            // if not found
            if (assembly == null)
            {
                assembly = RuleBaseLoader.createAssembly(ruleBase);
            }
            // Set the assembly on the rulebase.
            Hashtable parameters = new Hashtable();
            parameters["RuleSet"] = ruleBase.RuleSets;
            parameters["Assembly"] = assembly;
            DotNetRuleBaseIterator.Action(DotNetRuleBaseActions.SETASSEMBLY, parameters);
            return assembly;

        }

        private static Assembly createAssembly(RuleBase ruleBase)
        {
			System.Collections.ArrayList assemblies = CreateIntermediateAssemblies(ruleBase);

            if (assemblies.Count == 0)
                return null;
            ILMerge ilmerge = new ILMerge();
#if FRAMEWORK11
            ReadOnlyList listOfRules = ruleBase.RuleSets as ReadOnlyList;
#else
            ReadOnlyList<RuleSet> listOfRules = ruleBase.RuleSets as ReadOnlyList<RuleSet>;
#endif
            RuleSet ruleSet = listOfRules[0] as RuleSet;
            ///ruleBase.RuleSets[0] as RuleSet;
            ilmerge.OutputFile = ruleSet.getRuleBaseContext().get("AssemblyName") as string;
            
            ilmerge.DebugInfo = true;            
            ilmerge.KeyFile = ruleSet.getRuleBaseContext().get("KeyFile") as string;
            ilmerge.SetInputAssemblies((string[])assemblies.ToArray(Type.GetType("System.String")));
            try
            {
                ilmerge.Merge();
            }
            catch (Exception e)
            {
                string error = e.ToString();
            }
            //deleteIntermediateAssemblies(assemblies);
            Assembly assembly = Assembly.LoadFile(ilmerge.OutputFile.ToString());
            return assembly;
        }

        private static System.Collections.ArrayList CreateIntermediateAssemblies(RuleBase ruleBase)
        {
            System.Collections.ArrayList assemblies = null;
            System.Collections.Hashtable compileParams = new System.Collections.Hashtable();
            {
                compileParams["RuleSet"] = ruleBase.RuleSets;
                assemblies
                    = DotNetRuleBaseIterator.Action(DotNetRuleBaseActions.COMPILE, compileParams) as System.Collections.ArrayList;
            }
            return assemblies;
        }
        #endregion

	}
}
