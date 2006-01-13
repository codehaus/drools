using System;
using System.IO;
using System.Collections.Generic;
using System.Reflection;
using org.drools.io;
using org.drools.spi;
using java.io;
using java.net;
using org.drools.conflict;

namespace org.drools.dotnet.io
{
	/// <summary>
	///	Load one or more rules sets for in the Drools engine.
	/// </summary>
	public static class RuleBaseLoader
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
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\", UriKind.Absolute);
			URL[] urls = new URL[uris.Length];
			int count = 0;
			foreach (Uri uri in uris)
			{
				if (! uri.IsAbsoluteUri)
				{
					urls[count] = new URL(new Uri(baseUri, uri).AbsoluteUri);
				}
				else
				{
					urls[count] = new URL(uri.AbsoluteUri);
				}
				count++;
			}
			return new RuleBase(org.drools.io.RuleBaseLoader.loadFromUrl(urls, resolver));
		}

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
			List<Stream> streams = new List<Stream>();
			foreach (string resource in assembly.GetManifestResourceNames())
			{
				if (resource.EndsWith(".drl.xml"))
				{
					streams.Add(assembly.GetManifestResourceStream(resource));
				}
			}
			return LoadFromStream(streams.ToArray(), resolver);
		}
	}
}
