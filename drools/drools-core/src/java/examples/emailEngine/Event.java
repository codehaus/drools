package emailEngine;

import emailEngine.*;

/**
 * The base object which hold event related information.  All
 * events (emails, documents, calendar meetings, etc.) inherit
 * from this object.
 *
 * <P>
 * $Id: Event.java,v 1.1 2002-05-16 05:09:18 mhald Exp $
 * @version $Revision: 1.1 $
 * @author <a href="mailto:martin.hald@bigfoot.com">Martin Hald &lt;martin.hald@bigfoot.com&gt;</a>
 */

public class Event implements EventInterface
   {
   private int type = UNKNOWN;
   private String date = "";
   private Email email = null;

   /**
    * Set the type of event.  Valid types are EMAIL, DOCUMENT or CALENDAR.
    * @param type A static int of one of the valid Event.EVENTNAME types
    */

   public void setType(int type)
      {
      this.type = type;
      }

   /**
    * Get the type of event.
    * @return a static int of the event type
    */

   public int getType()
      {
      return type;
      }

   /**
    * Set the date the event entered the queue.
    * @param date the date the event entered the queue
    */

   public void setDate(String date)
      {
      this.date = date;
      }

   /**
    * Get the date that the event was placed into the queue.
    * @return the event queue date
    */

   public String getDate()
      {
      return date;
      }

   /**
    * Get a single line string that describes the event.
    * @return a single line description
    */

   public String toString()
      {
      StringBuffer buffer = new StringBuffer();
      buffer.append("Event: \n  type="+typeString(type)+"\n");
      buffer.append("  when="+date+"\n");
      if (type==EMAIL)
         buffer.append("  email: "+super.toString());
      return buffer.toString();
      }

   /**
    * Returns the string that describes the type of event.
    * @return a string name of the event
    */

   private String typeString(int type)
      {
      if (type==EMAIL)
         return "EMAIL";
      else if (type==DOCUMENT)
         return "DOCUMENT";
      else if (type==CALENDAR)
         return "CALENDAR";
      else
         return "UNKNOWN";
      }
   }
