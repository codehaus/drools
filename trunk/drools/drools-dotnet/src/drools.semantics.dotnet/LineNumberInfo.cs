using System;
using System.Collections.Generic;
using System.Text;
using org.drools.smf;
using org.drools.io;
using javalang = java.lang;

namespace org.drools.semantics.dotnet
{
    public class LineNumberInfo
    {

        public const string NODELINESTART = "NODLINESTART";
        public const string NODELINEEND = "NODELINEEND";
        public const string NODECOLUMNSTART = "NODECOLUMNSTART";
        public const string NODECOLUMNEND = "NODECOLUMNEND";
        public const string NODEFILENAME = "NODEFILENAME";

        private int _startLine = -1;
        private int _startColumn = -1;
        private int _endLine = -1;
        private int _endColumn = -1;
        private string _fileName = null;

        private LineNumberInfo() { }
        public LineNumberInfo(int startLine, int endLine, int startColumn, int endColumn, string fileName):this()
        {
            //char[] chars = "NODELINESTART".ToCharArray();
            //javalang.StringBuffer buf = new javalang.StringBuffer("NODELINESTART");            
            _startLine = startLine;
            _startColumn = startColumn;
            _endLine = endLine;
            _endColumn = endColumn;
            _fileName = fileName;
        }

        public static LineNumberInfo Retrieve(Configuration c)
        {
            int lineStart = -1;
            int lineEnd = -1;
            int columnStart = -1;
            int columnEnd = -1;
            string fileName   = null;
            try
            {
                if (!int.TryParse(c.getAttribute(NODELINESTART), out lineStart))
                    return null;
                if (!int.TryParse(c.getAttribute(NODELINEEND), out lineEnd))
                    return null;
                if (!int.TryParse(c.getAttribute(NODECOLUMNSTART), out columnStart))
                    return null;
                if (!int.TryParse(c.getAttribute(NODECOLUMNEND), out columnEnd))
                    return null;
                fileName = c.getAttribute(NODEFILENAME);
                Uri uri = new Uri(fileName);
                fileName = uri.Segments[uri.Segments.Length - 1];
                if ( fileName== null)
                    return null;     
               
            }
            catch (Exception ex) // for any exception, return null
            {
                return null;
            }
            return new LineNumberInfo(lineStart, lineEnd, columnStart, columnEnd, fileName);

        }

        public int StartLine
        {
            get { return _startLine; }
        }
        public int StartColumn
        {
            get { return _startColumn; }
        }
        public int EndLine
        {
            get { return _endLine; }
        }
        public int EndColumn
        {
            get { return _endColumn; }
        }
        public string FileName
        {
            get { return _fileName; }
        }
    }
}
