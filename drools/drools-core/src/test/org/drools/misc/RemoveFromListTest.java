package org.drools.misc;

import junit.framework.TestCase;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleSetLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileReader;

/**
 * A consequence removes an object from a list
 *
 * Autor: thomas.diesler@iotronics.com
 *
 * $Id: RemoveFromListTest.java,v 1.4 2003-10-14 22:57:59 bob Exp $
 */
public class RemoveFromListTest extends TestCase
{

    private WorkingMemory workingMemory;

    public RemoveFromListTest( String name )
    {
        super( name );
    }

    public void setUp() throws Exception
    {

        RuleBase ruleBase = new RuleBase();

        RuleSetLoader loader = new RuleSetLoader();
        URL url = getClass().getResource( "RemoveFromListTest.drl" );
        assertNotNull( "cannot find drl file", url );
        loader.load(url, ruleBase);

        workingMemory = ruleBase.newWorkingMemory();
    }

    public void testRemoveFromList() throws Exception
    {

        Participant pt1 = new Participant( false );
        Participant pt2 = new Participant( true );

        ArrayList list = new ArrayList();
        list.add( pt1 );
        list.add( pt2 );

        workingMemory.assertObject( list );
        workingMemory.assertObject( pt1 );
        workingMemory.assertObject( pt2 );

        workingMemory.fireAllRules();

        assertEquals( "invalid number of participants", 1, list.size() );
        assertEquals( "particpant should be inactive", false, ( (Participant) list.get( 0 ) ).isActive() );
    }
}
