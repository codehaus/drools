package org.drools.reteoo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Declaration;
import org.drools.rule.Extraction;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.InstrumentedExtractor;
import org.drools.spi.MockObjectType;

public class RuleBaseTest extends DroolsTestCase
{

    public RuleBaseTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {

    }

    public void testSerialize() throws Exception
    {
        Rule rule1 = new Rule( "test-rule 1" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        Declaration localDecl = new Declaration( new MockObjectType( true ),
                                                 "localVar" );

        Extraction extraction = new Extraction( localDecl,
                                                new InstrumentedExtractor() );

        rule1.addParameterDeclaration( paramDecl );
        rule1.addExtraction( extraction );

        //add consequence
        rule1.setConsequence( new org.drools.spi.InstrumentedConsequence() );

        //add conditions
        rule1.addCondition( new org.drools.spi.InstrumentedCondition() );
        rule1.addCondition( new org.drools.spi.InstrumentedCondition() );

        rule1.setSalience( 42 );

        Rule rule2 = new Rule( "test-rule 2" );
        paramDecl = new Declaration( new MockObjectType( true ),
                                     "paramVar" );

        localDecl = new Declaration( new MockObjectType( true ),
                                     "localVar" );

        extraction = new Extraction( localDecl,
                                     new InstrumentedExtractor());

        rule2.addParameterDeclaration( paramDecl );
        rule2.addExtraction( extraction );

        //add consequence
        rule2.setConsequence( new org.drools.spi.InstrumentedConsequence() );

        //add conditions
        rule2.addCondition( new org.drools.spi.InstrumentedCondition() );
        rule2.addCondition( new org.drools.spi.InstrumentedCondition() );
        rule2.setSalience( 12 );

        RuleSet ruleSet= new RuleSet( "rule_set" );
        ruleSet.addRule(rule1);
        ruleSet.addRule(rule2);

        Builder builder = new Builder();
        builder.addRuleSet(ruleSet);
        builder.setConflictResolver(DefaultConflictResolver.getInstance());
        RuleBase ruleBase = builder.buildRuleBase();

        // Serialize to a byte array
        System.out.println("Serialise out");
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutput out = new ObjectOutputStream(bos) ;
        out.writeObject(ruleBase);
        out.close();

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray();

        // Deserialize from a byte array
        System.out.println("Serialise in");
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        ruleBase = (RuleBase) in.readObject();
        in.close();
    }
}
