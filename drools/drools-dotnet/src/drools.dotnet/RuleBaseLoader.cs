using System;
using System.IO;
using System.Collections.Generic;
using System.Reflection;
using org.drools.io;
using org.drools.spi;
using java.io;
using java.net;

namespace org.drools.dotnet
{
	/// <summary>
	///	.NET wrapper for RuleBaseLoader
	/// </summary>
	public class RuleBaseLoader
	{
		public static RuleBase LoadFromStream(Stream stream)
		{
			return RuleBaseLoader.LoadFromStream(stream, null);
		}

		public static RuleBase LoadFromStream(Stream stream, ConflictResolver resolver)
		{
			return RuleBaseLoader.LoadFromStream(
				new Stream[] { stream }, resolver);
		}

		public static RuleBase LoadFromStream(Stream[] streams)
		{
			return RuleBaseLoader.LoadFromStream(streams, null);
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
			return RuleBaseLoader.LoadFromUri(uri, null);
		}

		public static RuleBase LoadFromUri(Uri uri, ConflictResolver resolver)
		{
			return RuleBaseLoader.LoadFromUri(
				new Uri[] { uri }, resolver);
		}

		public static RuleBase LoadFromUri(Uri[] uris)
		{
			return RuleBaseLoader.LoadFromUri(uris, null);
		}

		public static RuleBase LoadFromUri(Uri[] uris, ConflictResolver resolver)
		{
			Uri baseUri = new Uri(Path.GetDirectoryName(
				Assembly.GetExecutingAssembly().Location) + "/", UriKind.Absolute);
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
			return LoadFromAssembly(assembly, null);
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
