package org.drools;

import org.drools.spi.ConflictResolutionStrategy;
import org.drools.conflict.SalienceConflictResolutionStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/** A knowledge session for a <code>RuleBase</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public interface WorkingMemory
{
    /** Retrieve the application data that is associated with
     *  this memory.
     *
     *  @return The application data or <code>null</code> if
     *  no data has been set for this memory.
     */
    Object getApplicationData();

    /** Set the application data associated with this memory.
     *
     *  @param appData The application data for this memory.
     */
    void setApplicationData(Object appData);

    /** Retrieve the <code>RuleBase</code>
     *  of this working memory.
     *
     *  @return The <code>RuleBase</code>.
     */
    RuleBase getRuleBase();

    /** Fire all items on the agenda until empty.
     *
     *  @throws FactException If an error occurs.
     */
    void fireAllRules() throws FactException;

    Object getObject(FactHandle handle)
        throws NoSuchFactObjectException;

    List getObjects();

    boolean containsObject(FactHandle handle);

    FactHandle assertObject(Object object)
        throws FactException;

    void retractObject(FactHandle handle)
        throws FactException;

    void modifyObject(FactHandle handle,
                      Object object)
        throws FactException;
}
