package org.drools.examples.conway.rules.dsl;

import org.drools.spi.Consequence;
import org.drools.spi.Tuple;
import org.drools.rule.Declaration;
import org.drools.examples.conway.Cell;
import org.drools.examples.conway.CellState;

/**
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 * @version $Id: GiveBirthConsequence.java,v 1.2 2005-05-16 23:31:19 brownj Exp $
 */
public class GiveBirthConsequence implements Consequence
{
    private final Declaration cellDeclaration;

    public GiveBirthConsequence(Declaration cellDeclaration)
    {
        this.cellDeclaration = cellDeclaration;
    }

    /**
     * Execute the consequence for the supplied matching <code>Tuple</code>.
     *
     * @param tuple
     *            The matching tuple.
     */
    public void invoke(Tuple tuple)
    {
        Cell cell = (Cell) tuple.get( cellDeclaration );
        cell.queueNextCellState( CellState.LIVE );
    }
}
