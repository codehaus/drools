package emailEngine;

import java.io.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import org.dom4j.*;
import org.dom4j.io.*;
import org.dom4j.datatype.*;

import emailEngine.*;

/**
 * A factory to manage the creation of event objects from thier 
 * XML representation.  Since this class only creates other 
 * classes it is implemented using the Singleton pattern.
 *
 * <P>
 * $Id: EventFactory.java,v 1.1 2002-05-16 05:09:18 mhald Exp $
 * @version $Revision: 1.1 $
 * @author <a href="mailto:martin.hald@bigfoot.com">Martin Hald &lt;martin.hald@bigfoot.com&gt;</a>
 */

public class EventFactory
   {
   /**
    * The constructor is protected since this class uses a singleton
    * pattern.
    */

   protected EventFactory()
      {
      }

   static private EventFactory _instance = null;

   /**
    * Returns the fully instantiated instance of the class.
    * @return an instance of the EventFactory 
    */

   static public EventFactory getInstance() 
      {
      if (null == _instance) 
         {
         _instance = new EventFactory();
         }
      return _instance;
      }

   /**
    * Translate an event from the XML format and into an
    * event object.  For reasons of effeciency the XML parsing 
    * is handled by the caller and this method expects an Element
    * object as the parameter.
    * <p>
    * The return element is of type event, which is the base class
    * that all events share.  The actual object will be an Email,
    * Calendar or other higher level event object.
    * @param   node  the event element object
    * @return  the event
    */

   public Event processEvent(Element node)
      {
      Event event = new Event();
      if (node!=null && node.attribute("type")!=null && 
          node.attribute("type").getData().equals("email"))
         {
         event.setType(Event.EMAIL);
         event.setDate((String)node.attribute("date").getData());

         // Create a new InboundEmail event object and fill it
         // with the event data.
         InboundEmail email = new InboundEmail();
         email.setSender(getData(node.element("from")));
         email.setRecipient(getData(node.element("to")));
         email.setCarbonCopy(getData(node.element("cc")));
         email.setSubject(getData(node.element("subject")));

         // Loop through the extra headers if there are any.  Note: we cannot
         // use the dom4j XPath query methods here because dom4j internally uses a
         // singleton pattern and we don't want to corrupt the outer query.
         for ( Iterator outerIter = node.elementIterator("headers"); outerIter.hasNext(); )
            {
            Element outerElement = (Element)outerIter.next();
            for ( Iterator innerIter = outerElement.elementIterator("header"); innerIter.hasNext(); )
               {
               Element element = (Element)innerIter.next();
               email.addHeader(element.attributeValue("name"),(String)element.getData());
               }
            }

         return email;
         }
      else
         {
         System.out.println("Unknown type of event");
         return new Event();
         }
      }

   /**
    * Return the data for an element if it exists, otherwise
    * return an empty string.
    * @param   element  the element object
    * @return  the data from the object or an empty string if there is none
    */

   private String getData(Element element)
      {
      if (element!=null)
         {
         return (String)element.getData();
         }
      else
         {
         return "";
         }
      }
   }
