package org.drools.conflict;

import org.drools.DroolsTestCase;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.Tuple;

public class SalienceConflictResolutionStrategyTest
    extends DroolsTestCase
{
    public void testCompare_Equal()
        throws Exception
    {
        final Rule rule = new Rule( "rule" );
        rule.setSalience( 42 );

        Activation activation = new Activation()
            {
                public Rule getRule()
                {
                    return rule;
                }

                public Tuple getTuple()
                {
                    return null;
                }
            };

        SalienceConflictResolutionStrategy strategy = SalienceConflictResolutionStrategy.getInstance();

        assertEquals( 0,
                      strategy.compare( activation,
                                        activation ) );
    }

    public void testCompare_FirstSecond()
        throws Exception
    {
        final Rule rule1 = new Rule( "rule-1" );

        rule1.setSalience( 42 );

        Activation activation1 = new Activation()
            {
                public Rule getRule()
                {
                    return rule1;
                }

                public Tuple getTuple()
                {
                    return null;
                }
            };

        final Rule rule2 = new Rule( "rule-2" );

        rule2.setSalience( 0 );

        Activation activation2 = new Activation()
            {
                public Rule getRule()
                {
                    return rule2;
                }

                public Tuple getTuple()
                {
                    return null;
                }
            };

        SalienceConflictResolutionStrategy strategy = SalienceConflictResolutionStrategy.getInstance();

        assertEquals( -1,
                      strategy.compare( activation1,
                                        activation2 ) );
    }

    public void testCompare_SecondFirst()
        throws Exception
    {
        final Rule rule1 = new Rule( "rule-1" );

        rule1.setSalience( 0 );

        Activation activation1 = new Activation()
            {
                public Rule getRule()
                {
                    return rule1;
                }

                public Tuple getTuple()
                {
                    return null;
                }
            };

        final Rule rule2 = new Rule( "rule-2" );

        rule2.setSalience( 42 );

        Activation activation2 = new Activation()
            {
                public Rule getRule()
                {
                    return rule2;
                }

                public Tuple getTuple()
                {
                    return null;
                }
            };

        SalienceConflictResolutionStrategy strategy = SalienceConflictResolutionStrategy.getInstance();

        assertEquals( 1,
                      strategy.compare( activation1,
                                        activation2 ) );
    }
}
