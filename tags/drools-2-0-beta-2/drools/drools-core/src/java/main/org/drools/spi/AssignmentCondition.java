
package org.drools.spi;

/** A {@link Condition} representing a <i>consistent assignment</i>
 *  as defined by the Rete-OO algorithm.
 *
 *  The assignment occurs through the process of extracting a
 *  new fact from existing facts.
 *
 *  @see FactExtractor
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class AssignmentCondition implements Condition
{
    /** The target of the assignment. */
    private Declaration targetDeclaration;

    /** FactExtractor to acquire value for assignment. */
    private FactExtractor factExtractor;
    
    /** Construct.
     *
     *  @param targetDeclaration The target of this assignment.
     *  @param factExtractor Value generator for the assignment.
     */
    public AssignmentCondition(Declaration targetDeclaration,
                               FactExtractor factExtractor)
    {
        this.targetDeclaration = targetDeclaration;
        this.factExtractor     = factExtractor;
    }

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

    public Declaration[] getRequiredTupleMembers()
    {
        return getFactExtractor().getRequiredTupleMembers();
    }
}
