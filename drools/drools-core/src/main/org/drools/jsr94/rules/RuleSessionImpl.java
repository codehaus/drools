package org.drools.jsr94.rules;

/*
 $Id: RuleSessionImpl.java,v 1.1 2003-03-15 01:24:20 bob Exp $

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

import javax.rules.*;

/**
 * This interface is a representation of a client session with a rules engine.
 * A rules engine session serves as an entry point into an underlying rules engine.
 * The <code>RuleSession</code> is bound to a rules engine instance and exposes a vendor-neutral
 * rule processing API for executing Rule(s) within a bound <code>RuleExecutionSet</code>.
 *
 * @see RuleSession
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
abstract class RuleSessionImpl implements RuleSession {

   /**
    * Returns the meta data for the rule execution set bound to this rule session.
    */
   public RuleExecutionSetMetadata getRuleExecutionSetMetadata() {
      // [TODO]
      throw new NotImplementedException();
   }

   /**
    * Releases all resources used by this rule session.
    * This method renders this rule session unusable until it is reacquired through the RuleRuntime.
    */
   abstract public void release();

   /**
    * Returns the type identifier for this RuleSession.
    * The type identifiers are defined in the RuleRuntime interface.
    */
   public int getType() throws InvalidRuleSessionException {

      if (this instanceof StatelessRuleSession)
         return RuleRuntime.STATELESS_SESSION_TYPE;

      if (this instanceof StatefulRuleSession)
         return RuleRuntime.STATEFUL_SESSION_TYPE;

      throw new InvalidRuleSessionException("unknown type");
   }

}
