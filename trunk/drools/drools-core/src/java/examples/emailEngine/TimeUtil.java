package emailEngine;

import java.util.*;
import java.text.*;

/**
 * A time utility class to handles intra-day time checks.
 *
 * <P>
 * $Id: TimeUtil.java,v 1.1 2002-05-16 05:09:18 mhald Exp $
 * @version $Revision: 1.1 $
 * @author <a href="mailto:martin.hald@bigfoot.com">Martin Hald &lt;martin.hald@bigfoot.com&gt;</a>
 */

public class TimeUtil
   {
   /**
    * Returns true if and only if the current time
    * is before the miltime parameter.
    * @param time  The time to check against in military format (ranges from 0000 to 2400)
    * @return true if the time is before the passed time, otherwise false 
    */

   public static boolean isCurrentTimeBefore(String miltime)
      {
      int mil1 = Integer.parseInt(miltime);
      int mil2 = ctime();
      return (mil2 < mil1);
      }

   /**
    * Returns true if and only if the current time
    * is after the miltime parameter.
    * @param time  The time to check against in military format (ranges from 0000 to 2400)
    * @return true if the time is after the passed time, otherwise false 
    */

   public static boolean isCurrentTimeAfter(String miltime)
      {
      int mil1 = Integer.parseInt(miltime);
      int mil2 = ctime();
      return (mil2 > mil1);
      }

   /**
    * Return the current time in military format as an integer.  
    * Thereby a time of 2:45 PM would return 1445.
    */

   private static int ctime()
      {
      SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
      String mil2string = formatter.format(new Date());
      return Integer.parseInt(mil2string);
      }
   }
