package org.drools.jsr94.rules;

/*
 $Id: JSR94WorkingMemory.java,v 1.2 2003-03-22 00:41:19 tdiesler Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide access to the list of objects currently asserted to the working memory.
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class JSR94WorkingMemory extends WorkingMemory {

   /** contains the objects currently in the working memory. */
   private List objectList = new ArrayList();

   /**
    * Construct a new working memory for a ruleBase.
    *
    *  @param ruleBase The rule base with which this memory is associated.
    */
   JSR94WorkingMemory(RuleBase ruleBase) {
      super(ruleBase);
   }

   /**
    * Get a list of objects currently asserted to the working memory.
    */
   List getObjectList() {
      return objectList;
   }

   /** Assert a new fact object into this working memory.
    *
    *  @param object The object to assert.
    *
    *  @throws AssertionException if an error occurs during assertion.
    */
   public synchronized void assertObject(Object object) throws AssertionException {
      super.assertObject(object);
      objectList.add(object);
   }

   /** Retract a fact object from this working memory.
    *
    *  @param object The object to retract.
    *
    *  @throws RetractionException if an error occurs during retraction.
    */
   public synchronized void retractObject(Object object) throws RetractionException {
      super.retractObject(object);
      objectList.remove(object);
   }
}
