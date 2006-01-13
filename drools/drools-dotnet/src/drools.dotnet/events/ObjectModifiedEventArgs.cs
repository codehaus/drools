using System;

namespace org.drools.dotnet.events
{
	/// <summary>
	/// Provides data for the <see cref="WorkingMemory.ObjectModified"/> event. 
	/// </summary>
	public class ObjectModifiedEventArgs : EventArgs
	{
		private object _oldObject;
		private object _newObject;

		public ObjectModifiedEventArgs(object oldObject, object newObject)
		{
			_oldObject = oldObject;
			_newObject = newObject;
		}

		public object OldObject
		{
			get { return _oldObject; }
		}

		public object NewObject
		{
			get { return _oldObject; }
		}
	}
}
