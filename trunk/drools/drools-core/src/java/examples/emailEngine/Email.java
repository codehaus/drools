package emailEngine;

import java.util.*;

/**
 * Class that wraps a single email event.
 *
 * <P>
 * $Id: Email.java,v 1.1 2002-05-16 05:09:18 mhald Exp $
 * @version $Revision: 1.1 $
 * @author <a href="mailto:martin.hald@bigfoot.com">Martin Hald &lt;martin.hald@bigfoot.com&gt;</a>
 */

public class Email extends Event
   {
   private String from = "";
   private String to = "";
   private String cc = "";
   private String subject = "";
   private boolean important = false;
   private Hashtable headers = new Hashtable();
   private HashMap filters = new HashMap();

   /**
    * Sets the senders information.
    * @param sender  Any information about the sender (aka name and email address)
    */

   public void setSender(String sender)
      {
      from = sender;
      }

   /**
    * Returns the senders information.
    * @return  The information about the sender (aka name and email address)
    */

   public String getSender()
      {
      return from;
      }

   /**
    * Sets the recipients information.
    * @param recipient  Any information about the recipient (aka name and email address)
    */

   public void setRecipient(String recipient)
      {
      to = recipient;
      }

   /**
    * Returns the emails to field.
    * @return  The information about the recipient (aka name and email address)
    */

   public String getRecipient()
      {
      return to;
      }

   /**
    * Sets the CC information.
    * @param caronCopy  The CC field (aka name and email address)
    */

   public void setCarbonCopy(String carbonCopy)
      {
      cc = carbonCopy;
      }

   /**
    * Returns the emails cc field.
    * @return  The information about any carbon copied users  (aka names and email addresses)
    */

   public String getCarbonCopy()
      {
      return cc;
      }

   /**
    * Set the emails subject.
    * @param subject  The subject line
    */

   public void setSubject(String subject)
      {
      this.subject = subject;
      }

   /**
    * Returns the emails subject.
    * @return  The emails subject.
    */

   public String getSubject()
      {
      return subject;
      }

   /**
    * Add a header to the email.  Normally this method is only used to keep
    * track of user defined headers (the headers that start with "X-").
    * @param name The name of the header
    * @param value The string to the header equals
    */

   public void addHeader(String name, String value)
      {
      headers.put(name,value);
      }

   /**
    * Returns the header value for the passed header name.  If there
    * is no such header then the function just returns and emptry string.
    * @return  The header value
    */

   public String getHeader(String name)
      {
      if (headers.containsKey(name))
         {
         return (String)headers.get(name);
         }
      else
         {
         return "";
         }
      }

   /**
    * Adds a filter to the email.  Filters allow rules to trigger
    * the firing of related rules.  Filters can be thought of as
    * a feature group associated with the email.  Filters do not
    * have any data associated with them -- they just exist tied
    * to the email.
    * @param name The name of the filter
    */

   public void addFilter(String name)
      {
      filters.put(name,null);
      }

   /**
    * Removes a filter from the email.
    * @param name The name of the filter
    */

   public void removeFilter(String name)
      {
      filters.remove(name);
      }

   /**
    * Returns true if and only if the email has been tagged with a
    * filter matching the passed name.
    * @return  True if the filter exists
    */

   public boolean hasFilter(String name)
      {
      return filters.containsKey(name);
      }

   /**
    * Returns true if and only if the email has no filters.
    * @return  True if the email has no filers
    */

   public boolean hasNoFilters()
      {
      return (filters.size()==0);
      }

   /**
    * A online line string to describe the email.
    * @return  Some vital stats about the email
    */

   public String toString()
      {
      StringBuffer buffer = new StringBuffer();
      buffer.append("Email from=\""+getSender()+"\"");
      if (cc.length()>0)
         buffer.append("\n         cc=\""+getCarbonCopy()+"\"");
      buffer.append("\n         subject=\""+getSubject()+"\"");
      if (getHeader("X-Priority").equals("1"))
         {
         buffer.append("\n         ** MARKED IMPORTANT ** ");
         }
      buffer.append("\n");
      return buffer.toString();
      }
   }
