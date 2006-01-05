using System;
using NUnit.Framework;
using System.Collections.Generic;
using System.IO;
using org.drools.@event;
using java.util;

namespace org.drools.dotnet.examples.manners
{
	[TestFixture]
	public class MannersTests : ExampleBase
	{
		private int numGuests = 16;
		private int numSeats = 16;
		private int minHobbies = 2;
		private int maxHobbies = 3;
		
		[Test]
		public void TestMannersExample()
		{
			RuleBase ruleBase = RuleBaseLoader.LoadFromUri(new Uri(
				"./drls/manners.csharp.drl.xml", UriKind.Relative));
			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			workingMemory.addEventListener(new DebugWorkingMemoryEventListener());

			IList<Guest> guests = GenerateGuests();
			Context context = new Context("start");
			LastSeat lastSeat = new LastSeat(numSeats);

			long start = DateTime.Now.Ticks;
			workingMemory.assertObject(context);
			workingMemory.assertObject(lastSeat);
			foreach(Guest guest in guests)
			{
				workingMemory.assertObject(guest);
			}
			workingMemory.fireAllRules();
			long stop = DateTime.Now.Ticks;
			Console.Out.WriteLine("Elapsed time: " + (stop - start) / 10000 + "ms");
			
			//Get seats
			/* TODO: This code sucks because you need convert from java types to .NET types
			 * which requires one to liter their project with Java code or add 
			 * support classes (total hack job).  To fix this we need to wrap these 
			 * object and expose them as .NET types or port this to a pure .NET solution.
			 */

			Iterator i = workingMemory.getObjects().iterator();
			IList<Seat> seats = new List<Seat>();
			while(i.hasNext())
			{
				Seat seat = i.next() as Seat;
				if (seat != null) seats.Add(seat);
			}
			Assert.AreEqual(numGuests, seats.Count, "seated guests " + seats.Count + 
				" didn't match expected " + numGuests);

			ValidateResults(guests, seats);
		}

		private void ValidateResults(IList<Guest> guests, IList<Seat> seats)
		{
		    Guest lastGuest = null;
		    foreach (Seat seat in seats)
		    {
		        if (lastGuest == null)
		        {
		            lastGuest = Guest4Seat(guests, seat);
		        }
				Guest guest = Guest4Seat(guests, seat);
				bool hobbyFound = false;
				foreach(string hobby in lastGuest.Hobbies)
				{
					if (guest.Hobbies.Contains(hobby))
					{
						hobbyFound = true;
						break;
					}
				}
				if (!hobbyFound) 
					Assert.Fail("seat: " + seat + " no common hobby " + lastGuest + " -> " + guest);
		    }
		}
		
		private Guest Guest4Seat(IList<Guest> guests, Seat seat)
		{
			foreach (Guest guest in guests)
			{
				if (guest.Name.Equals(seat.Name)) return guest;
			}
			return null;
		}

		private IList<Guest> GenerateGuests()
		{
			int maxMale = numGuests / 2;
			int maxFemale = numGuests / 2;
			int maleCount = 0;
			int femaleCount = 0;

			IList<Guest> guests = new List<Guest>();

			//Init hobbies
			IList<string> allHobbies = new List<string>();
			for (int i = 1; i <= maxHobbies; i++)
			{
				allHobbies.Add("h" + i);
			}

			//Generate Guests
			System.Random rnd = new System.Random();
			for (int i = 1; i <= numGuests; i++)
			{
				//Determine guest sex
				Sex sex = rnd.Next(0, 1) == 1 ? Sex.Male : Sex.Female;
				if (sex.Equals(Sex.Male) && maleCount == maxMale)
				{
					sex = Sex.Female;
				}
				if (sex.Equals(Sex.Female) && femaleCount == maxFemale)
				{
					sex = Sex.Male;
				}
				if (sex.Equals(Sex.Male))
				{
					maleCount++;
				}
				if (sex.Equals(Sex.Female))
				{
					femaleCount++;
				}

				//Set guest hobbies
				int numHobbies = rnd.Next(minHobbies, maxHobbies);
				IList<string> hobbies = new List<string>(allHobbies);
				while (hobbies.Count > numHobbies)
				{
					hobbies.RemoveAt(rnd.Next(0, hobbies.Count - 1));
				}
				
				//Create Guest
				Guest guest = new Guest("Guest_" + i, sex, hobbies);
				Console.WriteLine("Created guest: " + guest.ToString());
				guests.Add(guest);
			}
			return guests;
		}
	}
}