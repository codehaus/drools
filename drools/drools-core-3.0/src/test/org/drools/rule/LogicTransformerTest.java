package org.drools.rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.drools.DroolsTestCase;
import org.drools.rule.ConstraintTest.Cheese;
import org.drools.rule.LogicTransformer.AndOrTransformation;
import org.drools.rule.LogicTransformer.OrTransformation;
import org.drools.spi.ConstraintComparator;
import org.drools.spi.LiteralExpressionConstraint;

public class LogicTransformerTest extends DroolsTestCase
{

    // /*
    // * construct
    // * <pre>
    // * or0
    // * / | \
    // * / c \
    // * or1 and0
    // * / | \ / \
    // * / a b d and1
    // * or3 / | \
    // * / \ e f and2
    // * j k / \
    // * g or2
    // * / \
    // * h i
    // * </pre>
    // *
    // * remove redundant
    // *
    // * </pre>
    // * or0
    // * //| |\\
    // * a b c j k\
    // * and0
    // * //| |\
    // * d e f g or2
    // * / \
    // * h i
    // * </pre>
    // */
    // public void testCollapseRedundant() {
    // String a = "a";
    // String b = "b";
    // String c = "c";
    // String d = "d";
    // String e = "e";
    // String f = "f";
    // String g = "g";
    // String h = "h";
    // String i = "i";
    // String j = "j";
    // String k = "k";
    //
    // Or or0 = new Or();
    // Or or1 = new Or();
    // Or or2 = new Or();
    // Or or3 = new Or();
    // And and0 = new And();
    // And and1 = new And();
    // And and2 = new And();
    //
    // or0.addChild(or1);
    // or0.addChild(and0);
    // or0.addChild(c);
    // or1.addChild(a);
    // or1.addChild(b);
    // or1.addChild(or3);
    // or3.addChild(j);
    // or3.addChild(k);
    // and0.addChild(d);
    // and0.addChild(and1);
    // and1.addChild(e);
    // and1.addChild(f);
    // and1.addChild(and2);
    // and2.addChild(or2);
    // and2.addChild(g);
    // or2.addChild(h);
    // or2.addChild(i);
    //
    // LogicTransformer helper = LogicTransformer.getInstance();
    // helper.collapseRedundant(or0);
    // assertLength(6, or0.getChildren());
    //
    // assertContains(a, or0.getChildren());
    // assertContains(b, or0.getChildren());
    // assertContains(c, or0.getChildren());
    // assertContains(j, or0.getChildren());
    // assertContains(k, or0.getChildren());
    // assertContains(and0, or0.getChildren());
    //
    // assertLength(5, and0.getChildren());
    // assertContains(d, and0.getChildren());
    // assertContains(e, and0.getChildren());
    // assertContains(f, and0.getChildren());
    // assertContains(g, and0.getChildren());
    // assertContains(or2, and0.getChildren());
    //
    // assertContains(h, or2.getChildren());
    // assertContains(i, or2.getChildren());
    // }
    //
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

    //
    // /**
    // * all nots when when use ORS shold be rewritten as ANDs
    // *
    // * Construct !(a||b)
    // *
    // * <pre>
    // * not
    // * |
    // * or
    // * / \
    // * a b
    // *
    // * </pre>
    // *
    // * Should become !a&&!b
    // *
    // * <pre>
    // *
    // * and
    // * / \
    // * not not
    // * | |
    // * a b
    // *
    // * </pre>
    // */
    // public void testRewriteNot() {
    // String a = "a";
    // String b = "b";
    //
    // Not not = new Not();
    // Or or = new Or();
    // not.addChild(or);
    // or.addChild(a);
    // or.addChild(b);
    //
    // LogicTransformer helper = LogicTransformer.getInstance();
    // And newAnd = helper.rewriteNot(not);
    //
    // assertLength(2, newAnd.getChildren());
    // assertTrue(true);
    //
    // assertTrue(newAnd.getChildren().get(0) instanceof Not);
    //
    // assertTrue(newAnd.getChildren().get(1) instanceof Not);
    //
    // Not not1 = (Not) newAnd.getChildren().get(0);
    // assertLength(1, not1.getChildren());
    // assertSame(a, not1.getChild());
    //
    // Not not2 = (Not) newAnd.getChildren().get(1);
    // assertLength(1, not2.getChildren());
    // assertSame(b, not2.getChild());
    // }

