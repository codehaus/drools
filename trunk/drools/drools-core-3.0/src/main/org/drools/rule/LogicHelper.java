package org.drools.rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LogicHelper
{
    private static LogicHelper INSTANCE = null;
    
    public static LogicHelper getInstance()
    {
        if ( INSTANCE == null )
        {
            INSTANCE = new LogicHelper();
        }
        
        return INSTANCE;
    }

    public void rewriteLogic(ConditionalElement ce)
    {
        collapseRedundant( ce );
        processNot( ce );
        processAnd( ce );
    }

    /**
     * if the child is of the same type as the parent then collapse it.
     *
     */
    public void collapseRedundant(ConditionalElement ce)
    {
        ConditionalElement child = null;
        Object object = null;

        List collapseable = new ArrayList();
        Iterator it = ce.getChildren().iterator();
        while ( it.hasNext() )
        {
            object = it.next();
            if ( object instanceof ConditionalElement )
            {
                child = (ConditionalElement) object;
                collapseRedundant( child );
                if ( ce.getClass().isInstance( child ) )
                {
                    collapseable.add( child );
                    it.remove();
                }
            }
        }

        it = collapseable.iterator();
        while ( it.hasNext() )
        {
            child = (ConditionalElement) it.next();
            ce.getChildren().addAll( child.getChildren() );
        }
    }            
    
    public void processExist(ConditionalElement ce)
    {
        Object object =  null;
        And and = null;
        List newChildren = null;
        
        Iterator it = ce.getChildren().iterator();
        while ( it.hasNext() )
        {
            object = it.next();
            if ( object instanceof ConditionalElement )
            {
                processExist( ( ConditionalElement ) object );
                if ( object instanceof Exist )
                {
                    and = (And) object;
                    Iterator it2 = and.getChildren().iterator();
                    while ( it2.hasNext() )
                    {
                        if ( it2.next() instanceof Or)
                        {
                            newChildren.add( rewriteAnd( and ) );
                            it.remove();
                            break;
                        }
                    }
                }
            }
        }
        
        ce.getChildren().addAll( newChildren );
    }

    /**
     * Where ever we have
     *    AND
     *    /
     *   OR 
     *  /  \
     * a    b
     * @param ce
     */
    public void  processAnd(ConditionalElement ce)
    {
        Object object =  null;
        And and = null;
        List newChildren = null;
        
        Iterator it = ce.getChildren().iterator();
        while ( it.hasNext() )
        {
            object = it.next();
            if ( object instanceof ConditionalElement )
            {
                processAnd( ( ConditionalElement ) object );
                if ( object instanceof And )
                {
                    and = (And) object;
                    Iterator it2 = and.getChildren().iterator();
                    while ( it2.hasNext() )
                    {
                        if ( it2.next() instanceof Or)
                        {
                            newChildren.add( rewriteAnd( and ) );
                            it.remove();
                            break;
                        }
                    }
                }
            }
        }
        
        ce.getChildren().addAll( newChildren );
        
        /* we know this an OR node and any newChildren will be AND nodes
         * rewritten to be ORs, so now we need to collapse the redundancy
         */
        if ( ce instanceof Or)
        {
            collapseRedundant( ce );
        }

    }        
    
    public void  processNot(ConditionalElement ce)
    {
        Object object =  null;
        Not not = null;
        List newChildren = null;
        
        Iterator it = ce.getChildren().iterator();
        while ( it.hasNext() )
        {
            object = it.next();
            if ( object instanceof ConditionalElement )
            {
                processNot( ( ConditionalElement ) object );
                if ( object instanceof Not )
                {
                    not = (Not) object;
                    if ( not.getChild() instanceof Or )
                    {
                        newChildren.add( rewriteNot( not ) );
                        it.remove();
                    }
                }
            }
        }
        
        ce.getChildren().addAll( newChildren );
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
    public Or rewriteAnd(And and)
    {
        ConditionalElement child = null;
        Object object = null;
        List combinations = new ArrayList();
        List combination = new ArrayList();

        /* first gather up all direct none conditional element children */
        Iterator it = and.getChildren().iterator();
        while ( it.hasNext() )
        {
            object = it.next();
            if ( !(object instanceof ConditionalElement) )
            {
                combination.add( object );
            }
        }
        combinations.add( combination );

        Iterator childIter = and.getChildren().iterator();
        while ( childIter.hasNext() )
        {
            object = childIter.next();
            if ( object instanceof ConditionalElement )
            {
                child = (ConditionalElement) object;
                List newCombinations = new ArrayList();
                Iterator combiIter = combinations.iterator();
                while ( combiIter.hasNext() )
                {
                    combination = (List) combiIter.next();

                    it = child.getChildren().iterator();
                    while ( it.hasNext() )
                    {
                        List newCombination = new ArrayList( combination );
                        newCombination.add( it.next() );
                        newCombinations.add( newCombination );
                    }
                    combiIter.remove();
                }
                combinations = newCombinations;
            }
        }

        //each combination is an AND so now construct the logic
        Or or = new Or();
        it = combinations.iterator();
        while ( it.hasNext() )
        {
            combination = (List) it.next();
            and = new And();
            Iterator it2 = combination.iterator();
            while ( it2.hasNext() )
            {
                and.addChild( it2.next() );
            }
            or.addChild( and );
        }

        return or;
    }

    public And rewriteNot(Not not)
    {
        Not newNot = null;
        And and = new And();
        
        /* we know a Not only ever has one child, and the previous algorithm
         * has confirmed the child is an OR
         */
        Or or  = (Or) not.getChild();
        Iterator it = or.getChildren().iterator();
        while ( it.hasNext() )
        {
            newNot = new Not();
            newNot.addChild( it.next() );
            and.addChild( newNot );
        }
        return and;
    }

    /**
     * This class allows us to build a sorted collection of And elements in depth order
     * @author mproctor
     *
     */
    static class AndIndex
        implements
        Comparable
    {
        private final int level;

        private final And and;

        public AndIndex(And and,
                        int level)
        {
            this.and = and;
            this.level = level;
        }

        public And getAnd()
        {
            return this.and;
        }

        public int getLevel()
        {
            return this.level;
        }

        public int compareTo(Object otherObject)
        {
            int otherLevel = ((AndIndex) otherObject).getLevel();
            return otherLevel - this.level;
        }

    }
}
