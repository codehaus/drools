using System;

namespace org.drools.dotnet.events
{
	/// <summary>
	/// Provides data for the <see cref="WorkingMemory.ObjectAsserted"/> event. 
	/// </summary>
	public class ObjectAssertedEventArgs : EventArgs
	{
		private object _object;

		public ObjectAssertedEventArgs(object o) 
		{
			_object = o;
		}

		public object Object
		{
			get { return _object; }
		}
	}
}
