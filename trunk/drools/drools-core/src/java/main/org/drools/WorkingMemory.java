package org.drools;

/*
 $Id: WorkingMemory.java,v 1.14 2003-08-21 01:08:39 tdiesler Exp $

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.reteoo.Agenda;
import org.drools.reteoo.JoinMemory;
import org.drools.reteoo.JoinNode;
import org.drools.reteoo.impl.AgendaImpl;
import org.drools.reteoo.impl.JoinMemoryImpl;
import org.drools.reteoo.impl.JoinNodeImpl;

import java.util.HashMap;
import java.util.Map;

/** A knowledge session for a <code>RuleBase</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class WorkingMemory
{
    private static Log log = LogFactory.getLog( WorkingMemory.class );

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The <code>RuleBase</code> with which this memory is associated. */
    private RuleBase ruleBase;

    /** The actual memory for the <code>JoinNode</code>s. */
    private Map joinMemories;

    /** Rule-firing agenda. */
    private AgendaImpl agenda;

    /** Flag to determine if a rule is currently being fired. */
    private boolean firing;

    /** Application data which is associated with this memory. */
    private Object applicationData;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct a new working memory for a ruleBase.
     *
     *  @param ruleBase The rule base with which this memory is associated.
     */
    protected WorkingMemory( RuleBase ruleBase )
    {
        this.ruleBase = ruleBase;
        this.joinMemories = new HashMap();

        this.agenda = new AgendaImpl( this );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the application data that is associated with
     *  this memory.
     *
     *  @return The application data or <code>null</code> if
     *  no data has been set for this memory.
     */
    public Object getApplicationData()
    {
        return this.applicationData;
    }

    /** Set the application data associated with this memory.
     *
     *  @param appData The application data for this memory.
     */
    public void setApplicationData( Object appData )
    {
        this.applicationData = appData;
    }

    /** Retrieve the rule-firing <code>Agenda</code> for
     *  this <code>WorkingMemory</code>.
     *
     *  @return The <code>Agenda</code>.
     */
    public Agenda getAgenda()
    {
        return this.agenda;
    }

    /** Retrieve the <code>RuleBase</code>
     *  of this working memory.
     *
     *  @return The <code>RuleBase</code>.
     */
    public RuleBase getRuleBase()
    {
        return this.ruleBase;
    }

    /** Fire all items on the agenda until empty.
     *
     *  @throws AssertionException If an assertion error occurs.
     */
    private void fireAgenda() throws AssertionException
    {
        Agenda agenda = getAgenda();

        // If we're already firing a rule, then it'll pick up
        // the firing for any other assertObject(..) that get
        // nested inside, avoiding concurrent-modification
        // exceptions, depending on code paths of the actions.

        if ( !this.firing )
        {
            try
            {
                this.firing = true;

                while ( !agenda.isEmpty() )
                {
                    getAgenda().fireNextItem();
                }
            }
            finally
            {
                this.firing = false;
            }
        }
    }

    /** Assert a new fact object into this working memory.
     *
     *  @param object The object to assert.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    public synchronized void assertObject( Object object ) throws AssertionException
    {
        log.debug( "assertObject: " + object );

        getRuleBase().assertObject( object,
                this );

        fireAgenda();
    }

    /** Retract a fact object from this working memory.
     *
     *  @param object The object to retract.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    public synchronized void retractObject( Object object ) throws RetractionException
    {
        log.debug( "retractObject: " + object );

        getRuleBase().retractObject( object,
                this );
    }

    /** Modify a fact object in this working memory.
     *
     *  With the exception of time-based nodes, modification of
     *  a fact object is semantically equivelent to retracting and
     *  re-asserting it.
     *
     *  @param object The object to modify.
     *
     *  @throws FactException if an error occurs during modification.
     */
    public synchronized void modifyObject( Object object ) throws FactException
    {
        log.debug( "modifyObject: " + object );

        getRuleBase().modifyObject( object,
                this );

        fireAgenda();
    }

    /** Retrieve the <code>JoinMemory</code> for a particular <code>JoinNode</code>.
     *
     *  @param node The <code>JoinNode</code> key.
     *
     *  @return The node's memory.
     */
    public JoinMemory getJoinMemory( JoinNode node )
    {
        JoinMemory memory = (JoinMemory) this.joinMemories.get( node );

        if ( memory == null )
        {
            memory = new JoinMemoryImpl( (JoinNodeImpl) node );

            this.joinMemories.put( node,
                    memory );
        }

        return memory;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** Produce a debug string.
     *
     *  @return A debug string.
     */
    public String toString()
    {
        return "[WorkingMemory: " + joinMemories + ",applicationData" + applicationData + "]";
    }
}
