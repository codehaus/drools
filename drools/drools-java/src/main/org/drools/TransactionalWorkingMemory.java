
package org.drools;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** A {@link WorkingMemory} which caches all assertion, retractions
 *  and modifications, and only performs the fact propagation and
 *  rule action invokation upon {@link #commit}.  
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class TransactionalWorkingMemory extends WorkingMemory
{
    /** Object to assert upon commit. */
    private Set     assertedObjects;

    /** Objects retracted during commit. */
    private Set     retractedObjects;

    /** Construct.
     *
     *  @param ruleBase The <code>RuleBase</code> for this <code>WorkingMemory</code>.
     */
    public TransactionalWorkingMemory(RuleBase ruleBase)
    {
        super( ruleBase );

        this.assertedObjects = new HashSet();
    }

    public void assertObject(Object object) throws AssertionException
    {
        if ( this.retractedObjects != null )
        {
            super.assertObject( object );
        }
        else
        {
            this.assertedObjects.add( object );
        }
    }

    public void modifyObject(Object object) throws ModificationException
    {
        if ( this.retractedObjects != null )
        {
            super.modifyObject( object );
        }
        else
        {
            this.assertedObjects.add( object );
        }
    }

    public void retractObject(Object object) throws RetractionException
    {
        if ( this.retractedObjects != null )
        {
            super.retractObject( object );
            this.retractedObjects.add( object );
        }
        else
        {
            this.assertedObjects.remove( object );
        }
    }

    /** Abort this <code>TransactionalWorkingMemory</code>,
     *  by tossing out all asserted objects, and reseting
     *  this <code>TransactionalWorkingMemory</code> to a
     *  clean state.
     */
    public void abort()
    {
        this.assertedObjects.clear();
    }

    /** Commit all asserted objects into the logic engine,
     *  and reset this <code>TransactionalWorkingMemory</code>
     *  to a clean state.
     */
    public void commit() throws DroolsException
    {
        try
        {
            this.retractedObjects = new HashSet();

            Iterator objIter = this.assertedObjects.iterator();
            Object   eachObj = null;
            
            while ( objIter.hasNext() )
            {
                eachObj = objIter.next();

                if ( this.retractedObjects.contains( eachObj ) )
                {
                    continue;
                }
                
                super.assertObject( eachObj );

                objIter.remove();
            }
        }
        finally
        {
            this.assertedObjects.clear();
            this.retractedObjects = null;
        }
    }
}
