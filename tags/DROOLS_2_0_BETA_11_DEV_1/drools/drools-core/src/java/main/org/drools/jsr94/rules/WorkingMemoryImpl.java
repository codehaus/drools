package org.drools.jsr94.rules;

import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide access to the list of objects currently asserted to the working memory.
 *
 *  @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class WorkingMemoryImpl extends WorkingMemory
{

    /** contains the objects currently in the working memory. */
    private List objectList = new ArrayList();

    /**
     * Construct a new working memory for a ruleBase.
     *
     *  @param ruleBase The rule base with which this memory is associated.
     */
    WorkingMemoryImpl(RuleBase ruleBase)
    {
        super(ruleBase);
    }

    /**
     * Get a list of objects currently asserted to the working memory.
     */
    public List getObjectList()
    {
        return objectList;
    }

    /** Assert a new fact object into this working memory.
     *
     *  @param object The object to assert.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    public synchronized void assertObject(Object object) throws AssertionException
    {
        super.assertObject(object);
        objectList.add(object);
    }

    /** Retract a fact object from this working memory.
     *
     *  @param object The object to retract.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    public synchronized void retractObject(Object object) throws RetractionException
    {
        super.retractObject(object);
        objectList.remove(object);
    }
}
