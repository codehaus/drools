package org.drools.rule;

/*
 $Id: FactExtraction.java,v 1.3 2002-08-01 21:00:21 bob Exp $

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

import org.drools.spi.FactExtractor;

/** A <code>Condition</code> representing a <i>consistent assignment</i>
 *  as defined by the Rete-OO algorithm.
 *
 *  The assignment occurs through the process of extracting a
 *  new fact from existing facts.
 *
 *  @see FactExtractor
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class FactExtraction 
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The target of the assignment. */
    private Declaration targetDeclaration;

    /** FactExtractor to acquire value for assignment. */
    private FactExtractor factExtractor;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------
    
    /** Construct.
     *
     *  @param targetDeclaration The target of this assignment.
     *  @param factExtractor Value generator for the assignment.
     */
    public FactExtraction(Declaration targetDeclaration,
                          FactExtractor factExtractor)
    {
        this.targetDeclaration = targetDeclaration;
        this.factExtractor     = factExtractor;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the <code>Declaration</code> for the target
     *  of the assignment.
     *
     *  @return The target's <code>Declaration</code>
     */
    public Declaration getTargetDeclaration()
    {
        return this.targetDeclaration;
    }

    /** Retrieve the <code>FactExtractor</code> responsible
     *  for generating the assignment value.
     *
     *  @return The <code>FactExtractor</code>.
     */
    public FactExtractor getFactExtractor()
    {
        return this.factExtractor;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.spi.Condition
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the array of <code>Declaration</code>s required
     *  by this condition to perform its duties.
     *
     *  @return The array of <code>Declarations</code> expected
     *          on incoming <code>Tuple</code>s.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return getFactExtractor().getRequiredTupleMembers();
    }
}
