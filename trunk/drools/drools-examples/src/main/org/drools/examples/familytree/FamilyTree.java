package org.drools.examples.familytree;

/*
$Id: FamilyTree.java,v 1.1 2004-07-07 04:45:21 dbarnett Exp $

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

import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseBuilder;
;
/**
 * @author searle
 *  
 */
public final class FamilyTree
{
	public static Map tree = new Hashtable( );

	/**
	 * Construct the family return and return the root Person, Balbo
	 */
	static public Person getFamilyTree( final WorkingMemory workingMemory ) throws Exception
	{
		final Person Balbo = new Person( "Balbo", true );
		final Person Mungo = new Person( "Mungo", true );
		final Person Bungo = new Person( "Bungo", true );
		final Person Bilbo = new Person( "Bilbo", true );
		Balbo.getChildren( ).add( Mungo );
		Mungo.getChildren( ).add( Bungo );
		Bungo.getChildren( ).add( Bilbo );

		final Person Lar = new Person( "Lar", true );
		final Person Fosco = new Person( "Fosco", true );
		final Person Drogo = new Person( "Drogo", true );
		final Person Frodo = new Person( "Frodo", true );
		Balbo.getChildren( ).add( Lar );
		Lar.getChildren( ).add( Fosco );
		Fosco.getChildren( ).add( Drogo );
		Drogo.getChildren( ).add( Frodo );

		//Cheating
		final Person Laura = new Person( "Laura", false );
		Laura.getChildren( ).add( Bungo );
		workingMemory.assertObject( Laura );

		final Person Belladonna = new Person( "Belladonna", false );
		Belladonna.getChildren( ).add( Bilbo );
		workingMemory.assertObject( Belladonna );

		final Person Tanta = new Person( "Tanta", false );
		Tanta.getChildren( ).add( Fosco );
		workingMemory.assertObject( Tanta );

		final Person Ruby = new Person( "Ruby", false );
		Ruby.getChildren( ).add( Drogo );
		workingMemory.assertObject( Ruby );

		final Person Primula = new Person( "Primula", false );
		Primula.getChildren( ).add( Frodo );
		workingMemory.assertObject( Primula );

		return Balbo;
	}

	public static void main( String[] args )
	{
        if (args.length != 1) {
            System.out.println("Usage: " + FamilyTree.class.getName() + " [drl file]");
            return;
        }
        System.out.println("Using drl: " + args[0]);
		// First, construct an empty RuleBase to be the
		// container for your rule logic.
        try {
            URL url = FamilyTree.class.getResource( args[0] );
            RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( url );

            // Create a [org.drools.WorkingMemory] to be the
            // container for your facts
            final WorkingMemory workingMemory = ruleBase.newWorkingMemory( );

            // Now, simply assert them into the [org.drools.WorkingMemory]
            // and let the logic engine do the rest.
            workingMemory.assertObject( getFamilyTree( workingMemory ) );
            workingMemory.fireAllRules( );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
