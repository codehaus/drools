using System;
using NUnit.Framework;
using System.Configuration;

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
			Assert.AreEqual(providerName, ConfigurationManager.AppSettings[settingName]);
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
		}

		[Test]
		public void TestVisualBasic()
		{
			string providerName = "Microsoft.VisualBasic.VBCodeProvider, System, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089";
			SetProvider(providerName);
			Assert.AreEqual(providerName, ConfigurationManager.AppSettings[settingName]);
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/visualbasic.drl.xml", UriKind.Relative));
		}

		[Test]
		public void TestCSharp()
		{
			string providerName = "Microsoft.CSharp.CSharpCodeProvider, System, Version=2.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089";
			SetProvider(providerName);
			Assert.AreEqual(providerName, ConfigurationManager.AppSettings[settingName]);
			RuleBase rulebase = RuleBaseLoader.LoadFromUri(
				new Uri("./drls/csharp.drl.xml", UriKind.Relative));
		}

		private void SetProvider(string provider)
		{
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
		}
	}
}
