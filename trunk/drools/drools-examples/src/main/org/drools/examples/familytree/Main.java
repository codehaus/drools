package org.drools.examples.familyTree;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseBuilder;

import java.io.*;
import java.util.*;
import java.net.*;
/**
 * @author searle
 *
 */
public final class Main {

	public static Map tree = new Hashtable();

 
	static public Person getFamilyTree(final WorkingMemory workingMemory ) throws Exception {
		final Person Balbo = new Person("Balbo", true);
		final Person Mungo = new Person("Mungo", true);
		final Person Bungo = new Person("Bungo", true);
		final Person Bilbo = new Person("Bilbo", true);
		Balbo.getChildren().add(Mungo);
		Mungo.getChildren().add(Bungo);
		Bungo.getChildren().add(Bilbo);

		final Person Lar	 = new Person("Lar", true);
		final Person Fosco = new Person("Fosco", true);
		final Person Drogo = new Person("Drogo", true);
		final Person Frodo = new Person("Frodo", true);
		Balbo.getChildren().add(Lar);
		Lar.getChildren().add(Fosco);
		Fosco.getChildren().add(Drogo);
		Drogo.getChildren().add(Frodo);


		//Cheating
		final Person Laura = new Person ("Laura", false);
		Laura.getChildren().add(Bungo);
		workingMemory.assertObject(Laura);

		final Person Belladonna = new Person ("Belladonna", false);
		Belladonna.getChildren().add(Bilbo);
		workingMemory.assertObject(Belladonna);

		final Person Tanta = new Person ("Tanta", false);
		Tanta.getChildren().add(Fosco);
		workingMemory.assertObject(Tanta);

		final Person Ruby = new Person ("Ruby", false);
		Ruby.getChildren().add(Drogo);
		workingMemory.assertObject(Ruby);

		final Person Primula = new Person ("Primula", false);
		Primula.getChildren().add(Frodo);
		workingMemory.assertObject(Primula);

		return Balbo;
        }

    public static void main(String[] args) throws Exception
    {
            // First, construct an empty RuleBase to be the
            // container for your rule logic.

            URL url = Main.class.getResource( "familyTree.drl" );
            RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( url );


            // Create a [org.drools.WorkingMemory] to be the
            // container for your facts
            
            final WorkingMemory workingMemory = ruleBase.newWorkingMemory();

                // Now, simply assert them into the [org.drools.WorkingMemory]
                // and let the logic engine do the rest.

                workingMemory.assertObject( getFamilyTree(workingMemory) );
    }
}
