package org.drools.rule;

import java.util.List;

import org.drools.DroolsTestCase;

public class LogicTransformerTest extends DroolsTestCase
{
    /**
     * (a||b)&&c
     * 
     * <pre>
     *        and
     *        / \
     *       or  c 
     *      /  \
     *     a    b
     * </pre>
     * 
     * Should become (a&&c)||(b&&c)
     * 
     * <pre>
     *          
     *        or
     *       /  \  
     *      /    \ 
     *     /      \ 
     *      and      and     
     *   / \      / \
     *  a   c    b   c
     * </pre>
     */
    public void testAndOrTransformation()
    {
        String a = "a";
        String b = "b";
        String c = "c";

        And and = new And( );
        and.addChild( c );
        Or or = new Or( );
        or.addChild( a );
        or.addChild( b );
        and.addChild( or );

        Or newOr = (Or) LogicTransformer.getInstance( ).applyOrTransformation( and,
                                                                               or );

        assertLength( 2,
                      newOr.getChildren( ) );
        assertEquals( And.class,
                      newOr.getChildren( ).get( 0 ).getClass( ) );
        assertEquals( And.class,
                      newOr.getChildren( ).get( 1 ).getClass( ) );

        And and1 = (And) newOr.getChildren( ).get( 0 );
        assertContains( a,
                        and1.getChildren( ) );
        assertContains( c,
                        and1.getChildren( ) );

        And and2 = (And) newOr.getChildren( ).get( 1 );
        assertContains( b,
                        and2.getChildren( ) );
        assertContains( c,
                        and2.getChildren( ) );
    }

    /**
     * (Not (OR (A B)
     * 
     * <pre>
     *        Not
     *         | 
     *        or   
     *       /  \
     *      a    b
     * </pre>
     * 
     * (And ( Not (a) Exist (b)) )
     * 
     * <pre>
     *        And   
     *       /   \
     *      Not  Not
     *      |     |
     *      a     b
     * </pre>
     * </pre>
     */
    public void testNotOrTransformation()
    {
        String a = "a";
        String b = "b";

        Not not = new Not( );
        Or or = new Or( );
        not.addChild( or );

        or.addChild( a );
        or.addChild( b );

        And newAnd = (And) LogicTransformer.getInstance( ).applyOrTransformation( not,
                                                                                  or );

        assertLength( 2,
                      newAnd.getChildren( ) );
        assertEquals( Not.class,
                      newAnd.getChildren( ).get( 0 ).getClass( ) );
        assertEquals( Not.class,
                      newAnd.getChildren( ).get( 1 ).getClass( ) );

        Not not1 = (Not) newAnd.getChildren( ).get( 0 );
        assertContains( a,
                        not1.getChildren( ) );

        Not not2 = (Not) newAnd.getChildren( ).get( 1 );
        assertContains( b,
                        not2.getChildren( ) );
    }

    /**
     * (Exist (OR (A B)
     * 
     * <pre>
     *        Not
     *         | 
     *        or   
     *       /  \
     *      a    b
     * </pre>
     * 
     * (And ( Exist (a) Exist (b)) )
     * 
     * <pre>
     *        And   
     *       /   \
     *      Not  Not
     *      |     |
     *      a     b
     * </pre>
     * </pre>
     */
    public void testExistOrTransformation()
    {
        String a = "a";
        String b = "b";

        Exist exist = new Exist( );
        Or or = new Or( );
        exist.addChild( or );

        or.addChild( a );
        or.addChild( b );

        And newAnd = (And) LogicTransformer.getInstance( ).applyOrTransformation( exist,
                                                                                  or );

        assertLength( 2,
                      newAnd.getChildren( ) );
        assertEquals( Exist.class,
                      newAnd.getChildren( ).get( 0 ).getClass( ) );
        assertEquals( Exist.class,
                      newAnd.getChildren( ).get( 1 ).getClass( ) );

        Exist exist1 = (Exist) newAnd.getChildren( ).get( 0 );
        assertContains( a,
                        exist1.getChildren( ) );

        Exist exist2 = (Exist) newAnd.getChildren( ).get( 1 );
        assertContains( b,
                        exist2.getChildren( ) );
    }

    public void testDuplicatTransformation()
    {
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";

        And and1 = new And( );
        and1.addChild( a );
        and1.addChild( b );

        And and2 = new And( );
        and2.addChild( c );
        and2.addChild( d );

        and1.addChild( and2 );

        Or or = new Or( );
        and1.addChild( or );

        LogicTransformer.getInstance( ).checkForAndRemoveDuplicates( and1 );

        assertLength( 5,
                      and1.getChildren( ) );
        assertContains( a,
                        and1.getChildren( ) );
        assertContains( b,
                        and1.getChildren( ) );
        assertContains( c,
                        and1.getChildren( ) );
        assertContains( d,
                        and1.getChildren( ) );
        assertContains( or,
                        and1.getChildren( ) );

    }

