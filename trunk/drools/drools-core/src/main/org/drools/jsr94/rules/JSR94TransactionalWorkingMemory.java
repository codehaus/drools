package org.drools.jsr94.rules;

/*
 $Id: JSR94TransactionalWorkingMemory.java,v 1.5 2003-06-19 09:28:35 tdiesler Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import org.apache.commons.collections.SequencedHashMap;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.RuleBase;
import org.drools.TransactionalWorkingMemory;

import javax.rules.Handle;
import javax.rules.InvalidRuleSessionException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Provide access to the list of objects currently asserted to the working memory.
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class JSR94TransactionalWorkingMemory extends TransactionalWorkingMemory
{

    // list of Handles
    private Map objectMap = new SequencedHashMap();

    // the next handle id
    private static BigInteger nextHandleid = new BigInteger( "1" );

    /**
     * Construct a new transactional working memory for a ruleBase.
     *
     *  @param ruleBase The rule base with which this memory is associated.
     */
    JSR94TransactionalWorkingMemory( RuleBase ruleBase )
    {
        super( ruleBase );
    }

    /**
     * Gets the next <code>Handle</code> for this <code>RuleRuntime</code>.
     */
    Handle getNextHandle()
    {
        Handle handle = new HandleImpl( nextHandleid );
        nextHandleid = nextHandleid.add( new BigInteger( "1" ) );
        return handle;
    }

    /**
     * Get a collection of handles currently asserted to the working memory.
     */
    Collection getObjectHandles()
    {
        return objectMap.keySet();
    }

    /**
     * Get a collection of handles currently asserted to the working memory.
     */
    Collection getObjects()
    {
        return objectMap.values();
    }


    Object getObject( Handle handle )
    {
        return objectMap.get( handle );
    }

    /**
     * Assert a new object with the given handle into this working memory.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    void assertObjectForHandle( Handle handle, Object object ) throws AssertionException
    {
        objectMap.put( handle, object );
        super.assertObject( object );
    }

    /**
     * Retract an object with the given handle from this working memory.
     *
     *  @param handle The handle to retract.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    void removeObjectForHandle( Handle handle ) throws RetractionException, InvalidRuleSessionException
    {
        Object object = objectMap.get( handle );
        if ( object == null ) throw new InvalidRuleSessionException( "invalid handle: " + handle );
        super.retractObject( object );
        objectMap.remove( handle );
    }

    /**
     * Assert a new fact object into this working memory.
     * <p>
     * When a fact is asserted during <code>StatefulRuleSession.executeRules</code> we commit immediately.
     *
     *  @param object The object to assert.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    public void assertObject( Object object ) throws AssertionException
    {
        Handle handle = getNextHandle();
        objectMap.put( handle, object );
        super.assertObject( object );
    }

    /** Retract a fact object from this working memory.
     *
     *  @param object The object to retract.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    public void retractObject( Object object ) throws RetractionException
    {
        super.retractObject( object );

        // Note: this is really bad, for each removal we have to scan the entire map.
        // Any other ways?
        Iterator itKeys = objectMap.keySet().iterator();
        while ( itKeys.hasNext() )
        {
            Handle handle = (Handle) itKeys.next();
            if ( objectMap.get( handle ) == object )
            {
                objectMap.remove( handle );
                break;
            }
        }
    }
}
