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
			_workingMemory.OnActivationCancelled(_workingMemory,
				new ActivationCancelledEventArgs());
		}

		public void activationCreated(ActivationCreatedEvent ace)
		{
			_workingMemory.OnActivationCreated(_workingMemory,
				new ActivationCreatedEventArgs());
		}

		public void activationFired(ActivationFiredEvent afe)
		{
			_workingMemory.OnActivationFired(_workingMemory,
				new ActivationFiredEventArgs());
		}

		public void conditionTested(ConditionTestedEvent cte)
		{
			_workingMemory.OnConditionTested(_workingMemory,
				new ConditionTestedEventArgs());
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
