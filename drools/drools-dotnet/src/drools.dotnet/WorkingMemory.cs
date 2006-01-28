using System;
using java.util;
using System.Collections.Generic;
using org.drools.dotnet.util;
using org.drools.dotnet.events;

namespace org.drools.dotnet
{
    /// <summary>
    /// This is a wrapper around the core drools working memory. 
    /// For pure .Net clients.
    /// </summary>
	public class WorkingMemory
	{
		private org.drools.WorkingMemory _javaWorkingMemory;

		/// <summary>
		/// Create a new instance of WorkingMemory
		/// </summary>
		/// <param name="javaWorkingMemory">Java WorkingMemory class</param>
		internal WorkingMemory(org.drools.WorkingMemory javaWorkingMemory)
		{
			_javaWorkingMemory = javaWorkingMemory;
			_javaWorkingMemory.addEventListener(new EventListener(this));
		}

		/// <summary>
		/// Asserts an object into working memory
		/// </summary>
		/// <param name="o">Object to assert</param>
		public void AssertObject(object o)
		{
			_javaWorkingMemory.assertObject(o);
		}

		/// <summary>
		/// Clear the agenda
		/// </summary>
		public void ClearAgenda()
		{
			_javaWorkingMemory.clearAgenda();
		}

		/// <summary>
		/// Determines whether the object is contained in working memory 
		/// </summary>
		/// <param name="o">The object to locate in working memory</param>
		/// <returns>true if the object is found in working memory; otherwise, false. </returns>
		public bool ContainsObject(object o)
		{
			FactHandle factHandle;
			try
			{
				factHandle = _javaWorkingMemory.getFactHandle(o);
				return _javaWorkingMemory.containsObject(factHandle);
			}
			catch (NoSuchFactHandleException)
			{
				return false;
			}
		}

		/// <summary>
		/// Fire all items on the agenda until empty.
		/// </summary>
		public void FireAllRules()
		{
			_javaWorkingMemory.fireAllRules();
		}

		//TODO: fireAllRules(AgendaFilter af)

		/// <summary>
		/// A READ-ONLY copy of all application data
		/// </summary>
		public IDictionary<string, object> ApplicationData
		{
			get
			{
				throw new NotImplementedException(
					"The .NET semantics module does not support application data.");
				//return new ReadOnlyDictionary<string, object>(
					//_javaWorkingMemory.getApplicationDataMap());
			}
		}

		/// <summary>
		/// Sets an application data item to a value
		/// </summary>
		/// <param name="identifier">Identifier of the item to set</param>
		/// <param name="value">Value to set the item to</param>
		public void SetApplicationData(string identifier, object value)
		{
			throw new NotImplementedException(
				"The .NET semantics module does not support application data.");
			//_javaWorkingMemory.setApplicationData(identifier, value);
		}

		/// <summary>
		/// A READ-ONLY list of all objects in working memory.
		/// </summary>
		public IList<object> Objects
		{
			get { return new ReadOnlyList<object>(_javaWorkingMemory.getObjects()); }
		}

		/// <summary>
		/// Retrieves all objects of a specific type from working memory.
		/// </summary>
		/// <typeparam name="T">Type of objects to return.</typeparam>
		/// <returns>List of objects found in working memory of the given type.</returns>
		public IList<T> GetObjects<T>()
		{
			return new ReadOnlyList<T>(_javaWorkingMemory.getObjects(
				java.lang.Class.forName(typeof(T).AssemblyQualifiedName)));
		}

		/// <summary>
		/// RuleBase for the working memory
		/// </summary>
		public RuleBase RuleBase
		{
			get { return new RuleBase(_javaWorkingMemory.getRuleBase()); }
		}

		/// <summary>
		/// Modify an object that already exists in working memory.
		/// </summary>
		/// <param name="o">The modified object</param>
		public void ModifyObject(object o)
		{
			_javaWorkingMemory.modifyObject(_javaWorkingMemory.getFactHandle(o), o);
		}

		/// <summary>
		/// Retract an asserted from from working memory
		/// </summary>
		/// <param name="o">The object to retract</param>
		public void RetractObject(object o)
		{
			_javaWorkingMemory.retractObject(_javaWorkingMemory.getFactHandle(o));
		}

		//TODO: setAsyncExceptionHandler

		#region Events
		/// <summary>
		/// Occurs when an activation is cancelled.
		/// </summary>
		public event EventHandler<ActivationCancelledEventArgs> ActivationCancelled;

		/// <summary>
		/// Occurs when an activation is created.
		/// </summary>
		public event EventHandler<ActivationCreatedEventArgs> ActivationCreated;

		/// <summary>
		/// Occurs when an activation is fired.
		/// </summary>
		public event EventHandler<ActivationFiredEventArgs> ActivationFired;

		/// <summary>
		/// Occurs when an condition is tested.
		/// </summary>
		public event EventHandler<ConditionTestedEventArgs> ConditionTested;

		/// <summary>
		/// Occurs when an object is asserted to working memory
		/// </summary>
		public event EventHandler<ObjectAssertedEventArgs> ObjectAsserted;

		/// <summary>
		/// Occurs when an object is modified in working memory
		/// </summary>
		public event EventHandler<ObjectModifiedEventArgs> ObjectModified;

		/// <summary>
		/// Occurs when an object is retracted from working memory
		/// </summary>
		public event EventHandler<ObjectRetractedEventArgs> ObjectRetracted;
		#endregion

		# region Event Callouts
		internal void OnActivationCancelled(object sender, ActivationCancelledEventArgs e)
		{
			if (ActivationCancelled != null) ActivationCancelled(sender, e);
		}

		internal void OnActivationCreated(object sender, ActivationCreatedEventArgs e)
		{
			if (ActivationCreated != null) ActivationCreated(sender, e);
		}

		internal void OnActivationFired(object sender, ActivationFiredEventArgs e)
		{
			if (ActivationFired != null) ActivationFired(sender, e);
		}

		internal void OnConditionTested(object sender, ConditionTestedEventArgs e)
		{
			if (ConditionTested != null) ConditionTested(sender, e);
		}

		internal void OnObjectAsserted(object sender, ObjectAssertedEventArgs e)
		{
			if (ObjectAsserted != null) ObjectAsserted(sender, e);
		}

		internal void OnObjectModified(object sender, ObjectModifiedEventArgs e)
		{
			if (ObjectModified != null) ObjectModified(sender, e);
		}

		internal void OnObjectRetracted(object sender, ObjectRetractedEventArgs e)
		{
			if (ObjectRetracted != null) ObjectRetracted(sender, e);
		}
		# endregion
	}
}
