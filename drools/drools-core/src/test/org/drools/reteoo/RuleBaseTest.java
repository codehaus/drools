package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.InstrumentedExtractor;
import org.drools.spi.MockObjectType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class RuleBaseTest extends DroolsTestCase
{
    public void testSerialize() throws Exception
    {
        Rule rule1 = new Rule( "test-rule 1" );
        rule1.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        rule1.addLocalDeclaration( "localVar", new MockObjectType( true ) );

        rule1.addExtraction( "localVar", new InstrumentedExtractor() );

        //add consequence
        rule1.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        //add conditions
        rule1.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule1.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        rule1.setSalience( 42 );

        Rule rule2 = new Rule( "test-rule 2" );

        rule2.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        rule2.addLocalDeclaration( "localVar", new MockObjectType( true ) );

        rule2.addExtraction( "localVar", new InstrumentedExtractor( ) );

        //add consequence
        rule2.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        //add conditions
        rule2.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule2.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule2.setSalience( 12 );

        RuleSet ruleSet = new RuleSet( "rule_set" );
        ruleSet.addRule( rule1 );
        ruleSet.addRule( rule2 );

        Builder builder = new Builder( );
        builder.addRuleSet( ruleSet );
        builder.setConflictResolver( DefaultConflictResolver.getInstance( ) );
        RuleBase ruleBase = builder.buildRuleBase( );

        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( ruleBase );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream( new ByteArrayInputStream( bytes ) );
        in.readObject( );
        in.close( );
    }
}