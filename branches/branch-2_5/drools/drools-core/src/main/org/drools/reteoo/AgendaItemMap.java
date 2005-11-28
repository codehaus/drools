package org.drools.reteoo;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.drools.rule.Rule;

/**
 * Stores an Agenda item which may be reused in the case of a object modify. (so
 * that it is not a fresh agenda create event).
 * 
 * All this effort is so that events due to a "modify" are "normalised". (In
 * other worse, when you modify an object, there should be one modify event).
 * 
 * Is basically a hash of a hash keyed on rule, then tuple key.
 * 
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 */
public class AgendaItemMap
    implements
    Serializable
{

    private final Map ruleMap;

    public AgendaItemMap()
    {
        ruleMap = new HashMap( );
    }

    public void putAgendaItem(Rule rule,
                              TupleKey tupleKey,
                              AgendaItem item)
    {
        put( rule,
             tupleKey,
             item );
    }

    public AgendaItem removeAgendaItem(Rule rule,
                                       TupleKey tupleKey)
    {
        return (AgendaItem) remove( rule,
                                    tupleKey );
    }

    // package protected methods for grey box testing...
    void put(Rule rule,
             TupleKey tupleKey,
             Object obj)
    {
        Map tupleMap = (Map) ruleMap.get( rule.getName( ) );
        if ( tupleMap == null )
        {
            tupleMap = new HashMap( );
            ruleMap.put( rule.getName( ),
                         tupleMap );
        }
        tupleMap.put( tupleKey,
                      obj );
    }

    Object remove(Rule rule,
                  TupleKey tupleKey)
    {
        Map tupleMap = (Map) ruleMap.get( rule.getName( ) );

        if ( tupleMap == null )
        {
            return null;
        }

        Object val = tupleMap.remove( tupleKey );

        if ( tupleMap.values( ).size( ) == 0 )
        {
            ruleMap.remove( rule.getName( ) );
        }

        return val;

    }

    public boolean isEmpty()
    {
        return ruleMap.isEmpty( );
    }

    void removeAll(RemoveDelegate removeDelegate)
    {

        for ( Iterator tupleMapList = ruleMap.values( ).iterator( ); tupleMapList.hasNext( ); )
        {
            Map tupleMap = (Map) tupleMapList.next( );
            for ( Iterator itemList = tupleMap.values( ).iterator( ); itemList.hasNext( ); )
            {
                Object item = itemList.next( );
                removeDelegate.processRemove( item );
                itemList.remove( );
            }
            tupleMapList.remove( );

        }

    }

    static interface RemoveDelegate
    {
        void processRemove(Object obj);
    }

}
