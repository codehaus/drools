using System;
using org.drools.@event;
using org.drools.dotnet.events;

namespace org.drools.dotnet
{
	/// <summary>
	/// Internal working memory event listener class
	/// </summary>
	internal class EventListener : WorkingMemoryEventListener
	{
		private WorkingMemory _workingMemory;
		public EventListener(WorkingMemory workingMemory)
		{
			_workingMemory = workingMemory;
		}

		public void activationCancelled(ActivationCancelledEvent ace)
		{
			//TODO: Implement these events
		}

		public void activationCreated(ActivationCreatedEvent ace)
		{
			//TODO: Implement these events
		}

		public void activationFired(ActivationFiredEvent afe)
		{
			//TODO: Implement these events
		}

		public void conditionTested(ConditionTestedEvent cte)
		{
			//TODO: Implement these events
		}

		public void objectAsserted(ObjectAssertedEvent oae)
		{
			_workingMemory.OnObjectAsserted(_workingMemory, 
				new ObjectAssertedEventArgs(oae.getObject()));
		}

		public void objectModified(ObjectModifiedEvent ome)
		{
			_workingMemory.OnObjectModified(_workingMemory, 
				new ObjectModifiedEventArgs(ome.getOldObject(),
				ome.getObject()));
		}

		public void objectRetracted(ObjectRetractedEvent ore)
		{
			_workingMemory.OnObjectRetracted(_workingMemory, 
				new ObjectRetractedEventArgs(ore.getOldObject()));
		}
	}
}
