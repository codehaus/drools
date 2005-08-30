package org.drools.spring.examples.jiahvac.control.rules;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
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



import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.Vent;

public class OpenVentWhenHeatingVentClosedFloorTooColdTest extends HVACRuleTestCase
{
    private OpenVentWhenHeatingVentClosedFloorTooCold rule;

    @Override
    protected void setupBuilding() {
        super.setupBuilding();
        
        rule = new OpenVentWhenHeatingVentClosedFloorTooCold();
        rule.setControl(mockTempuratureControl.object);
    }
    
    /*
     * Really, this method cannot fail. This test serves only as documentation of intent.
     */
    public void testIsPumpHeating() {
        for (HeatPump.State state : HeatPump.State.values()) {
            mocks.reset();
            setupPumpState(mockPump_A, state );
            mocks.replay();

            boolean result = rule.isPumpHeating(mockPump_A.object);

            mocks.verify();
            assertTrue((state == HeatPump.State.HEATING) ? result : !result );
        }
    }

    /*
     * Really, this method cannot fail. This test serves only as documentation of intent.
     */
    public void testIsVentClosed() {
        for (Vent.State state : Vent.State.values()) {
            mocks.reset();
            setupVentState(mockVent_1, state );
            mocks.replay();

            boolean result = rule.isVentClosed(mockVent_1.object);

            mocks.verify();
            assertTrue((state == Vent.State.CLOSED) ? result : !result );
        }
    }

    public void testIsSameFloorAllDifferentFloors() {
        mocks.replay();

        boolean result = rule.isSameFloor(mockVent_1.object, mockThermometer_2.object, mockPump_B.object);

        mocks.verify();
        assertFalse(result);
    }

    public void testIsSameFloorPumpDifferentFloor() {
        mocks.replay();

        boolean result = rule.isSameFloor(mockVent_1.object, mockThermometer_1.object, mockPump_B.object);

        mocks.verify();
        assertFalse(result);
    }

    public void testIsSameFloorThermometerDifferentFloor() {
        mocks.replay();

        boolean result = rule.isSameFloor(mockVent_1.object, mockThermometer_2.object, mockPump_A.object);

        mocks.verify();
        assertFalse(result);
    }

    public void testIsSameFloorAllSameFloor() {
        mocks.replay();

        boolean result = rule.isSameFloor(mockVent_1.object, mockThermometer_1.object, mockPump_A.object);

        mocks.verify();
        assertTrue(result);
    }

    public void testIsNotWarmEnoughFalse() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupControlIsWarmEnough(mockTempuratureControl, 80.0, true);
        mocks.replay();

        boolean result = rule.isNotWarmEnough(mockThermometer_1.object);

        mocks.verify();
        assertFalse(result);

    }

    public void testIsNotWarmEnoughTrue() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupControlIsWarmEnough(mockTempuratureControl, 80.0, false);
        mocks.replay();

        boolean result = rule.isNotWarmEnough(mockThermometer_1.object);

        mocks.verify();
        assertTrue(result);

    }

    public void testConsequence() {
        mockVent_1.object.setState(Vent.State.OPEN);
        mocks.replay();

        rule.consequence(mockVent_1.object);

        mocks.verify();
    }
}

