package org.drools.examples;

/*
 * $Id: MannersNativeTest.java,v 1.2 2004-11-07 18:34:44 mproctor Exp $
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
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.WorkingMemory;
import org.drools.examples.model.Context;
import org.drools.examples.model.Guest;
import org.drools.examples.model.LastSeat;
import org.drools.examples.model.Seat;
import org.drools.examples.model.Seating;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.examples.ClassObjectType;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import java.io.Serializable;

/**
 * @author Andy Barnett
 */
public class MannersNativeTest extends TestCase implements Serializable
{
    /** Drools working memory. */
    private WorkingMemory workingMemory = null;

    /** Number of guests at the dinner (default: 16). */
    private int    numGuests  = 16;

    /** Number of seats at the table (default: 16). */
    private int    numSeats   = 16;

    /** Minimum number of hobbies each guest should have (default: 2). */
    private int    minHobbies = 2;

    /** Maximun number of hobbies each guest should have (default: 3). */
    private int    maxHobbies = 3;

    protected void setUp() throws Exception
    {
        // Reuse the Java semantics ObjectType
        // so Drools can identify the model classes
        ClassObjectType contextType = new ClassObjectType( Context.class );
        ClassObjectType guestType = new ClassObjectType( Guest.class );
        ClassObjectType seatingType = new ClassObjectType( Seating.class );
        ClassObjectType lastSeatType = new ClassObjectType( LastSeat.class );

        // <rule-set name="Miss Manners" ...>
        RuleSet ruleSet = new RuleSet( "Miss Manners" );

        // ===========================================
        // <rule name="find first seat" salience="40">
        // ===========================================
        Rule rule = new Rule( "find first seat" );
        rule.setSalience( 40 );

        // Build the declaration and specify it as a parameter of the Rule
        // <parameter identifier="context">
        //   <class>org.drools.examples.manners.model.Context</class>
        // </parameter>
        final Declaration contextDecl = new Declaration( contextType, "context" );
        rule.addParameterDeclaration( contextDecl );

        // <parameter identifier="guest">
        //   <class>org.drools.examples.manners.model.Guest</class>
        // </parameter>
        final Declaration guestDecl = new Declaration( guestType, "guest" );
        rule.addParameterDeclaration( guestDecl );

        // Build and Add the Condition to the Rule
        // <java:condition>context.isState("start")</java:condition>
        Condition condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Context context = ( Context ) tuple.get( contextDecl );
                return context.isState( "start" );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{contextDecl};
            }

