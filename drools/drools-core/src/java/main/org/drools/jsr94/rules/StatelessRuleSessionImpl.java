package org.drools.jsr94.rules;

/*
 $Id: StatelessRuleSessionImpl.java,v 1.2 2003-03-22 00:41:19 tdiesler Exp $

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
import org.drools.RuleBase;
import org.drools.jsr94.rules.admin.RuleExecutionSetImpl;
import org.drools.jsr94.rules.admin.RuleExecutionSetRepository;

import javax.rules.InvalidRuleSessionException;
import javax.rules.ObjectFilter;
import javax.rules.RuleExecutionSetNotFoundException;
import javax.rules.StatelessRuleSession;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface is a representation of a stateless rules engine session.
 * A stateless rules engine session exposes a stateless rule execution API to an underlying rules engine.
 *
 * @see StatelessRuleSession
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class StatelessRuleSessionImpl extends RuleSessionImpl implements StatelessRuleSession {

   /** the rule set from the repository */
   private RuleExecutionSetImpl ruleSet;

   /** The rule base this session is associated with. */
   private RuleBase ruleBase;

   /**
    * Gets the <code>RuleExecutionSet</code> for this URI and associated it with a RuleBase.
    *
    * @param bindUri the URI the <code>RuleExecutionSet</code> has been bound to
    * @throws RuleExecutionSetNotFoundException if there is no rule set under the given URI
    */
   StatelessRuleSessionImpl(String bindUri) throws RuleExecutionSetNotFoundException {

      // get the rule set from the repository
      RuleExecutionSetRepository repository = RuleExecutionSetRepository.getInstance();
      ruleSet = (RuleExecutionSetImpl)repository.getRuleExecutionSet(bindUri);
      if (ruleSet == null) throw new RuleExecutionSetNotFoundException("RuleExecutionSet unbound: " + bindUri);

      ruleBase = ruleSet.getRuleBase();
   }

   /**
    * Executes the rules in the bound rule execution set using the supplied list of objects
    * until no rule is executable anymore.
    * A List is returned over all objects created by the executed rules
    * that pass the default <code>RuleExecutionSet</code>  <code>ObjectFilter</code> (if present).

    * @see StatelessRuleSession#executeRules(List)
    */
   public List executeRules(List list) throws InvalidRuleSessionException {

      // Note: this breaks the factory intension of RuleBase.createWorkingMemory
      JSR94WorkingMemory workingMemory = new JSR94WorkingMemory(ruleBase);

      try {
         for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            workingMemory.assertObject(obj);
         }
      } catch (AssertionException ex) {
         throw new InvalidRuleSessionException(ex.getMessage(), ex);
      }

      List outList = workingMemory.getObjectList();

      // apply the default filter
      ObjectFilter objectFilter = ruleSet.getObjectFilter();
      if (objectFilter != null) {

         // apply the filter
         List cpyList = new ArrayList();
         for (int i = 0; i < outList.size(); i++) {
            Object obj = objectFilter.filter(outList.get(i));
            if (obj != null) cpyList.add(obj);
         }
         outList = cpyList;
      }

      return outList;
   }

   /**
    * Executes the rules in the bound rule execution set using the supplied list of objects until no rule is executable anymore.
    * An iterator is returned over all objects created by the executed rules and filtered with the supplied object filter.

    * @see StatelessRuleSession#executeRules(List,ObjectFilter)
    */
   public List executeRules(List list, ObjectFilter objectFilter) throws InvalidRuleSessionException {

      // Note: this breaks the factory intension of RuleBase.createWorkingMemory
      JSR94WorkingMemory workingMemory = new JSR94WorkingMemory(ruleBase);

      try {
         for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            workingMemory.assertObject(obj);
         }
      } catch (AssertionException ex) {
         throw new InvalidRuleSessionException(ex.getMessage(), ex);
      }

      List outList = workingMemory.getObjectList();

      // apply the filter
      List cpyList = new ArrayList();
      for (int i = 0; i < outList.size(); i++) {
         Object obj = objectFilter.filter(outList.get(i));
         if (obj != null) cpyList.add(obj);
      }

      return cpyList;
   }

   /**
    * Releases all resources used by this rule session.
    * This method renders this rule session unusable until it is reacquired through the RuleRuntime.
    */
   public void release() {
   }
}
