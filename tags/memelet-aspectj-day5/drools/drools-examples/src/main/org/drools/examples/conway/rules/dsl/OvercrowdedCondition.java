package org.drools.examples.conway.rules.dsl;

import org.drools.examples.conway.Cell;
import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.Tuple;

/**
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 * @version $Id: OvercrowdedCondition.java,v 1.2 2005-05-16 23:31:19 brownj Exp $
 */
public class OvercrowdedCondition implements Condition
{
    protected final Declaration cellDeclaration;

    public OvercrowdedCondition(Declaration cellDeclaration)
    {
        this.cellDeclaration = cellDeclaration;
    }

    /**
     * Determine if the supplied <code>Tuple</code> is allowed by this
     * condition.
     *
     * @param tuple
     *            The <code>Tuple</code> to test.
     *
     * @return <code>true</code> if the <code>Tuple</code> passes this
     *         condition, else <code>false</code>.
     *
     */
    public boolean isAllowed(Tuple tuple)
    {
        Cell cell = (Cell) tuple.get( cellDeclaration );
        int numberOfLiveNeighborsCellHas = cell.getNumberOfLiveNeighbors();
        boolean isAllowed = ( numberOfLiveNeighborsCellHas > 3 );
        return isAllowed;
    }

    /**
     * Retrieve the array of <code>Declaration</code> s required by this
     * condition to perform its duties.
     *
     * @return The array of <code>Declarations</code> expected on incoming
     *         <code>Tuples</code>.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return new Declaration[]{cellDeclaration};
    }
}

