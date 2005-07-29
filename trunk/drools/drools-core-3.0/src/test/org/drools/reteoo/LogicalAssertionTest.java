package org.drools.reteoo;

import java.util.HashMap;
import java.util.Map;

import org.drools.DroolsTestCase;
import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.ClassObjectType;
import org.drools.spi.Consequence;

public class LogicalAssertionTest extends DroolsTestCase
{

    public void testSingleLogicalRelationship() throws Exception
    {
        /* create a RuleBase with a single ObjectTypeNode
         * we attach a MockObjectSink so we can detect 
         * assertions and retractions
         */
        Rete rete = new Rete();        
        ObjectTypeNode objectTypeNode = rete.getOrCreateObjectTypeNode( new ClassObjectType( String.class ) );        
        MockObjectSink sink = new MockObjectSink();
        objectTypeNode.addObjectSink( sink );                
        RuleBase ruleBase = new RuleBaseImpl( rete );
        WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory();

        final Agenda agenda = workingMemory.getAgenda();

        Consequence consequence = new Consequence() {
            public void invoke(org.drools.spi.Tuple tuple)
            {
                // do nothing
            }
        };    
        final Rule rule1 = new Rule( "test-rule1" );
        rule1.setConsequence( consequence );

        FactHandleImpl handle1 = new FactHandleImpl( 1 );        
        ReteTuple tuple1 = new ReteTuple( 0,
                                         handle1,
                                         workingMemory );

        final PropagationContext context1 = new PropagationContext( PropagationContext.ASSERTION,
                                                                    null,
                                                                    null );        

        /* Test single activation for a single logical assertions */
        agenda.addToAgenda( tuple1,
                            context1,
                            rule1 );
        ModuleImpl main = ( ModuleImpl ) agenda.getFocus();
        Activation activation1 = ( Activation ) main.getActivationQueue().get();
        
        String logicalString = "logical";
        FactHandle logicalHandle = workingMemory.assertObject( logicalString,
                                                               false,
                                                               true,
                                                               rule1,
                                                               activation1 );                         
        agenda.removeFromAgenda( tuple1.getKey(),
                                 context1,
                                 rule1 );
        
        assertLength( 1,
                      sink.getRetracted() );         
        
        Object[] values = ( Object[] ) sink.getRetracted().get( 0 );
        
        assertSame( logicalHandle, values[0] );
        
        /* Test single activation for a single logical assertions.
         * This also tests that logical assertions live on after
         * the related Activation has fired.
         */
        agenda.addToAgenda( tuple1,
                            context1,
                            rule1 );
        activation1 = ( Activation ) main.getActivationQueue().get();
        
        logicalHandle = workingMemory.assertObject( logicalString,
                                                    false,
                                                    true,
                                                    rule1,
                                                    activation1 );            
        
        agenda.fireNextItem( null );
       
        agenda.removeFromAgenda( tuple1.getKey(),
                                 context1,
                                 rule1 );
        
        assertLength( 2,
                      sink.getRetracted() );         
        
        values = ( Object[] ) sink.getRetracted().get( 1 );
        
        assertSame( logicalHandle, values[0] );     
    }
                
    
}
