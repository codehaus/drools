using System;
using System.IO;
using System.Collections.Generic;
using System.Reflection;
using org.drools.io;
using org.drools.spi;
using java.io;
using java.net;
using org.drools.conflict;

namespace org.drools.dotnet
{
	/// <summary>
	///	This is the main entry point for the Drools engine.
    /// 
    /// Load a rulebase from the rule "DRL" source.
    /// From a RuleBase you obtain a Working Memory (RuleBase.newWorkingMemory())/ 
    /// This working memory is the "engine" that you interact with, by asserting objects etc.
    /// 
    /// The RuleBase can and should be cached (you only need one instance of it). If you wish to 
    /// change rules on the fly, then you will need a way to expire this cache so a new RuleBase can be swapped in
    /// with the latest rules.
	/// </summary>
	public class RuleBaseLoader
	{
		public static RuleBase LoadFromStream(Stream stream)
		{
			return RuleBaseLoader.LoadFromStream(stream, DefaultConflictResolver.getInstance());
		}

		public static RuleBase LoadFromStream(Stream stream, ConflictResolver resolver)
		{
			return RuleBaseLoader.LoadFromStream(
				new Stream[] { stream }, resolver);
		}

		public static RuleBase LoadFromStream(Stream[] streams)
		{
			return RuleBaseLoader.LoadFromStream(streams, DefaultConflictResolver.getInstance());
		}

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
			return org.drools.io.RuleBaseLoader.loadFromInputStream(byteStreams, resolver);
		}

		public static RuleBase LoadFromUri(Uri uri)
		{
			return RuleBaseLoader.LoadFromUri(uri, DefaultConflictResolver.getInstance());
		}

		public static RuleBase LoadFromUri(Uri uri, ConflictResolver resolver)
		{
			return RuleBaseLoader.LoadFromUri(
				new Uri[] { uri }, resolver);
		}

		public static RuleBase LoadFromUri(Uri[] uris)
		{
			return RuleBaseLoader.LoadFromUri(uris, DefaultConflictResolver.getInstance());
		}

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
			return org.drools.io.RuleBaseLoader.loadFromUrl(urls, resolver);
		}

		public static RuleBase LoadFromAssembly(Assembly assembly)
		{
			return LoadFromAssembly(assembly, DefaultConflictResolver.getInstance());
		}

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
