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
import org.drools.spring.examples.jiahvac.control.rules.PumpHeatingWhenFloorTooCold;

public class PumpHeatingWhenFloorTooColdTest extends HVACRuleTestCase
{
    private PumpHeatingWhenFloorTooCold rule;

    @Override
    protected void setupBuilding() {
        super.setupBuilding();
        
        rule = new PumpHeatingWhenFloorTooCold();
        rule.setControl(mockTempuratureControl.object);
    }
    
    /*
     * Really, this method cannot fail. This test serves only as documentation of intent.
     */
    public void testIsPumpOff() {
        for (HeatPump.State state : HeatPump.State.values()) {
            mocks.reset();
            setupPumpState(mockPump_A, state );
            mocks.replay();

            boolean result = rule.isPumpOff(mockPump_A.object);

            mocks.verify();
            assertTrue((state == HeatPump.State.OFF) ? result : !result );
        }
    }

    public void testIsPumpServicingFloorFalse() {
        mocks.replay();

        boolean result = rule.isPumpServicingFloor(mockPump_B.object, mockThermometer_1.object);

        mocks.verify();
        assertFalse(result);
    }

    public void testIsPumpServicingFloorTrue() {
        mocks.replay();

        boolean result = rule.isPumpServicingFloor(mockPump_A.object, mockThermometer_1.object);

        mocks.verify();
        assertTrue(result);
    }

    public void testIsTooColdFalse() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupControlIsTooCold(mockTempuratureControl, 80.0, false);
        mocks.replay();

        boolean result = rule.isTooCold( mockThermometer_1.object);
        mocks.verify();
        assertFalse(result);
    }

    public void testIsTooColdTrue() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupControlIsTooCold(mockTempuratureControl, 80.0, true);
        mocks.replay();

        boolean result = rule.isTooCold( mockThermometer_1.object);
        mocks.verify();
        assertTrue(result);
    }

    public void testConsequence() {
        mockPump_A.object.setState(HeatPump.State.HEATING);
        mocks.replay();

        rule.consequence(mockPump_A.object);

        mocks.verify();
    }
}

