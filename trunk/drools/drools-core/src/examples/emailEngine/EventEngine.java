package emailEngine;

import java.io.*;
import java.util.*;
import java.net.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.dom4j.*;
import org.dom4j.io.*;
import org.dom4j.datatype.*;

import org.drools.*;
import org.drools.spi.*;
import org.drools.semantic.java.*;

import emailEngine.*;

/**
 * The event engine handles runs the events in the event queue
 * through the users rules, thereby executing whatever actions
 * need to be executed.
 *
 * <P>
 * $Id: EventEngine.java,v 1.1 2002-05-16 05:09:18 mhald Exp $
 * @version $Revision: 1.1 $
 * @author <a href="mailto:martin.hald@bigfoot.com">Martin Hald &lt;martin.hald@bigfoot.com&gt;</a>
 */

public class EventEngine
   {
   private static String eventsFile = "example_data/email_engine/event_queue.xml";
   private static String userFile = "example_data/email_engine/conf_user.xml";
   private static String userTrans = "example_data/email_engine/translate.xsl";
   private static File sysFile = new File("example_data/email_engine/system_rules.xml");

   /**
    * Starts the event handling process from the shell.
    * @param args command line parameters
    */

   public static void main(String[] args) throws Exception
      {
      // Get the command line parameter (if any).
      String arg = "";
      if (args.length>0)
         arg = args[0];

      // Get the users rules in a string object.
      System.out.println("Loading user rules");
      String userRulesXML = getUserRules();

      // Check for command line parameters and dump any 
      // required data back to the user if needed.
      if (arg.equals("-rules"))
         {
         System.out.println("Dumping rules to screen");
         System.out.println(prettyPrint(userRulesXML));
         }

      // Get the events from the event queue.  Each event is
      // encoded into an object of the matching event type. 
      // Therefore a series of email events will result in a
      // vector of email objects.
      System.out.println("Loading events");
      Vector events = getEvents();

      if (arg.equals("-events"))
         {
         System.out.println("Dumping events to screen");
         Enumeration eventList = events.elements();
         while (eventList.hasMoreElements())
            {
            System.out.println((Event)eventList.nextElement());
            }
         }

      // First, construct an empty RuleBase to be the
      // container for your rule logic.
      RuleBase ruleBase = new RuleBase();

      // Then add the users rules as well as the system rules.  Here
      // the users rules are passed as a String Reader and the system
      // rules are passed as a file object and the loading of the 
      // rules is handled within the rule loader.
      RuleLoader.load(ruleBase, new StringReader(userRulesXML));
      RuleLoader.load(ruleBase, sysFile);

      // Create a [org.drools.WorkingMemory] to be the
      // container for your facts
      WorkingMemory workingMemory = ruleBase.createWorkingMemory();

      System.out.println("   ... processing events\n");
      try
         {
         // Now, loop through all the events and for each event assert it
         // into the [org.drools.WorkingMemory] and let the logic engine 
         // do the rest.
         Enumeration eventList = events.elements();
         while (eventList.hasMoreElements())
            {
            Event event = (Event)eventList.nextElement();
            System.out.println("  "+event);
            workingMemory.assertObject(event);
            System.out.println();
            }
         }
      catch (AssertionException e)
         {
         e.printStackTrace();
         }

      }

   /**
    * Translate and return the XML event queue into a vector
    * of event objects that all enherit from the base event 
    * class.
    * @return  a vector of events
    */

   private static Vector getEvents()
      {
      Vector events = new Vector();
      try
         {
         EventFactory factory = EventFactory.getInstance();
         Document schema =  DocumentHelper.parseText(readFile(eventsFile));

         // Loop through each event
         XPath xpathSelector = DocumentHelper.createXPath("/queue/*");
         List results = xpathSelector.selectNodes(schema);
         for ( Iterator iter = results.iterator(); iter.hasNext(); ) 
            {
            Element element = (Element)iter.next();
            Event event = factory.processEvent(element);
            events.add(event);
            }
         }
      catch (Exception e)
         {
         System.err.println("Could not load events: "+e);
         }
      return events;
      }

   /**
    * Translate users rules into a drools rule file and return the
    * newly created rules.  The original users rules are stored in
    * a pure XML format, and through pushing the user XML rules 
    * through an XSLT process the drools rules (a combination of 
    * XML and Java) are created.
    * @return the users Drools rules
    */

   private static String getUserRules()
      {
      // Set the rule XML in case the generation of users 
      // rules fails.
      String xml = "<ruleset name=\"Failed Generation\"></ruleset>";
      try
         {
         // create File and Stream objects
         File dataFile  = new File(userFile);  // data
         File styleFile = new File(userTrans);  // stylesheet
         InputStream dataStream = new FileInputStream(dataFile);
         InputStream styleStream = new FileInputStream(styleFile);

         // create XSLT Source and Result objects
         Source data = new StreamSource(dataStream);
         Source style = new StreamSource(styleStream);
         StringWriter string = new StringWriter();
         Result output = new StreamResult(string);
         System.out.println("   ... xml rules loaded");

         // create Transformer and perform the tranfomation
         Transformer xslt =
           TransformerFactory.newInstance().newTransformer(style);
         xslt.transform(data, output);
         System.out.println("   ... xml rules translated into drools");

         xml = string.getBuffer().toString();
         }
      catch (Exception e)
         {
         System.out.println("Cannot load user rules: "+e);
         }
      // return the rules as a string
      return xml;
      }

   /**
    * Return a nice rendering of the users rules.
    * @param   rules the Drools rules
    * @return  the text rendering of the XML rules
    */

   private static String prettyPrint(String rules)
      {
      try
         {
         StringWriter string = new StringWriter();
         Document document = DocumentHelper.parseText(rules);
         OutputFormat format = OutputFormat.createPrettyPrint();
         XMLWriter writer = new XMLWriter( string, format );
         writer.write( document );
         return string.getBuffer().toString();
         }
      catch (Exception e)
         {
         System.out.println("Error formatting XML for printing: "+e); 
         return "";
         }
      }

   /**
    * Read the users rules in from file.
    * @param path the relative (or full) path to the file
    * @return  the file contents
    */

   private static String readFile(String path) 
      {
      StringBuffer contents = new StringBuffer();
      try
         {
         FileInputStream in = new FileInputStream(path);
         InputStreamReader isr = new InputStreamReader(in);
         int ch=0;
         while((ch = in.read())> -1)
            {
            contents.append((char)ch);
            }
         }
      catch (IOException e)
         {
         System.out.println(e.getMessage());
         }
      return contents.toString();
      }
   }
