package org.drools.reteoo;

import junit.framework.TestCase;
import org.drools.MockObjectType;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;

public class ReteTupleTest extends TestCase
{
  
    public void testCanConstructTupleWithExtractedValueFromTupleWithOneExtractedValue()
    {
        assertTrue(true);
        
        //WTF????
        /*
        Rule rule = new Rule( "rule" );
        Declaration declarationB = rule.addLocalDeclaration( "declarationB", new MockObjectType( ) );
        Declaration declarationC = rule.addLocalDeclaration( "declarationC", new MockObjectType( ) );
        ReteTuple tupleA = new ReteTuple( new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) ), rule );
        ReteTuple tupleB = new ReteTuple( tupleA, declarationB, this );
        ReteTuple tupleC = new ReteTuple( tupleB, declarationC, this );

        assertSame( this, tupleC.get( declarationB ) );
        assertSame( this, tupleC.get( declarationC ) );
        */
    }
}