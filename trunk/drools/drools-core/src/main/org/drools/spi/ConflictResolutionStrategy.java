package org.drools.spi;

/*
 $Id: ConflictResolutionStrategy.java,v 1.3 2003-11-19 21:31:12 bob Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
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

/** Strategy for resolving conflicts amongst multiple rules.
 *
 *  <p>
 *  Since a fact or set of facts may activate multiple rules,
 *  a <code>ConflictResolutionStrategy</code> is used to provide
 *  priority ordering of conflicting rules.
 *  </p>
 *
 *  @see Activation.
 *  @see Tuple.
 *  @see org.drools.rule.Rule.
 *
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *
 *  @version $Id: ConflictResolutionStrategy.java,v 1.3 2003-11-19 21:31:12 bob Exp $
 */
public interface ConflictResolutionStrategy
{
    /** Compare two <code>Activation</code>s.
     *
     *  <p>
     *  The implementation must ensure that if the first activation
     *  has higher priority than the second that a positive integer
     *  is returned; if the second has higher priority than the first,
     *  then a negative integer; if no difference, then zero.
     *  <p>
     *
     *  @param activationOne The first activation.
     *  @param activationTwo The second activation.
     *
     *  @return A negative integer, positive integer or zero in
     *          accordance with the relative priority of the activations.
     */
    int compare(Activation activationOne,
                Activation activationTwo);
}
