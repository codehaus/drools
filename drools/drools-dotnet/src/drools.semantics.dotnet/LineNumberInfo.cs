using System;
#if FRAMEWORK11
	using System.Collections;
#else
	using System.Collections.Generic;
#endif
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
                if (!IntTryParse(c.getAttribute(NODELINESTART), out lineStart))
                    return null;
                if (!IntTryParse(c.getAttribute(NODELINEEND), out lineEnd))
                    return null;
                if (!IntTryParse(c.getAttribute(NODECOLUMNSTART), out columnStart))
                    return null;
                if (!IntTryParse(c.getAttribute(NODECOLUMNEND), out columnEnd))
                    return null;
                fileName = c.getAttribute(NODEFILENAME);
                Uri uri = new Uri(fileName);
                fileName = uri.Segments[uri.Segments.Length - 1];
                if ( fileName== null)
                    return null;     
               
            }
            catch (Exception) // for any exception, return null
            {
                return null;
            }
            return new LineNumberInfo(lineStart, lineEnd, columnStart, columnEnd, fileName);

        }

		private static bool IntTryParse(string val, out int result)
		{
#if FRAMEWORK11
			bool parseOk = false;
			try
			{
				result = int.Parse(val);
				parseOk = true;
			}
			catch (Exception)
			{
				result = 0;
			}
			return parseOk;
#else
			return int.TryParse(val, out result);
#endif
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
