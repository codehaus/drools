using System;
//using System.Collections.Generic;
using System.Text;
using org.drools.decisiontable;
using System.IO;
using java.io;

namespace org.drools.dotnet.io
{
    /// <summary>
    /// This class is a utility class for loading decision tables from a stream. Can
    /// be used as an alternative to RuleBaseLoader, for decision tables.
    /// Example usage: <code>
    /// RuleBase rb = DecisionTableLoader.loadFromStream(streamToSpreadsheet);
    /// typically you will want to cache the built RuleBase, for performance
    /// to use the rulebase:
    /// WorkingMemory engine = rb.GetNewWorkingMemory();
    /// engine.AssertObject(yourObject);
    /// engine.FireAllRules();
    /// </summary>
    public class DecisionTableLoader
    {
              
        /// <summary>
        /// Load a rulebase from a decision table spreadsheet. 
        /// </summary>
        /// <param name="stream"> Stream to a spreadsheet</param>
        /// <returns><see cref="org.drools.dotnet.RuleBase"/></returns>
        public static RuleBase LoadFromStream(Stream stream)
        {
                SpreadsheetDRLConverter drlConverter = new SpreadsheetDRLConverter();
                
                byte[] buffer = new byte[stream.Length];
                stream.Read(buffer, 0, buffer.Length);
                ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(buffer);

                string drl = drlConverter.convertToDRL(byteArrayInputStream);
                string updatedDrl = javaDrlToCsharpDrl(drl);
                
                return RuleBaseLoader.LoadFromReader(new System.IO.StringReader(updatedDrl));
        }

        /// <summary>
        /// This function replaces java-specific elements with dotnet elements in a DRL.
        /// Ideally this will be refactored with drools 3, and will not require 
        /// </summary>
        /// <param name="javadrl">drl string</param>
        /// <returns></returns>
        private static string javaDrlToCsharpDrl(string javadrl)
        {
            javadrl = javadrl.Replace("class", "dotnet:class");
            javadrl = javadrl.Replace("java:", "dotnet:");
            javadrl = javadrl.Replace("java:condition", "dotnet:condition");
            javadrl = javadrl.Replace("java:consequence", "dotnet:consequence");
            javadrl = javadrl.Replace("java:functions", "dotnet:functions");
            javadrl = javadrl.Replace("xmlns:java=\"http://drools.org/semantics/java\"", "xmlns:dotnet=\"http://drools.org/semantics/dotnet\"");
            javadrl = javadrl.Replace("xs:schemaLocation=\"http://drools.org/rules rules.xsd http://drools.org/semantics/java java.xsd\"", "xs:schemaLocation=\"http://drools.org/rules rules.xsd http://drools.org/semantics/dotnet dotnet.xsd\"");
            return javadrl;
        }

    }
}
