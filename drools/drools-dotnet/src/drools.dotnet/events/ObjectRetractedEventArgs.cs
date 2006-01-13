using System;

namespace org.drools.dotnet.events
{
	/// <summary>
	/// Provides data for the <see cref="WorkingMemory.ObjectRetracted"/> event. 
	/// </summary>
	public class ObjectRetractedEventArgs : EventArgs
	{
		private object _object;

		public ObjectRetractedEventArgs(object o)
		{
			_object = o;
		}

		public object Object
		{
			get { return _object; }
		}
	}
}
