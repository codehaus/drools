/*
 * Created on Nov 17, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.drools.util;

import java.util.Collection;

import org.drools.util.PrimitiveLongMap;

import junit.framework.TestCase;

/**
 * @author mproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PrimitiveLongMapTest extends TestCase
{

    public void testValues()
    {
        PrimitiveLongMap map = new PrimitiveLongMap();
        
        //test doesn't throw a null pointer
        Collection values = map.values();        
    }
}
