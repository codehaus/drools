package org.drools.misc;



/**
 * A consequence removes an object from a list
 *
 * Autor: thomas.diesler@iotronics.com
 *
 * $Id: Participant.java,v 1.1 2003-08-20 23:50:37 tdiesler Exp $
 */
public class Participant {

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
