package org.drools.examples.manners;

/*
 * $Id: MannersBase.java,v 1.2 2004-09-17 00:37:54 mproctor Exp $
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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import org.drools.examples.manners.model.Context;
import org.drools.examples.manners.model.Guest;
import org.drools.examples.manners.model.LastSeat;
import org.drools.examples.manners.model.Seat;

/**
 * Miss Manners is a program which handles the problem of finding an acceptable
 * seating arrangement for guests at a dinner party. It will attempt to match
 * people with the same hobbies, and to seat everyone next to a member of the
 * opposite sex. Manners is a small program, which has only few rules, and
 * employs a depth-first search approach to the problem.
 */
abstract public class MannersBase
{
    /** URI of file containing rule-set (default: 'manners.java.drl'). */
    protected String ruleUri    = "manners.java.drl";

    /** Number of guests at the dinner (default: 16). */
    protected int    numGuests  = 16;

    /** Number of seats at the table (default: 16). */
    protected int    numSeats   = 16;

    /** Minimum number of hobbies each guest should have (default: 2). */
    protected int    minHobbies = 2;

    /** Maximun number of hobbies each guest should have (default: 3). */
    protected int    maxHobbies = 3;

    protected MannersBase(String[] args)
    {
        if ( args.length > 0 )
        {
            ruleUri = args[0];
        }

        if ( args.length > 1 )
        {
            numGuests = Integer.parseInt( args[1] );
            numSeats = numGuests;
        }

        if ( args.length > 2 )
        {
            numSeats = Integer.parseInt( args[2] );
        }

        if ( args.length > 3 )
        {
            minHobbies = Integer.parseInt( args[3] );
        }

        if ( args.length > 4 )
        {
            maxHobbies = Integer.parseInt( args[4] );
        }

    }

    protected void run() throws Exception
    {
        setUp( );

        List inList = getInputObjects( generateData( ) );

        System.out.println( "Using drl: " + ruleUri );

        long start = System.currentTimeMillis( );

        List outList = test( inList );

        System.out.println( "Elapsed time: "
                            + ( System.currentTimeMillis( ) - start ) + "ms" );

        int actualGuests = validateResults( inList, outList );
        if ( numGuests != actualGuests )
        {
            throw new RuntimeException( "seated guests " + actualGuests
                                        + " didn't match expected " + numGuests );
        }

        tearDown( );
    }

    /**
     * Setup the test case.
     */
    abstract protected void setUp() throws Exception;

    /**
     * Tear down the test case.
     */
    abstract protected void tearDown() throws Exception;

    /**
     * Just do it.
     */
    abstract protected List test(List inList) throws Exception;

    /**
     * Verify that each guest has at least one common hobby with the guest
     * before him/her.
     */
    protected static int validateResults(List inList, List outList)
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
    protected List getInputObjects(InputStream inputStream) throws IOException
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

    protected InputStream generateData()
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
}