using System;
using System.Text;

namespace org.drools.dotnet.examples.escalation
{
	public class TroubleTicket
	{
		private string _submitter;
		private Status _status;

		public TroubleTicket(string submitter)
		{
			_submitter = submitter;
			_status = Status.New;
		}

		public virtual string Submitter
		{
			get { return _submitter; }
		}

		public virtual Status Status
		{
			get{ return _status; }
			set{ _status = value; }
		}

		public override string ToString()
		{
			StringBuilder buf = new StringBuilder();

			buf.Append("[TroubleTicket: submitter='");
			buf.Append(Submitter);
			buf.Append("'; status='");
			buf.Append(Status);
			buf.Append("']");
			return buf.ToString();
		}
	}
}