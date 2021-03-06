package org.drools.spi;

/*
 * $Id: MockAgendaItem.java,v 1.2 2005-02-04 02:13:37 mproctor Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import org.drools.WorkingMemory;
import org.drools.rule.Rule;

/**
 * Item entry in the <code>Agenda</code>.
 * 
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
public class MockAgendaItem
    implements
    Activation
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The tuple. */
    private MockTuple   tuple;

    /** The rule. */
    private Rule        rule;

    // ** The Counter */
    private static long counter;

    private long        activationNumber;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param tuple
     *            The tuple.
     * @param rule
     *            The rule.
     */
    public MockAgendaItem(MockTuple tuple,
                          Rule rule)
    {
        this.tuple = tuple;
        this.rule = rule;
        this.activationNumber = counter++;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the rule.
     * 
     * @return The rule.
     */
    public Rule getRule()
    {
        return this.rule;
    }

    /**
     * Set the tuple.
     * 
     * @param tuple
     *            The tuple.
     */
    void setTuple(MockTuple tuple)
    {
        this.tuple = tuple;
    }

    /**
     * Retrieve the tuple.
     * 
     * @return The tuple.
     */
    public Tuple getTuple()
    {
        return this.tuple;
    }

    /**
     * Fire this item.
     * 
     * @param workingMemory
     *            The working memory context.
     * 
     * @throws ConsequenceException
     *             If an error occurs while attempting to fire the consequence.
     */
    void fire(WorkingMemory workingMemory) throws ConsequenceException
    {
        getRule( ).getConsequence( ).invoke( getTuple( ) );
    }

    public long getActivationNumber()
    {
        return this.activationNumber;
    }

    public String toString()
    {
        return "[" + this.rule.getName( ) + " " + this.tuple + "]";
    }
}