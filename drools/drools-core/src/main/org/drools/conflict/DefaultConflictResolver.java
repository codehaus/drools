package org.drools.conflict;

/*
 $Id: DefaultConflictResolver.java,v 1.4 2004-06-26 17:54:53 mproctor Exp $

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.io.Serializable;

import org.drools.spi.Activation;
import org.drools.spi.ConflictResolver;
import org.drools.spi.Tuple;

/**
 * Strategy for resolving conflicts amongst multiple rules.
 * 
 * <p>
 * Since a fact or set of facts may activate multiple rules, a
 * <code>ConflictResolutionStrategy</code> is used to provide priority
 * ordering of conflicting rules.
 * </p>
 * 
 * @see Activation
 * @see Tuple
 * @see org.drools.rule.Rule
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * 
 * @version $Id: DefaultConflictResolver.java,v 1.2 2004/06/25 11:46:58 mproctor
 *          Exp $
 */
public class DefaultConflictResolver implements ConflictResolver, Serializable
{
	// ----------------------------------------------------------------------
	//     Class members
	// ----------------------------------------------------------------------

	/** Singleton instance. */
	private static final DefaultConflictResolver INSTANCE = new DefaultConflictResolver( );
	private static List conflictResolvers;

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

	/**
	 * Setup a default ConflictResolver configuration
	 */
	public DefaultConflictResolver()
	{
		if ( (conflictResolvers == null) || (conflictResolvers.isEmpty( )) )
		{
			conflictResolvers = new ArrayList( );
			conflictResolvers.add( SalienceConflictResolver.getInstance( ) );
			conflictResolvers.add( ComplexityConflictResolver.getInstance( ) );
			//conflictResolvers.add(BreadthConflictResolver.getInstance());
			conflictResolvers.add( LoadOrderConflictResolver.getInstance( ) );
		}
	}

	public static void setStrategies( List strategyList )
	{
		conflictResolvers = strategyList;
	}

	public List insert( Activation activation, List list )
	{
		ConflictResolver conflictResolver;
		Iterator iterator = conflictResolvers.iterator( );
		while ( (list != null) && iterator.hasNext( ) )
		{
			conflictResolver = (ConflictResolver) iterator.next( );
			list = conflictResolver.insert( activation, list );
		}
		return list;
	}
}