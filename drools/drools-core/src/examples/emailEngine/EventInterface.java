package emailEngine;

/**
 * The collection of methods that all events inherit by extending
 * the Event class.
 *
 * <P>
 * $Id: EventInterface.java,v 1.1 2002-05-16 05:09:18 mhald Exp $
 * @version $Revision: 1.1 $
 * @author <a href="mailto:martin.hald@bigfoot.com">Martin Hald &lt;martin.hald@bigfoot.com&gt;</a>
 */

public interface EventInterface
   {
   /**
    * A static to indicate the event is of an unknown type.
    */

   public static int UNKNOWN = 0;

   /**
    * A static to indicate the event is an email.
    */

   public static int EMAIL = 1;

   /**
    * A static to indicate the event is from the filesystem.
    */

   public static int DOCUMENT = 1;

   /**
    * A static to indicate the event is related to the calendar.
    */

   public static int CALENDAR = 1;
   // ... and more types

   /**
    * Set the type of event.  Valid types are EMAIL, DOCUMENT or CALENDAR.
    * @param type A static int of one of the valid Event.EVENTNAME types
    */

   public void setType(int type);

   /**
    * Get the type of event.
    * @return a static int of the event type
    */

   public int getType();

   /**
    * Set the date the event entered the queue.
    * @param date the date the event entered the queue
    */

   public void setDate(String date);

   /**
    * Get the date that the event was placed into the queue.
    * @return the event queue date
    */
   public String getDate();
   }
