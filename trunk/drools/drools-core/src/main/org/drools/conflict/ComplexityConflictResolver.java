package org.drools.conflict;

import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.ConflictResolver;

import java.util.List;
import java.util.ListIterator;

public class ComplexityConflictResolver
    extends SalienceConflictResolver
{
    private static final ConflictResolver INSTANCE = new ComplexityConflictResolver();

    public static ConflictResolver getInstance()
    {
        return INSTANCE;
    }

    public ComplexityConflictResolver()
    {

    }

    public void insert(Activation activation,
                       List list)
    {
        int numConditions = activation.getRule().getConditions().length;
        
        for ( ListIterator listIter = list.listIterator();
              listIter.hasNext(); )
        {
            Activation eachActivation = (Activation) listIter.next();

            int eachNumConditions = eachActivation.getRule().getConditions().length;

            if ( numConditions > eachNumConditions )
            {
                listIter.previous();
                listIter.add( activation );
            }
            else if ( numConditions == eachNumConditions )
            {
                int startIndex = listIter.previousIndex();

              FIND_SUB_LIST:
                while ( listIter.hasNext() )
                {
                    eachActivation = (Activation) listIter.next();

                    if ( eachActivation.getRule().getConditions().length != numConditions )
                    {
                        break FIND_SUB_LIST;
                    }
                }

                int endIndex = listIter.previousIndex();

                super.insert( activation,
                              list.subList( startIndex,
                                            endIndex ) );
                return;
            }
        }

        list.add( activation );
    }
}
