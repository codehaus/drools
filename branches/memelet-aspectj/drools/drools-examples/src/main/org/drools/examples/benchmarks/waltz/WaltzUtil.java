package org.drools.examples.benchmarks.waltz;

/*
 * $Id: WaltzUtil.java,v 1.1 2004-12-15 15:13:41 dbarnett Exp $
 *
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
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
 *
 */

import org.drools.examples.benchmarks.waltz.model.Junction;

/**
 *
 */
public class WaltzUtil
{
    private static final int MOD_NUM = 100;

    private WaltzUtil( )
    {
        // don't instantiate; class contains only static methods
    }

    /**
     * Points are passed in the form x1y1;
     * get_x() and get_y() are passed these points
     * and return the x and y values respectively.
     * For example, get_x(1020) returns 10.
     */
    private static int get_x( double val )
    {
        return ( ( int ) ( val / MOD_NUM ) );
    }

    /**
     * Points are passed in the form x1y1;
     * get_x() and get_y() are passed these points
     * and return the x and y values respectively.
     * For example, get_y(1020) returns 20.
     */
    private static int get_y( double val )
    {
        return ( ( int ) ( val % MOD_NUM ) );
    }

    /**
     * This function is passed two points and calculates the angle between the
     * line defined by these points and the x-axis.
     */
    private static double get_angle( int p1, int p2 )
    {
        int delta_x;
        int delta_y;

        /* Calculate (x2 - x1) and (y2 - y1).  The points are passed in the
         * form x1y1 and x2y2.  get_x() and get_y() are passed these points
         * and return the x and y values respectively.  For example,
         * get_x(1020) returns 10. */
        delta_x = get_x( p2 ) - get_x( p1 );
        delta_y = get_y( p2 ) - get_y( p1 );

        return ( Math.atan2( delta_y, delta_x ) );
    }

    /**
     * This procedure is passed the basepoint of the intersection of two lines
     * as well as the other two endpoints of the lines and calculates the
     * angle inscribed by these three points.
     */
    private static double inscribed_angle( int basepoint, int p1, int p2 )
    {
        double angle1;
        double angle2;
        double temp;

        /* Get the angle between line #1 and the origin and the angle
         * between line #2 and the origin, and then subtract these values. */
        angle1 = get_angle( basepoint, p1 );
        angle2 = get_angle( basepoint, p2 );
        temp = angle1 - angle2;
        if ( temp < 0.0 )
        {
            temp = -temp;
        }

        /* We always want the smaller of the two angles inscribed, so if the
         * answer is greater than 180 degrees, calculate the smaller angle and
         * return it. */
        if ( temp > Math.PI )
        {
            temp = ( 2 * Math.PI ) - temp;
        }
        if ( temp < 0.0 )
        {
            return( -temp );
        }
        return( temp );
    }

    /**
     *
     */
    public static Junction make_3_junction( int basepoint,
                                            int p1,
                                            int p2,
                                            int p3 )
    {
        double angle12 = inscribed_angle( basepoint, p1, p2 );
        double angle13 = inscribed_angle( basepoint, p1, p3 );
        double angle23 = inscribed_angle( basepoint, p2, p3 );

        double sum1213 = angle12 + angle13;
        double sum1223 = angle12 + angle23;
        double sum1323 = angle13 + angle23;

        double sum;
        int shaft;
        int barb1;
        int barb2;
        if ( sum1213 < sum1223 )
        {
            if ( sum1213 < sum1323 )
            {
                sum = sum1213;
                shaft = p1;
                barb1 = p2;
                barb2 = p3;
            }
            else
            {
                sum = sum1323;
                shaft = p3;
                barb1 = p1;
                barb2 = p2;
            }
        }
        else
        {
            if ( sum1223 < sum1323 )
            {
                sum = sum1223;
                shaft = p2;
                barb1 = p1;
                barb2 = p3;
            }
            else
            {
                sum = sum1323;
                shaft = p3;
                barb1 = p1;
                barb2 = p2;
            }
        }

        double delta = sum - Math.PI;
        if ( delta < 0.0 )
        {
            delta = -delta;
        }

        String type;
        if ( delta < 0.001 )
        {
            type = "tee";
        }
        else if ( sum > Math.PI )
        {
            type = "fork";
        }
        else
        {
            type = "arrow";
        }

        return new Junction( barb1, shaft, barb2, basepoint, type );
    }
}
