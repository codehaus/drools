package org.drools.jsr94.rules.admin;

/*
 $Id: RuleAdministratorTestCase.java,v 1.3 2004-04-04 02:54:56 n_alex Exp $

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

import org.drools.jsr94.rules.RuleEngineTestBase;

import javax.rules.admin.*;

/**
 * Test the RuleRuntime implementation.
 *
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class RuleAdministratorTestCase extends RuleEngineTestBase {

   private RuleAdministrator ruleAdministrator;

   /**
    * Obtain an instance of <code>RuleAdministrator</code>.
    */
   protected void setUp() throws Exception {
      super.setUp();
      ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
   }

   /**
    * Test getRuleExecutionSetProvider.
    */
   public void testRuleExecutionSetProvider() throws Exception {
      RuleExecutionSetProvider ruleExecutionSetProvider = ruleAdministrator.getRuleExecutionSetProvider(null);
      assertNotNull("cannot obtain RuleExecutionSetProvider", ruleExecutionSetProvider);
   }

   /**
    * Test getLocalRuleExecutionSetProvider.
    */
   public void testLocalRuleExecutionSetProvider() throws Exception {
      LocalRuleExecutionSetProvider localRuleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(null);
      assertNotNull("cannot obtain LocalRuleExecutionSetProvider", localRuleExecutionSetProvider);
   }

   /**
    * Test registerRuleExecutionSet.
    */
   public void testRegisterRuleExecutionSet() throws Exception {
      try {
         // that it works is tested else where
         ruleAdministrator.registerRuleExecutionSet("test URI", null, null);
         fail("RuleExecutionSetRegisterException expected");
      } catch (RuleExecutionSetRegisterException ex) {
      }
   }

   /**
    * Test deregisterRuleExecutionSet.
    */
   public void testUnregisterRuleExecutionSet() throws Exception {
      try {
         // that it works is tested else where
         ruleAdministrator.deregisterRuleExecutionSet("test URI", null);
         fail("RuleExecutionSetUnregisterException expected");
      } catch (RuleExecutionSetDeregistrationException ex) {
      }
   }
}