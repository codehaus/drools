package org.drools.conflict;

/*
 $Id: SalienceConflictResolver.java,v 1.6 2004-07-04 11:45:43 mproctor Exp $

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

import java.util.List;
import java.util.ListIterator;

import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.ConflictResolver;

/**
 * <code>ConflictResolver</code> that uses the salience of rules to resolve
 * conflict.
 *
 * @see #getInstance
 * @see Rule#setSalience
 * @see Rule#getSalience
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 *
 * @version $Id: SalienceConflictResolver.java,v 1.3 2004/06/25 02:46:39
 *          mproctor Exp $
 */
public class SalienceConflictResolver implements ConflictResolver
{
	// ----------------------------------------------------------------------
	//     Class members
	// ----------------------------------------------------------------------

	/** Singleton instance. */
	private static final SalienceConflictResolver INSTANCE = new SalienceConflictResolver( );

	// ----------------------------------------------------------------------
	//     Class methods
	// ----------------------------------------------------------------------

	/**
	 * Retrieve the singleton instance.
	 *
	 * @return The singleton instance.
	 */
	public static ConflictResolver getInstance()
	{
		return INSTANCE;
	}

	// ----------------------------------------------------------------------
	//     Constructors
	// ----------------------------------------------------------------------

	/**
	 * Construct.
	 */
	public SalienceConflictResolver()
	{
		// intentionally left blank
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * @see ConflictResolver
	 */
	public List insert( Activation activation, List list )
	{
		ListIterator listIter;
		Activation eachActivation;
		int salience = activation.getRule( ).getSalience( );

		//quick optimisation, check if should just add to end
		if ( !list.isEmpty( ) )
		{
			eachActivation = (Activation) list.get( list.size( ) - 1 );
			if ( eachActivation.getRule( ).getSalience( ) > salience )
			{
				list.add( activation );
				return null;
			}
			//else get and return the conflicting items as a sublist
			else if ( eachActivation.getRule( ).getSalience( ) == salience )
			{
				int endIndex = list.size( );
				int startIndex = list.size( ) - 1;
				while ( (eachActivation != null)
						&& eachActivation.getRule( ).getSalience( ) == salience )
				{
					--startIndex;
					if ( startIndex >= 0 )
					{
						eachActivation = (Activation) list.get( startIndex );
					}
					else
					{
						eachActivation = null;
					}
				}
				return list.subList( startIndex + 1, endIndex );
			}

		}
		else
		{
			list.add( activation );
			return null;
		}

		// Traverse the list. If an activation is found
		// that has a lower salience than the item to be inserted,
		// insert the item *before* it by backing up and adding
		// to the list. Then return a list of any conflicts
		for ( listIter = list.listIterator( ); listIter.hasNext( ); )
		{
			eachActivation = (Activation) listIter.next( );
			if ( eachActivation.getRule( ).getSalience( ) <= salience )
			{
				//do we still have any conflicts
				int startIndex = listIter.previousIndex( );
				while ( eachActivation.getRule( ).getSalience( ) == salience )
				{
					eachActivation = (Activation) listIter.next( );
				}
				int endIndex = listIter.previousIndex( );
				if ( startIndex == endIndex )
				{
					listIter.previous( );
					listIter.add( activation );
					return null;
				}
				//if conflicts exist return a sublist of the conflicting items
				else
				{
					return list.subList( startIndex, endIndex );
				}
			}
		}

		// If not inserted by now, simply tack it onto the end.
		list.add( activation );
		return null;
	}

}

