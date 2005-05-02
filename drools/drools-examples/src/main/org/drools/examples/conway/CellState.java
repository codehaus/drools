package org.drools.examples.conway;

/**
 * <code>CellState</code> enumerates all of the valid states that a Cell may
 * be in.
 * 
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 * @version $Id: CellState.java,v 1.1.2.1 2005-05-02 01:51:50 mproctor Exp $
 * @see Cell
 * @see CellGrid
 */
public class CellState
{
    public static int NONE = -1;
    public static int LIVE = 0;
    public static int DEAD = 1;
}
