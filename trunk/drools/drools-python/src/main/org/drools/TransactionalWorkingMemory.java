package org.drools;

/*
  $Id: TransactionalWorkingMemory.java,v 1.7 2003-01-14 03:09:25 bob Exp $

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

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** A transactional knowledge session for a <code>RuleBase</code>.
 *
 *  <p>
 *  A <code>WorkingMemory</code> which caches all assertion, retractions
 *  and modifications, and only performs the fact propagation and
 *  rule action invokation upon {@link #commit}.  
 *  </p>
 *
 *  @see WorkingMemory
 *  @see RuleBase
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *  @author <a href="mailto:tomv@aic.net.au">Tom Vasak</a>
 */
public class TransactionalWorkingMemory extends WorkingMemory
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Object to assert upon commit. */
    private Set     assertedObjects;

    /** Objects retracted during commit. */
    private Set     retractedObjects;

    /** Performing commit. */
    private boolean committing;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param ruleBase The <code>RuleBase</code> for this <code>WorkingMemory</code>.
     */
    public TransactionalWorkingMemory(RuleBase ruleBase)
    {
        super( ruleBase );

        this.assertedObjects = new HashSet();
        this.retractedObjects = new HashSet();
        this.committing = false;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Abort this <code>TransactionalWorkingMemory</code>,
     *  by tossing out all asserted objects, and reseting
     *  this <code>TransactionalWorkingMemory</code> to a
     *  clean state.
     */
    public synchronized void abort()
    {
        this.assertedObjects.clear();
        this.retractedObjects.clear();
    }

    /** Commit all asserted objects into the logic engine,
     *  and reset this <code>TransactionalWorkingMemory</code>
     *  to a clean state.
     *
     *  @throws DroolsException If an 
     */
    public synchronized void commit() throws DroolsException
    {
        try
        {
            this.committing = true;

            Iterator objIter = this.assertedObjects.iterator();
            Object   eachObj = null;
            
            while ( objIter.hasNext() )
            {
                eachObj = objIter.next();

                super.assertObject( eachObj );

                objIter.remove();
            }

            objIter = this.retractedObjects.iterator();
            eachObj = null;
            
            while ( objIter.hasNext() )
            {
                eachObj = objIter.next();

                super.retractObject( eachObj );

                objIter.remove();
            }
        }
        finally
        {
            this.assertedObjects.clear();
            this.retractedObjects.clear();
            this.committing = false;
        }
    }

    /** Assert a new fact object into this working memory.
     *
     *  @param object The object to assert.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    public synchronized void assertObject(Object object) throws AssertionException
    {
        if ( this.committing )
        {
            super.assertObject( object );
        }
        else
        {
            if ( this.retractedObjects.contains( object ) )
            {
                this.retractedObjects.remove( object );
            }
            this.assertedObjects.add( object );
        }
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
    public synchronized void modifyObject(Object object) throws FactException
    {
        if ( this.committing )
        {
            super.modifyObject( object );
        }
        else
        {
            if ( this.retractedObjects.contains( object ) )
            {
                // Should throw a FactException since you are modifying
                // after retracting. Let it go for now.
                this.retractedObjects.remove( object );
            }
            this.assertedObjects.add( object );
        }
    }

    /** Retract a fact object from this working memory.
     *
     *  @param object The object to retract.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    public synchronized void retractObject(Object object) throws RetractionException
    {
        if ( this.committing )
        {
            super.retractObject( object );
            this.retractedObjects.add( object );
        }
        else
        {
            if ( this.retractedObjects.contains( object ) )
            {
                // Maybe should throw a RetractionException since you are
                // retracting again after a previous retraction.
            }
            else if ( this.assertedObjects.contains( object ) )
            {
                this.assertedObjects.remove( object );

                // Object may still be currently asserted as a fact so
                // still need to retract.
                this.retractedObjects.add( object );
            }
            else
            {
                this.retractedObjects.add( object );
            }
        }
    }
}
