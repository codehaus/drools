package org.drools.misc;

import junit.framework.TestCase;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleSetLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A consequence removes an object from a list
 *
 * Autor: thomas.diesler@iotronics.com
 *
 * $Id: RemoveFromListTest.java,v 1.1 2003-08-20 23:50:37 tdiesler Exp $
 */
public class RemoveFromListTest extends TestCase {

   private WorkingMemory workingMemory;

   public RemoveFromListTest (String name) {
      super(name);
   }

   public void setUp () throws Exception {

      RuleBase ruleBase = new RuleBase();

      RuleSetLoader loader = new RuleSetLoader();
      URL url = getClass().getResource("RemoveFromListTest.drl");
      assertNotNull("cannot find drl file", url);
      loader.load(url, ruleBase);

      workingMemory = ruleBase.createWorkingMemory();
   }

   public void testRemoveFromList () throws Exception {

      ArrayList list = new ArrayList();
      list.add(new Participant(false));
      list.add(new Participant(true));

      Iterator it = list.iterator();
      while (it.hasNext()) {
         Participant pt = (Participant) it.next();
         workingMemory.assertObject(pt);
      }
   }

   /**
    * Participant
    */
   public static class Participant {

      private boolean active;

      public Participant (boolean active) {
         this.active = active;
      }

      public boolean isActive () {
         return active;
      }

      public void setActive (boolean active) {
         this.active = active;
      }
   }
}
