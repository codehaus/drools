package org.drools.reteoo;

/*
 $Id: ReteTuple.java,v 1.19 2003-12-05 04:26:23 bob Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
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

import org.drools.FactHandle;
import org.drools.spi.Tuple;
import org.drools.rule.Declaration;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/** Base Rete-OO <code>Tuple</code> implementation.
 *
 *  @see Tuple
 *  
 *  @author <a href="mailto:bob@werken.com">bob mcwhirter</a>
 *
 *  @version $Id: ReteTuple.java,v 1.19 2003-12-05 04:26:23 bob Exp $ 
 */
class ReteTuple
    implements Tuple
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Key colums for this tuple. */
    private TupleKey key;

    /** Value columns in this tuple. */
    private Map columns;

    private Map objectToHandle;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------
    
    /** Construct.
     */
    public ReteTuple()
    {
        this.key            = new TupleKey();
        this.columns        = new HashMap();
        this.objectToHandle = new HashMap();
    }

    /** Copy constructor.
     *
     *  @param that The tuple to copy.
     */
    ReteTuple(ReteTuple that)
    {
        this.key            = new TupleKey( that.key );
        this.columns        = new HashMap( that.columns );
        this.objectToHandle = new HashMap( that.objectToHandle );
    }

    /** Construct a simple 1-column tuple.
     *
     *  @param declaration The column declaration.
     *  @param handle The fact-handle.
     *  @param value The column value.
     */
    ReteTuple(Declaration declaration,
              FactHandle handle,
              Object value)
    {
        this();
        putKeyColumn( declaration,
                      handle,
                      value );
    }

    public String toString()
    {
        return "[Tuple: key=" + this.key + "; columns=" + this.columns + "; o2h=" + this.objectToHandle + "]";
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------
    
    /** Set a key column's value.
     *
     *  @param declaration The column declaration.
     *  @param handle The fact-handle.
     *  @param value The value.
     */
    public void putKeyColumn(Declaration declaration,
                             FactHandle handle,
                             Object value)
    {
        this.key.put( declaration,
                      handle );

        this.objectToHandle.put( value,
                                 handle );
        
        putColumn( declaration,
                   value );
    }

    /** Add all columns from another tuple.
     *
     *  @param that The column source tuple.
     */
    public void putAll(ReteTuple that)
    {
        this.key.putAll( that.key );
        this.columns.putAll( that.columns );
        this.objectToHandle.putAll( that.objectToHandle );
    }

    /** Set an other column's value.
     *
     *  @param declaration The column declaration.
     *  @param value The value.
     */
    public void putColumn(Declaration declaration,
                          Object value)
    {
        this.columns.put( declaration,
                               value );
    }

    /** Retrieve the key for this tuple.
     *
     *  @return The key.
     */
    TupleKey getKey()
    {
        return this.key;
    }

    /** Retrieve the other columns for this tuple.
     *
     *  @return The other columns.
     */
    Map getOtherColumns()
    {
        return this.columns;
    }

    /** Determine if this tuple depends upon
     *  a specified object.
     *
     *  @param handle The object handle to test.
     *
     *  @return <code>true</code> if this tuple depends upon
     *          the specified object, otherwise <code>false</code>.
     */
    boolean dependsOn(FactHandle handle)
    {
        return this.key.containsRootFactHandle( handle );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see Tuple
     */
    public Object get(Declaration declaration)
    {
        return this.columns.get( declaration );
    }

    /** @see Tuple
     */
    public Set getDeclarations()
    {
        return this.columns.keySet();
    }

    /** @see Tuple
     */
    public FactHandle getFactHandleForObject(Object object)
    {
        return (FactHandle) this.objectToHandle.get( object );
    }
}