    // /**
    // * ((((a||b)&&c)||d)&&e)||f
    // *
    // * <pre>
    // *
    // * or
    // * / \
    // * and f
    // * / \
    // * or e
    // * / \
    // * and d
    // * / \
    // * or \
    // * / \ \
    // * a b c
    // *
    // * </pre>
    // *
    // * Should become (a&&b&&c)||(b&&c&&e)||(d&&e)||f
    // *
    // * <pre>
    // *
    // * a&amp;&amp;c||b&amp;&amp;c,
    // * or
    // * / | \ \
    // * / | \ \
    // * / | \ \
    // * / | \ \
    // * and and and \
    // * / | \ / | \ / \ \
    // * a b e b c e d e f
    // * </pre>
    // */
    // public void testProcessAnd() {
    // String a = "a";
    // String b = "b";
    // String c = "c";
    // String d = "d";
    // String e = "e";
    // String f = "f";
    //
    // Or or3 = new Or();
    // or3.addChild(a);
    // or3.addChild(b);
    //
    // And and2 = new And();
    // and2.addChild(c);
    // and2.addChild(or3);
    //
    // Or or2 = new Or();
    // or2.addChild(d);
    // or2.addChild(and2);
    //
    // And and1 = new And();
    // and1.addChild(e);
    // and1.addChild(or2);
    //
    // Or or1 = new Or();
    // or1.addChild(f);
    // or1.addChild(and1);
    //
    // LogicHelper helper = LogicHelper.getInstance();
    // helper.processAnd(or1);
    //
    // assertLength(4, or1.getChildren());
    //        
    // //We use this HashSet populated with all the strings so we can remove
    // them
    // //as we iterate trought the data structure, if all the letters are
    // removed
    // //then we know it was processed correctly
    //        
    // Set contents = new HashSet();
    // contents.add( "a" );
    // contents.add( "b" );
    // contents.add( "c" );
    // contents.add( "d" );
    // contents.add( "e" );
    // contents.add( "f" );
    //        
    // for (Iterator it = or1.getChildren().iterator(); it.hasNext(); )
    // {
    // Object object = it.next();
    // if ( object instanceof And )
    // {
    // for (Iterator it2 = ((ConditionalElement)
    // object).getChildren().iterator(); it2.hasNext(); )
    // {
    // contents.remove( it2.next() );
    // }
    // }
    // else if ( "f".equals( object ) )
    // {
    // contents.remove( object );
    // }
    // }
    // assertLength(0, contents);
    // }
    //    
    // /**
    // * ((((a||b)&&c)||d)&&e)||f
    // *
    // * <pre>
    //
    // * and
    // * / \
    // * or f
    // * / \
    // * and e
    // * / \
    // * or d
    // * / \
    // * and \
    // * / \ \
    // * a b c
    // *
    // * </pre>
    // *
    // * Should become (a&&b&&c)||(b&&c&&e)||(d&&e)||f
    // *
    // * <pre>
    // *
    // * a&amp;&amp;c||b&amp;&amp;c,
    // * or
    // * / | \ \
    // * / | \ \
    // * / | \ \
    // * / | \ \
    // * and and and \
    // * / | \ / | \ / \ \
    // * a b e b c e d e f
    // * </pre>
    // */
    // public void testProcessAnd2() {
    // String a = "a";
    // String b = "b";
    // String c = "c";
    // String d = "d";
    // String e = "e";
    // String f = "f";
    //
    // Or or3 = new Or();
    // or3.addChild(a);
    // or3.addChild(b);
    //
    // And and2 = new And();
    // and2.addChild(c);
    // and2.addChild(or3);
    //
    // Or or2 = new Or();
    // or2.addChild(d);
    // or2.addChild(and2);
    //
    // And and1 = new And();
    // and1.addChild(e);
    // and1.addChild(or2);
    //
    // Or or1 = new Or();
    // or1.addChild(f);
    // or1.addChild(and1);
    //
    // LogicHelper helper = LogicHelper.getInstance();
    // helper.processAnd(or1);
    //
    // assertLength(4, or1.getChildren());
    //        
    // //We use this HashSet populated with all the strings so we can remove
    // them
    // //as we iterate trought the data structure, if all the letters are
    // removed
    // //then we know it was processed correctly
    //        
    // Set contents = new HashSet();
    // contents.add( "a" );
    // contents.add( "b" );
    // contents.add( "c" );
    // contents.add( "d" );
    // contents.add( "e" );
    // contents.add( "f" );
    //        
    // for (Iterator it = or1.getChildren().iterator(); it.hasNext(); )
    // {
    // Object object = it.next();
    // if ( object instanceof And )
    // {
    // for (Iterator it2 = ((ConditionalElement)
    // object).getChildren().iterator(); it2.hasNext(); )
    // {
    // contents.remove( it2.next() );
    // }
    // }
    // else if ( "f".equals( object ) )
    // {
    // contents.remove( object );
    // }
    // }
    // assertLength(0, contents);
    // }
    //
    //    
    // public void processNotTest() {
    //
    // }
    //
    // public void processExistTest() {
    //
    // }

}