    /**                 
     *                   _/|\_
     *                __/  |  \__
     *               /     |     \ 
     *            __/      |      \__
     *           /         |         \
     *          And       and         Not
     *         / | \      / \          |
     *       a  And d    e  Or        Or
     *          / \        /  \      / \
     *         b  Not     f  Exist  i  And  
     *             |           |       / \
     *            Not         Or      j   k 
     *             |         /  \
     *             c        g    h 
     *
     *                     _/|\__
     *                  __/  |   \___
     *                 /     |       \__
     *              __/      |          \__
     *             /         |             \__
     *            /          |                \__
     *           |           |                   \
     *          And          Or                  And
     *        / | | \       /  \                /  \  
     *      a   b d Not   And   And           Not  Not
     *               |    / \  / |  \          |    | 
     *              Not  e  f e Exist Exist    i   And  
     *               |           |      |          / \
     *               c           g      h         j   k 
     *                     
     *                                         
     *              
     */
    public void testTransform()
    {
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";
        String e = "e";
        String f = "f";
        String g = "g";
        String h = "h";
        String i = "i";
        String j = "j";
        String k = "k";

        And and1 = new And( );
        And and2 = new And( );
        and1.addChild( a );
        and1.addChild( and2 );
        and2.addChild( b );
        Not not1 = new Not( );
        Not not2 = new Not( );
        not1.addChild( not2 );
        not2.addChild( c );
        and2.addChild( not1 );
        and1.addChild( d );

        And and3 = new And( );
        and3.addChild( e );
        Or or1 = new Or( );
        and3.addChild( or1 );
        Exist exist1 = new Exist( );
        Or or2 = new Or( );
        or2.addChild( f );
        or2.addChild( g );
        exist1.addChild( or2 );
        or1.addChild( exist1 );
        or1.addChild( h );

        Not not3 = new Not( );
        Or or3 = new Or( );
        not3.addChild( or3 );
        or3.addChild( i );
        And and4 = new And( );
        and4.addChild( j );
        and4.addChild( k );
        or3.addChild( and4 );

        And root = new And( );
        root.addChild( and1 );
        root.addChild( and3 );
        root.addChild( not3 );

        LogicTransformer.getInstance( ).transform( root );

        // --------------------------------------
        // Test that the treesEqual method works
        // --------------------------------------

        // Check against itself
        assertTrue( treesEqual( root,
                                root ) );

        // Test against a known false tree
        And testAnd1 = new And( );
        testAnd1.addChild( a );
        testAnd1.addChild( b );
        Or testOr2 = new Or( );
        testOr2.addChild( c );
        testOr2.addChild( d );
        testAnd1.addChild( testOr2 );
        assertFalse( treesEqual( root,
                                 testAnd1 ) );

        // ----------------------------------------------------------------------------------
        // Now construct the result tree so we can test root against what it
        // should look like
        // ----------------------------------------------------------------------------------

        // Get known correct tree
        And correctResultRoot = getTestTree( );

        // Make sure they are equal
        assertTrue( treesEqual( correctResultRoot,
                                root ) );
    }

    /**
     * This returns the result tree for testTransform. This is a separate method
     * so that we can declare same name variables in a different scope so they
     * don't clash.
     * 
     * @return
     */
    private And getTestTree()
    {
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";
        String e = "e";
        String f = "f";
        String g = "g";
        String h = "h";
        String i = "i";
        String j = "j";
        String k = "k";
        And and1 = new And( );
        and1.addChild( a );
        and1.addChild( d );
        and1.addChild( b );
        Not not1 = new Not( );
        Not not2 = new Not( );
        not1.addChild( not2 );
        not2.addChild( c );
        and1.addChild( not1 );

        Or or1 = new Or( );
        And and2 = new And( );
        and2.addChild( e );
        and2.addChild( h );
        or1.addChild( and2 );
        And and3 = new And( );
        and3.addChild( e );
               
        Exist exist1 = new Exist( );
        exist1.addChild( f );
        Exist exist2 = new Exist( );
        exist2.addChild( g );
        and3.addChild( exist1 );
        and3.addChild( exist2 );
        or1.addChild( and3 );


        And and4 = new And( );
        Not not3 = new Not( );
        not3.addChild( i );
        Not not4 = new Not( );
        And and5 = new And( );
        and5.addChild( j );
        and5.addChild( k );
        not4.addChild( and5 );
        and4.addChild( not3 );
        and4.addChild( not4 );

        And root = new And( );
        root.addChild( and1 );
        root.addChild( or1 );
        root.addChild( and4 );
        return root;
    }

    /**
     * Traverses two trees and checks that they are structurally equal at all
     * levels
     * 
     * @param e1
     * @param e2
     * @return
     */
    boolean treesEqual(ConditionalElement e1,
                       ConditionalElement e2)
    {
        List e1Children = e1.getChildren( );
        List e2Children = e2.getChildren( );
        if ( e1Children.size( ) != e2Children.size( ) )
        {
            return false;
        }

        for ( int i = 0; i < e1Children.size( ); i++ )
        {
            Object e1Object1 = e1Children.get( i );
            Object e2Object1 = e2Children.get( i );
            if ( e1Object1 instanceof ConditionalElement )
            {
                if ( e1Object1.getClass( ).isInstance( e2Object1 ) )
                {
                    if ( !treesEqual( (ConditionalElement) e1Object1,
                                      (ConditionalElement) e2Object1 ) )
                    {
                        System.out.println( e1Object1.getClass( ).getName( ) + " did not have identical children" );
                        return false;
                    }
                }
                else
                {
                    System.out.println( "Should be the equal Conditionalelements but instead was '" + e1Object1.getClass( ).getName( ) + "', '" + e2Object1.getClass( ).getName( ) + "'" );
                    return false;
                }
            }
            else if ( e1Object1 instanceof String )
            {
                if ( !e1Object1.equals( e2Object1 ) )
                {
                    System.out.println( "Should be the equal Strings but instead was '" + e1Object1 + "', '" + e2Object1 + "'" );
                    return false;
                }
            }
            else
            {
                System.out.println( "Objects are neither instances of ConditionalElement or String" );
                return false;
            }
        }

        return true;
    }


}
