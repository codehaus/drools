
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
    private Declaration targetDeclaration;
    private FactExtractor factExtractor;
    
    public AssignmentCondition(Declaration targetDeclaration,
                               FactExtractor factExtractor)
    {
        this.targetDeclaration = targetDeclaration;
        this.factExtractor     = factExtractor;
    }

    public Declaration getTargetDeclaration()
    {
        return this.targetDeclaration;
    }

    public FactExtractor getFactExtractor()
    {
        return this.factExtractor;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return getFactExtractor().getRequiredTupleMembers();
    }
}
