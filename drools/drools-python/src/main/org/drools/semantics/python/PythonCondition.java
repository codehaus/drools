package org.drools.semantics.python;

/*
 * $Id: PythonCondition.java,v 1.4 2005-11-10 04:54:43 mproctor Exp $
 *
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.smf.SemanticComponent;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;
import org.python.core.PyObject;
import org.python.core.__builtin__;

/**
 * Python expression semantics <code>Condition</code>.
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
public class PythonCondition extends PythonInterp implements Condition, SemanticComponent
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------
    protected final String        semanticType    = "python";

    protected final String        name;
    
    /** Required declarations. */
    private Declaration[] requiredDeclarations;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    public PythonCondition( String text,
                            Rule rule ) throws Exception
    {
        super( text,
               rule,
               "eval" );
        
        this.name = "condition";

        PythonExprAnalyzer analyzer = new PythonExprAnalyzer();

        this.requiredDeclarations = analyzer.analyze( getNode( ),
                                                      rule.getParameterDeclarations( ) );
    }
    
    public String getSemanticType()
    {
        return semanticType;
    }

    public String getName()
    {
        return this.name;
    }    

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.spi.Condition
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Determine if the supplied <code>Tuple</code> is allowed by this
     * condition.
     *
     * @param tuple The <code>Tuple</code> to test.
     *
     * @return <code>true</code> if the <code>Tuple</code> passes this
     *         condition, else <code>false</code>.
     *
     * @throws ConditionException if an error occurs during filtering.
     */
    public boolean isAllowed( Tuple tuple ) throws ConditionException
    {
        try
        {

            PyObject result = __builtin__.eval( getCode( ),
                                                setUpDictionary( tuple, declarationIterator( )  ),
                                                getGlobals( ) );

            Object answer = result.__tojava__( Object.class );

            if ( !( answer instanceof Number ) )
            {
                throw new NonBooleanExprException( getText( ) );
            }

            return ( ( Number ) answer ).intValue( ) != 0;
        }
        catch ( Exception e )
        {
            throw new ConditionException( e,
                                          getRule( ),
                                          getText( ) );
        }
    }

    /**
     * Retrieve the array of <code>Declaration</code> s required by this condition to perform its duties.
     *
     * @return The array of <code>Declarations</code> expected on incoming <code>Tuples</code>.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredDeclarations;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public int hashCode()
    {
        return this.getText( ).hashCode( );
    }

    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null || getClass( ) != object.getClass( ) )
        {
            return false;
        }

        return this.getText( ).equals( ( ( PythonInterp ) object ).getText( ) );
    }
    
    /**
     * PythonInterp needs a declaration iterator.
     * BlockConsequence uses the Iterator from Set.
     * So we emulate Iterator here so PythonInterp can be used for both.
     * @return
     */
    public Iterator declarationIterator( ) 
    {
        return new Iterator()
        {
            private int index=0;
            
            public void remove()
            {
                //null;
            }
            
            public boolean hasNext()
            {
                return (index < requiredDeclarations.length);
            }            
            
            public Object next()
            {
                if ( !hasNext( ) )
                {
                    throw new NoSuchElementException( );
                }
                return requiredDeclarations[this.index++];
            }
            
        };
    }
    
}