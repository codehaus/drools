package org.drools.reteoo;

/*
 $Id: ReteTuple.java,v 1.11 2002-07-27 02:37:12 bob Exp $

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

import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/** Base Rete-OO {@link Tuple} implementation.
 *
 *  @see Tuple
 *  
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class ReteTuple implements Tuple
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Key colums for this tuple. */
    private TupleKey keyColumns;

    /** Other columns in this tuple. */
    private Map otherColumns;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------
    
    /** Construct.
     */
    public ReteTuple()
    {
        this.keyColumns      = new TupleKey();
        this.otherColumns    = new HashMap();
    }

    /** Copy constructor.
     *
     *  @param that The tuple to copy.
     */
    public ReteTuple(ReteTuple that)
    {
        this.keyColumns      = new TupleKey( that.keyColumns );
        this.otherColumns    = new HashMap( that.otherColumns );
    }

    /** Construct a simple 1-column tuple.
     *
     *  @param declaration The column declaration.
     *  @param value The column value.
     */
    public ReteTuple(Declaration declaration,
                     Object value)
    {
        this();
        putKeyColumn( declaration,
                      value );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------
    
    /** Set a key column's value.
     *
     *  @param declaration The column declaration.
     *  @param value The value.
     */
    public void putKeyColumn(Declaration declaration,
                             Object value)
    {
        this.keyColumns.put( declaration,
                             value );
    }

    /** Add all columns from another tuple.
     *
     *  @param that The column source tuple.
     */
    public void putAll(ReteTuple that)
    {
        this.keyColumns.putAll( that.keyColumns );
        this.otherColumns.putAll( that.otherColumns );
    }

    /** Set an other column's value.
     *
     *  @param declaration The column declaration.
     *  @param value The value.
     */
    public void putOtherColumn(Declaration declaration,
                               Object value)
    {
        this.otherColumns.put( declaration,
                               value );
    }

    /** Retrieve the key for this tuple.
     *
     *  @return The key.
     */
    public TupleKey getKey()
    {
        return this.keyColumns;
    }

    /** Retrieve the other columns for this tuple.
     *
     *  @return The other columns.
     */
    public Map getOtherColumns()
    {
        return this.otherColumns;
    }

    /** Determine if this tuple depends upon
     *  a specified object.
     *
     *  @param object The object to test.
     *
     *  @return <code>true</code> if this tuple depends upon
     *          the specified object, otherwise <code>false</code>.
     */
    public boolean dependsOn(Object object)
    {
        return this.keyColumns.containsRootFactObject( object );
    }

    /** Retrieve the value of a particular declaration column.
     *
     *  @param declaration The declaration.
     *
     *  @return The value.
     */
    public Object get(Declaration declaration)
    {
        if ( this.keyColumns.containsDeclaration( declaration ) )
        {
            return this.keyColumns.get( declaration );
        }
        
        return this.otherColumns.get( declaration );
    }

    /** Retrieve all declarations participating in this tuple.
     *
     *  @return Set of all declarations.
     */
    public Set getDeclarations()
    {
        Set decls = new HashSet( this.keyColumns.size() 
                                 + this.otherColumns.size() );

        decls.addAll( this.keyColumns.getDeclarations() );
        decls.addAll( this.otherColumns.keySet() );

        return decls;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Produce a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return "[ReteTuple: key=" + this.keyColumns + "; others=" + this.otherColumns + "]";
    }
}
