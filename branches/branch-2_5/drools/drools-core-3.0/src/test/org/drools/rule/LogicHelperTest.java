package org.drools.rule;

import java.util.LinkedList;
import java.util.Set;

import org.drools.DroolsTestCase;
import org.drools.rule.ConstraintTest.Cheese;
import org.drools.spi.ConstraintComparator;
import org.drools.spi.LiteralExpressionConstraint;

public class LogicHelperTest extends DroolsTestCase
{
        
    /*
     * construct
     *           or0
     *         /  |  \
     *        /   c   \
     *       or1      and0  
     *      / | \    /   \
     *     /  a  b  d    and1
     *    or3           / | \
     *   /  \          e  f and2
     *  j    k              /   \
     *                     g    or2
     *                          / \
     *                         h   i 
     *                         
     * remove redundant                        
     *          or0
     *        //| |\\
     *      a b c j k\
     *              and0  
     *             //| |\ 
     *           d e f g or2
     *                    / \
     *                   h   i                          
     */
    public void testCollapseRedundant()
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
        
        Or or0 = new Or();
        Or or1 = new Or();
        Or or2 = new Or();
        Or or3 = new Or();        
        And and0 = new And();
        And and1 = new And();
        And and2 = new And();
        
        or0.addChild( or1 );
        or0.addChild( and0 );
        or0.addChild( c );
        or1.addChild( a );
        or1.addChild( b );
        or1.addChild( or3 );
        or3.addChild( j );
        or3.addChild( k );        
        and0.addChild( d );
        and0.addChild( and1 );
        and1.addChild( e );
        and1.addChild( f );
        and1.addChild( and2 );
        and2.addChild( or2 );
        and2.addChild( g );
        or2.addChild( h );
        or2.addChild( i );
        
        LogicHelper helper = LogicHelper.getInstance();
        helper.collapseRedundant( or0 );
        assertLength( 6, or0.getChildren() );        
        
        assertContains( a, or0.getChildren() );
        assertContains( b, or0.getChildren() );
        assertContains( c, or0.getChildren() );
        assertContains( j, or0.getChildren() );
        assertContains( k, or0.getChildren() );
        assertContains( and0, or0.getChildren() );
                             
        assertLength( 5, and0.getChildren() );
        assertContains( d, and0.getChildren() );
        assertContains( e, and0.getChildren() );
        assertContains( f, and0.getChildren() );
        assertContains( g, and0.getChildren() );
        assertContains( or2, and0.getChildren() );      
        
        assertContains( h, or2.getChildren() );        
        assertContains( i, or2.getChildren() );
    }
    
    /**
     * Construct (a||b)&&c 
     * <pre>
             and
             / \
            or  c 
           /  \
          a    b
       </pre>
     * Should become (a&&c)||(b&&c)  
     * <pre>        
               or
              /  \  
             /    \ 
            /      \ 
           and    and     
           / \    / \
          a   b  b   c
    * </pre>      
    */ 
    public void testRewriteAnd()
    {
        String a = "a";
        String b = "b";
        String c = "c";
        
        And and = new And();
        and.addChild( c );
        Or or = new Or();
        or.addChild( a );
        or.addChild( b );
        and.addChild( or );
        
        LogicHelper helper = LogicHelper.getInstance();
        Or newOr = helper.rewriteAnd( and );
        
        String d = "d";
        String e = "e";
        String f = "f";
        String g = "g";
        /*
        Or or = new Or();
        or.addChild( d );
        or.addChild( e );
        */
        
    }
    
    
    /**
     * Construct !(a||b) 
     * <pre>
             not
             |
             or  
            /  \
           a    b
       </pre>
     * Should become !a&&!b 
     * <pre>        
               and                
               /  \  
             not  not 
              |    |
              a    b  

    * </pre>      
    */ 
    public void testRewriteNot()
    {
        String a = "a";
        String b = "b";
        
        Not not = new Not();
        Or or = new Or();
        not.addChild( or );
        or.addChild( a );
        or.addChild( b );
        
        LogicHelper helper = LogicHelper.getInstance();
        And newAnd = helper.rewriteNot( not );
        
        assertLength( 2,
                      newAnd.getChildren() );
        
        assertTrue( newAnd.getChildren().get( 0 ) instanceof Not );
        
        assertTrue( newAnd.getChildren().get( 1 ) instanceof Not );
        
        Not not1 = (Not) newAnd.getChildren().get( 0 );
        assertLength( 1,
                      not1.getChildren() );
        assertSame( a,
                    not1.getChild() );
        
        Not not2 = (Not) newAnd.getChildren().get( 1 );
        assertLength( 1,
                      not2.getChildren() );
        assertSame( b,
                    not2.getChild() );        
        
    }    
}