            public String toString()
            {
                return "context.isState(\"start\")";
            }
        };
        rule.addCondition( condition );

        // Build and Add the Consequence to the Rule
        // <java:consequence>
        //   System.out.println("FIRE: find first seat: " + guest);
        //   import org.drools.examples.manners.model.Seating;
        //   drools.assertObject(new Seating(1, guest, null));
        //   context.setState("find_seating");
        //   drools.modifyObject(context);
        // </java:consequence>
        Consequence consequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                Context context = ( Context ) tuple.get( contextDecl );
                Guest guest = ( Guest ) tuple.get( guestDecl );

                try
                {
                    workingMemory.assertObject( new Seating( 1, guest, null ) );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }

                context.setState( "find_seating" );

                try
                {
                    workingMemory
                                 .modifyObject(
                                                tuple
                                                     .getFactHandleForObject( context ),
                                                context );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }
            }
        };
        rule.setConsequence( consequence );
        ruleSet.addRule( rule );

        // ========================================
        // <rule name="find seating" salience="30">
        // ========================================
        rule = new Rule( "find seating" );
        rule.setSalience( 30 );

        // Build the declaration and specify it as a parameter of the Rule
        // <parameter identifier="context">
        //   <class>org.drools.examples.manners.model.Context</class>
        // </parameter>
        //        final Declaration contextDecl = new Declaration(contextType,
        // "context");
        rule.addParameterDeclaration( contextDecl );

        // <parameter identifier="guest">
        //   <class>org.drools.examples.manners.model.Guest</class>
        // </parameter>
        //        final Declaration guestDecl = new Declaration(guestType, "guest");
        rule.addParameterDeclaration( guestDecl );

        // <parameter identifier="seating">
        //     <class>org.drools.examples.manners.model.Seating</class>
        // </parameter>
        final Declaration seatingDecl = new Declaration( seatingType, "seating" );
        rule.addParameterDeclaration( seatingDecl );

        // Build and Add the Condition to the Rule
        // <java:condition>context.isState("find_seating")</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Context context = ( Context ) tuple.get( contextDecl );
                return context.isState( "find_seating" );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{contextDecl};
            }

            public String toString()
            {
                return "context.isState(\"find_seating\")";
            }
        };
        rule.addCondition( condition );

        // <java:condition>seating.getGuest2() == null</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Seating seating = ( Seating ) tuple.get( seatingDecl );
                return seating.getGuest2( ) == null;
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{seatingDecl};
            }

            public String toString()
            {
                return "seating.getGuest2() == null";
            }
        };
        rule.addCondition( condition );

        // <java:condition>!seating.getTabooList().contains(guest)</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Seating seating = ( Seating ) tuple.get( seatingDecl );
                Guest guest = ( Guest ) tuple.get( guestDecl );
                return !seating.getTabooList( ).contains( guest );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{seatingDecl, guestDecl};
            }

            public String toString()
            {
                return "!seating.getTabooList().contains(guest)";
            }
        };
        rule.addCondition( condition );

        // <java:condition>seating.getGuest1().hasOppositeSex(guest)</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Seating seating = ( Seating ) tuple.get( seatingDecl );
                Guest guest = ( Guest ) tuple.get( guestDecl );
                return seating.getGuest1( ).hasOppositeSex( guest );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{seatingDecl, guestDecl};
            }

            public String toString()
            {
                return "seating.getGuest1().hasOppositeSex(guest)";
            }
        };
        rule.addCondition( condition );

        // <java:condition>seating.getGuest1().hasSameHobby(guest)</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Seating seating = ( Seating ) tuple.get( seatingDecl );
                Guest guest = ( Guest ) tuple.get( guestDecl );
                return seating.getGuest1( ).hasSameHobby( guest );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{seatingDecl, guestDecl};
            }

            public String toString()
            {
                return "seating.getGuest1().hasSameHobby(guest)";
            }
        };
        rule.addCondition( condition );

        // Build and Add the Consequence to the Rule
        // <java:consequence>
        //    System.out.println("FIRE: find seating: " + seating + " " + guest);
        //
        //    Seating nextSeat = new Seating(seating.getSeat2(), guest, seating);
        //    drools.assertObject(nextSeat);
        //
        //    seating.setGuest2(guest);
        //    seating.getTabooList().add(guest);
        //    drools.modifyObject(seating);
        // </java:consequence>
        consequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                Guest guest = ( Guest ) tuple.get( guestDecl );
                Seating seating = ( Seating ) tuple.get( seatingDecl );

                Seating nextSeat = new Seating( seating.getSeat2( ), guest,
                                                seating );
                try
                {
                    workingMemory.assertObject( nextSeat );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }

                seating.setGuest2( guest );
                seating.getTabooList( ).add( guest );

                try
                {
                    workingMemory
                                 .modifyObject(
                                                tuple
                                                     .getFactHandleForObject( seating ),
                                                seating );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }
            }
        };
        rule.setConsequence( consequence );
        ruleSet.addRule( rule );

        // ===========================================
        // <rule name="try another path" salience="20">
        // ===========================================
        rule = new Rule( "try another path" );
        rule.setSalience( 20 );

        // Build the declaration and specify it as a parameter of the Rule
        // <parameter identifier="context">
        //   <class>org.drools.examples.manners.model.Context</class>
        // </parameter>
        rule.addParameterDeclaration( contextDecl );

        // <parameter identifier="lastSeat">
        //     <class>org.drools.examples.manners.model.LastSeat</class>
        // </parameter>
        final Declaration lastSeatDecl = new Declaration( lastSeatType,
                                                          "lastSeat" );
        rule.addParameterDeclaration( lastSeatDecl );

        // <parameter identifier="seating">
        //     <class>org.drools.examples.manners.model.Seating</class>
        // </parameter>
        rule.addParameterDeclaration( seatingDecl );

        // <java:condition>context.isState("find_seating")</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Context context = ( Context ) tuple.get( contextDecl );
                return context.isState( "find_seating" );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{contextDecl};
            }

            public String toString()
            {
                return "context.isState(\"find_seating\")";
            }
        };
        rule.addCondition( condition );

        // <java:condition>lastSeat.getSeat() >
        // seating.getSeat1()</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                LastSeat lastSeat = ( LastSeat ) tuple.get( lastSeatDecl );
                Seating seating = ( Seating ) tuple.get( seatingDecl );
                return lastSeat.getSeat( ) > seating.getSeat1( );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{lastSeatDecl, seatingDecl};
            }

            public String toString()
            {
                return "lastSeat.getSeat() > seating.getSeat1()";
            }
        };
        rule.addCondition( condition );

        // <java:condition>seating.getGuest2() == null</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Seating seating = ( Seating ) tuple.get( seatingDecl );
                return seating.getGuest2( ) == null;
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{seatingDecl};
            }

            public String toString()
            {
                return "seating.getGuest2() == null";
            }
        };
        rule.addCondition( condition );

        // <java:consequence>
        //    System.out.println("FIRE: try another path: " + seating);
        //
        //    Seating prevSeat = seating.getPrevSeat();
        //    prevSeat.setGuest2(null);
        //    drools.modifyObject(prevSeat);
        //
        //    drools.retractObject(seating);
        // </java:consequence>
        consequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                Seating seating = ( Seating ) tuple.get( seatingDecl );

                Seating prevSeat = seating.getPrevSeat( );
                prevSeat.setGuest2( null );

                try
                {
                    workingMemory
                                 .modifyObject(
                                                tuple
                                                     .getFactHandleForObject( prevSeat ),
                                                prevSeat );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }

                try
                {
                    workingMemory
                                 .retractObject( tuple
                                                      .getFactHandleForObject( seating ) );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }
            }
        };
        rule.setConsequence( consequence );
        ruleSet.addRule( rule );

        // =======================================
        // <rule name="we are done" salience="10">
        // =======================================
        rule = new Rule( "we are done" );
        rule.setSalience( 10 );

        // Build the declaration and specify it as a parameter of the Rule
        // <parameter identifier="context">
        //   <class>org.drools.examples.manners.model.Context</class>
        // </parameter>
        rule.addParameterDeclaration( contextDecl );

        // <parameter identifier="lastSeat">
        //     <class>org.drools.examples.manners.model.LastSeat</class>
        // </parameter>
        rule.addParameterDeclaration( lastSeatDecl );

        // <parameter identifier="seating">
        //     <class>org.drools.examples.manners.model.Seating</class>
        // </parameter>
        rule.addParameterDeclaration( seatingDecl );

        // <java:condition>context.isState("find_seating")</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Context context = ( Context ) tuple.get( contextDecl );
                return context.isState( "find_seating" );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{contextDecl};
            }

            public String toString()
            {
                return "context.isState(\"find_seating\")";
            }
        };
        rule.addCondition( condition );

        // <java:condition>lastSeat.getSeat() ==
        // seating.getSeat1()</java:condition>
        condition = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                LastSeat lastSeat = ( LastSeat ) tuple.get( lastSeatDecl );
                Seating seating = ( Seating ) tuple.get( seatingDecl );
                return lastSeat.getSeat( ) == seating.getSeat1( );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{lastSeatDecl, seatingDecl};
            }

            public String toString()
            {
                return "lastSeat.getSeat() == seating.getSeat1()";
            }
        };
        rule.addCondition( condition );

        // <java:consequence>
        //    System.out.println("FIRE: we are done");
        //
        //    import org.drools.examples.manners.model.Seat;
        //    List list = new ArrayList();
        //    while(seating != null) {
        //        Seat seat = new Seat(seating.getSeat1(),
        // seating.getGuest1().getName());
        //        seating = seating.getPrevSeat();
        //        list.add(seat);
        //    }
        //
        //    for (int i = list.size(); i > 0; i--) {
        //        Seat seat = (Seat)list.get(i-1);
        //        System.out.println(seat);
        //        drools.assertObject(seat);
        //    }
        //
        //    context.setState("all_done");
        //    drools.modifyObject(context);
        // </java:consequence>
        consequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                Seating seating = ( Seating ) tuple.get( seatingDecl );
                Context context = ( Context ) tuple.get( contextDecl );

                List list = new ArrayList( );
                while ( seating != null )
                {
                    Seat seat = new Seat( seating.getSeat1( ),
                                          seating.getGuest1( ).getName( ) );
                    seating = seating.getPrevSeat( );
                    list.add( seat );
                }

                for ( int i = list.size( ); i > 0; i-- )
                {
                    Seat seat = ( Seat ) list.get( i - 1 );
                    try
                    {
                        workingMemory.assertObject( seat );
                    }
                    catch ( FactException e )
                    {
                        throw new ConsequenceException( e );
                    }
                }

                context.setState( "all_done" );
                try
                {
                    workingMemory
                                 .modifyObject(
                                                tuple
                                                     .getFactHandleForObject( context ),
                                                context );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }
            }
        };
        rule.setConsequence( consequence );
        ruleSet.addRule( rule );

        // ==================
        // Build the RuleSet.
        // ==================
        RuleBaseBuilder builder = new RuleBaseBuilder( );
        builder.addRuleSet( ruleSet );
        RuleBase ruleBase = builder.build( );
        workingMemory = getWorkingMemory( ruleBase );
        workingMemory.addEventListener(new TestWorkingMemoryEventListener());
    }

    protected void tearDown() throws Exception
    {
        workingMemory = null;
    }

    public void testManners() throws Exception
    {
        List inList = getInputObjects( generateData( ) );

        long start = System.currentTimeMillis( );

        //test serialization
        //workingMemory = serializeWorkingMemory( workingMemory );
        //workingMemory = serializeWorkingMemory( workingMemory );

        for ( Iterator i = inList.iterator( ); i.hasNext( ); )
        {
            workingMemory.assertObject( i.next( ) );
        }

        //test serialization
        //workingMemory = serializeWorkingMemory( workingMemory );
        //workingMemory = serializeWorkingMemory( workingMemory );

        workingMemory.fireAllRules( );

        //test serialization
        //workingMemory = serializeWorkingMemory( workingMemory );
        //workingMemory = serializeWorkingMemory( workingMemory );

        List outList = workingMemory.getObjects( );

        int actualGuests = validateResults( inList, outList );

        assertEquals(numGuests, actualGuests);

        TestWorkingMemoryEventListener listener = (TestWorkingMemoryEventListener) workingMemory.getListeners().get(0);
        assertEquals(50, listener.asserted);
        assertEquals(0, listener.retracted);
        assertEquals(17, listener.modified);
        //can't test this as it changes on each run
        //assertEquals(2024, listener.tested);
        assertEquals(96, listener.created);
        assertEquals(17, listener.fired);
        assertEquals(79, listener.cancelled);

    }


    /**
     * Verify that each guest has at least one common hobby with the guest
     * before him/her.
     */
    private static int validateResults(List inList, List outList)
    {
        int seatCount = 0;
        Guest lastGuest = null;
        Iterator it = outList.iterator( );
        while ( it.hasNext( ) )
        {
            Object obj = it.next( );
            if ( !( obj instanceof Seat ) )
            {
                continue;
            }

            Seat seat = ( Seat ) obj;
            if ( lastGuest == null )
            {
                lastGuest = guest4Seat( inList, seat );
            }

            Guest guest = guest4Seat( inList, seat );

            boolean hobbyFound = false;
            for ( int i = 0; !hobbyFound && i < lastGuest.getHobbies( ).size( ); i++ )
            {
                String hobby = ( String ) lastGuest.getHobbies( ).get( i );
                if ( guest.getHobbies( ).contains( hobby ) )
                {
                    hobbyFound = true;
                }
            }

            if ( !hobbyFound )
            {
                throw new RuntimeException( "seat: " + seat.getSeat( )
                                            + " no common hobby " + lastGuest
                                            + " -> " + guest );
            }
            seatCount++;
        }

        return seatCount;
    }

    /**
     * Gets the Guest object from the inList based on the guest name of the
     * seat.
     */
    private static Guest guest4Seat(List inList, Seat seat)
    {
        Iterator it = inList.iterator( );
        while ( it.hasNext( ) )
        {
            Object obj = it.next( );
            if ( !( obj instanceof Guest ) )
            {
                continue;
            }
            Guest guest = ( Guest ) obj;
            if ( guest.getName( ).equals( seat.getName( ) ) )
            {
                return guest;
            }
        }

        return null;
    }

    /**
     * Convert the facts from the <code>InputStream</code> to a list of
     * objects.
     */
    private List getInputObjects(InputStream inputStream) throws IOException
    {
        List list = new ArrayList( );

        BufferedReader br = new BufferedReader(
                                                new InputStreamReader(
                                                                       inputStream ) );

        Map guests = new HashMap( );

        String line = null;
        while ( ( line = br.readLine( ) ) != null )
        {
            if ( line.trim( ).length( ) == 0 || line.trim( ).startsWith( ";" ) )
            {
                continue;
            }
            StringTokenizer st = new StringTokenizer( line, "() " );
            String type = st.nextToken( );

            if ( "guest".equals( type ) )
            {
                if ( !"name".equals( st.nextToken( ) ) )
                {
                    throw new IOException( "expected 'name' in: " + line );
                }
                String name = st.nextToken( );
                if ( !"sex".equals( st.nextToken( ) ) )
                {
                    throw new IOException( "expected 'sex' in: " + line );
                }
                String sex = st.nextToken( );
                if ( !"hobby".equals( st.nextToken( ) ) )
                {
                    throw new IOException( "expected 'hobby' in: " + line );
                }
                String hobby = st.nextToken( );

                Guest guest = ( Guest ) guests.get( name );
                if ( guest == null )
                {
                    guest = new Guest( name, sex.charAt( 0 ) );
                    guests.put( name, guest );
                    list.add( guest );
                }
                guest.addHobby( hobby );
            }

            if ( "last_seat".equals( type ) )
            {
                if ( !"seat".equals( st.nextToken( ) ) )
                {
                    throw new IOException( "expected 'seat' in: " + line );
                }
                list
                    .add( new LastSeat(
                                        new Integer( st.nextToken( ) )
                                                                      .intValue( ) ) );
            }

            if ( "context".equals( type ) )
            {
                if ( !"state".equals( st.nextToken( ) ) )
                {
                    throw new IOException( "expected 'state' in: " + line );
                }
                list.add( new Context( st.nextToken( ) ) );
            }
        }
        inputStream.close( );

        return list;
    }

    private InputStream generateData()
    {
        final String LINE_SEPARATOR = System.getProperty( "line.separator" );

        StringWriter writer = new StringWriter( );

        int maxMale = numGuests / 2;
        int maxFemale = numGuests / 2;

        int maleCount = 0;
        int femaleCount = 0;

        // init hobbies
        List hobbyList = new ArrayList( );
        for ( int i = 1; i <= maxHobbies; i++ )
        {
            hobbyList.add( "h" + i );
        }

        Random rnd = new Random( );
        for ( int i = 1; i <= numGuests; i++ )
        {
            char sex = rnd.nextBoolean( ) ? 'm' : 'f';
            if ( sex == 'm' && maleCount == maxMale )
            {
                sex = 'f';
            }
            if ( sex == 'f' && femaleCount == maxFemale )
            {
                sex = 'm';
            }
            if ( sex == 'm' )
            {
                maleCount++;
            }
            if ( sex == 'f' )
            {
                femaleCount++;
            }

            List guestHobbies = new ArrayList( hobbyList );

            int numHobbies = minHobbies
                             + rnd.nextInt( maxHobbies - minHobbies + 1 );
            for ( int j = 0; j < numHobbies; j++ )
            {
                int hobbyIndex = rnd.nextInt( guestHobbies.size( ) );
                String hobby = ( String ) guestHobbies.get( hobbyIndex );
                writer.write( "(guest (name n" + i + ") (sex " + sex
                              + ") (hobby " + hobby + "))" + LINE_SEPARATOR );
                guestHobbies.remove( hobbyIndex );
            }
        }
        writer.write( "(last_seat (seat " + numSeats + "))" + LINE_SEPARATOR );

        writer.write( LINE_SEPARATOR );
        writer.write( "(context (state start))" + LINE_SEPARATOR );

        return new ByteArrayInputStream( writer.getBuffer( ).toString( )
                                               .getBytes( ) );
    }

    private static WorkingMemory getWorkingMemory(RuleBase ruleBase) throws Exception
    {
        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( ruleBase.newWorkingMemory( ) );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream(
                                                new ByteArrayInputStream( bytes ) );
        WorkingMemory workingMemoryOut = ( WorkingMemory ) in.readObject( );
        in.close( );
        return workingMemoryOut;
    }

    private static WorkingMemory serializeWorkingMemory(WorkingMemory workingMemoryIn) throws Exception
    {
        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( workingMemoryIn );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream(
                                                new ByteArrayInputStream( bytes ) );
        WorkingMemory workingMemoryOut = ( WorkingMemory ) in.readObject( );
        in.close( );
        return workingMemoryOut;
    }
}