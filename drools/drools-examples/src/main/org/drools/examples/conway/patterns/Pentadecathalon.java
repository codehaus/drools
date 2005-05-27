package org.drools.examples.conway.patterns;

/**
 * The Pentadecathalon <p/>
 * 
 * @see ConwayPattern
 * @see org.drools.examples.conway.CellGrid
 * 
 * @version $Id: Pentadecathalon.java,v 1.1.2.1 2005-05-02 01:51:50 mproctor Exp $
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 */
public class Pentadecathalon
    implements
    ConwayPattern
{

    private boolean[][] grid = {{true, true, true, true, true, true, true, true, true}};

    /**
     * This method should return a 2 dimensional array of boolean that represent
     * a conway grid, with <code>true</code> values in the positions where
     * cells are alive
     * 
     * @return array representing a conway grid
     */
    public boolean[][] getPattern()
    {
        return grid;
    }

    /**
     * @return the name of this pattern
     */
    public String getPatternName()
    {
        return "Pentadecathalon";
    }

    public String toString()
    {
        return getPatternName( );
    }
}
