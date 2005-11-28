package org.drools.reteoo;

import junit.framework.TestCase;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.ObjectType;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 */
public class AgendaItemMapTest extends TestCase
{

    /**
     * The simples test that should work.
     */
    public void testPutGetMatch()
    {
        AgendaItemMap map = new AgendaItemMap( );
        ObjectType objType = getMockObjectType( );

        Declaration dec = new Declaration( "somthing",
                                           objType,
                                           1 );
        TupleKey key = new TupleKey( dec,
                                     new FactHandleImpl( 1 ) );
        Rule rule = new Rule( "name1" );
        map.put( rule,
                 key,
                 "item1" );

        Object result = map.remove( rule,
                                    key );

        assertEquals( "item1",
                      result );

        result = map.remove( rule,
                             key );
        assertNull( result );

    }

    public void testMoreComplex()
    {
        AgendaItemMap map = new AgendaItemMap( );
        ObjectType objType = getMockObjectType( );

        Declaration dec = new Declaration( "somthing",
                                           objType,
                                           1 );
        TupleKey key = new TupleKey( dec,
                                     new FactHandleImpl( 1 ) );

        map.put( new Rule( "name1" ),
                 key,
                 "item1" );
        map.put( new Rule( "name2" ),
                 key,
                 "item2" );

        TupleKey key2 = new TupleKey( dec,
                                      new FactHandleImpl( 2 ) );
        map.put( new Rule( "name2" ),
                 key2,
                 "item3" );

        Object result = map.remove( new Rule( "name2" ),
                                    key );

        assertNotSame( "item1",
                       result );

        result = map.remove( new Rule( "name2" ),
                             key2 );

        assertEquals( "item3",
                      result );

        // now check null returns
        result = map.remove( new Rule( "missing rule" ),
                             key2 );
        assertNull( result );

        TupleKey missingKey = new TupleKey( dec,
                                            new FactHandleImpl( 42 ) );
        result = map.remove( new Rule( "name2" ),
                             missingKey );
        assertNull( result );
    }

    public void testRemoveAll()
    {
        AgendaItemMap map = new AgendaItemMap( );
        ObjectType objType = getMockObjectType( );

        Declaration dec = new Declaration( "somthing",
                                           objType,
                                           1 );
        TupleKey key = new TupleKey( dec,
                                     new FactHandleImpl( 1 ) );
        map.put( new Rule( "name1" ),
                 key,
                 "item1" );
        map.put( new Rule( "name2" ),
                 key,
                 "item1" );
        map.put( new Rule( "name3" ),
                 key,
                 "item1" );
        map.removeAll( getRemoveDelegate( ) );

        assertNull( map.remove( new Rule( "name1" ),
                                key ) );
        assertNull( map.remove( new Rule( "name2" ),
                                key ) );
        assertNull( map.remove( new Rule( "name3" ),
                                key ) );
    }

    public void testIsEmpty()
    {
        AgendaItemMap map = new AgendaItemMap( );
        assertTrue( map.isEmpty( ) );
        ObjectType objType = getMockObjectType( );

        Declaration dec = new Declaration( "somthing",
                                           objType,
                                           1 );
        TupleKey key = new TupleKey( dec,
                                     new FactHandleImpl( 1 ) );
        map.put( new Rule( "name1" ),
                 key,
                 "item1" );
        assertFalse( map.isEmpty( ) );
        AgendaItemMap.RemoveDelegate del = getRemoveDelegate( );
        map.removeAll( del );
        assertTrue( map.isEmpty( ) );

        map.put( new Rule( "name1" ),
                 key,
                 "item1" );
        map.remove( new Rule( "name1" ),
                    key );
        assertTrue( map.isEmpty( ) );

    }

    private AgendaItemMap.RemoveDelegate getRemoveDelegate()
    {
        AgendaItemMap.RemoveDelegate del = new AgendaItemMap.RemoveDelegate( ) {
            public void processRemove(Object obj)
            {
            }
        };
        return del;
    }

    private ObjectType getMockObjectType()
    {
        return new ObjectType( ) {

            public boolean matches(Object object)
            {
                return false;
            }

        };
    }

}
