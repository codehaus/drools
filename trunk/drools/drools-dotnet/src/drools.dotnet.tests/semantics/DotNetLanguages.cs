using System;
#if FRAMEWORK11
	using System.Xml;
#endif
using NUnit.Framework;
using System.Configuration;
using org.drools.dotnet.io;

namespace org.drools.dotnet.tests.semantics
{
	[TestFixture]
	public class DotNetLanguages
	{
		private string settingName = "drools.dotnet.codedomprovider";

		[TestFixtureTearDown]
		public void CleanupFixture()
		{
			//Set the provider back to null so the rest of the tests run correctly.
			SetProvider(null);
		}

		[Test]
		public void TestDefault()
		{
			string providerName = null;
			SetProvider(providerName);
#if FRAMEWORK11
			//TODO:Framework11 - Commented out the following assert because 1.1 framework doesn't offer a way to refresh ConfigurationSettings at run-time
			//Assert.AreEqual(providerName, ConfigurationSettings.AppSettings[settingName]);
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri(baseUri, "./drls/csharp.drl.xml"));
#else		
			Assert.AreEqual(providerName, ConfigurationManager.AppSettings[settingName]);
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
#endif
		}

		[Test]
		public void TestVisualBasic()
		{
			string providerName = "Microsoft.VisualBasic.VBCodeProvider, System, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089";
			SetProvider(providerName);
#if FRAMEWORK11
			//TODO:Framework11 - Commented out the following assert because 1.1 framework doesn't offer a way to refresh ConfigurationSettings at run-time
			//Assert.AreEqual(providerName, ConfigurationSettings.AppSettings[settingName]);
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri(baseUri, "./drls/visualbasic.drl.xml"));
#else
			Assert.AreEqual(providerName, ConfigurationManager.AppSettings[settingName]);
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/visualbasic.drl.xml", UriKind.Relative));
#endif
		}

		[Test]
		public void TestCSharp()
		{
			string providerName = "Microsoft.CSharp.CSharpCodeProvider, System, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089";
			SetProvider(providerName);
#if FRAMEWORK11
			//TODO:Framework11 - Commented out the following assert because 1.1 framework doesn't offer a way to refresh ConfigurationSettings at run-time
			//Assert.AreEqual(providerName, ConfigurationSettings.AppSettings[settingName]);
			Uri baseUri = new Uri(AppDomain.CurrentDomain.BaseDirectory + @"\");
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri(baseUri, "./drls/csharp.drl.xml"));
#else
			Assert.AreEqual(providerName, ConfigurationManager.AppSettings[settingName]);
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
#endif
		}

		private void SetProvider(string provider)
		{
#if FRAMEWORK11
			string configFile = System.Reflection.Assembly.GetExecutingAssembly().Location + ".config";
			XmlDocument dom = new XmlDocument();
			try 
			{
				dom.Load(configFile);
			}
			catch 
			{
				string Xml = @"<?xml version='1.0'?><configuration/>";
				dom.LoadXml(Xml);
			}
			XmlNode appSettingsNode = dom.SelectSingleNode(@"/configuration/appSettings");
			if (appSettingsNode != null)
			{
				appSettingsNode.RemoveAll();
			}
			else
			{
				appSettingsNode = dom.CreateNode(XmlNodeType.Element, "appSettings", null);
				dom.DocumentElement.AppendChild(appSettingsNode);
			}
			if (provider != null && provider.Length > 0)
			{
				XmlNode providerNode = dom.CreateNode(XmlNodeType.Element, "add", null);
				XmlAttribute keyAttribute = dom.CreateAttribute("key");
				keyAttribute.Value = settingName;
				providerNode.Attributes.Append(keyAttribute);
				XmlAttribute valueAttribute = dom.CreateAttribute("value");
				valueAttribute.Value = provider;
				providerNode.Attributes.Append(valueAttribute);
				appSettingsNode.AppendChild(providerNode);
			}
			try 
			{
				dom.Save(configFile);
			}
			catch 
			{
			}
#else
			Configuration config = ConfigurationManager.OpenExeConfiguration(
				ConfigurationUserLevel.None);
			config.AppSettings.Settings.Clear();
			if (provider != null && provider.Length > 0)
			{
				config.AppSettings.Settings.Add(settingName,
					provider);
			}
			config.Save(ConfigurationSaveMode.Modified);
			ConfigurationManager.RefreshSection("appSettings");
#endif
		}
	}
}
