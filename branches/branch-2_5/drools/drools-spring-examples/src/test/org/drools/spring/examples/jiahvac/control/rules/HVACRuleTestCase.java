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



import junit.framework.TestCase;

import org.drools.spring.examples.jiahvac.model.Floor;
import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControl;
import org.drools.spring.examples.jiahvac.model.Thermometer;
import org.drools.spring.examples.jiahvac.model.Vent;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public abstract class HVACRuleTestCase extends TestCase {

    protected EasymockContainer mocks = new EasymockContainer();

    protected Mock<TempuratureControl> mockTempuratureControl = mocks.createMock(TempuratureControl.class);

    protected Mock<Floor> mockFloor_1 = mocks.createMock(Floor.class);
    protected Mock<Floor> mockFloor_2 = mocks.createMock(Floor.class);
    protected Mock<Thermometer> mockThermometer_1 = mocks.createMock(Thermometer.class);
    protected Mock<Thermometer> mockThermometer_2 = mocks.createMock(Thermometer.class);
    protected Mock<Vent> mockVent_1 = mocks.createMock(Vent.class);
    protected Mock<Vent> mockVent_2 = mocks.createMock(Vent.class);
    protected Mock<HeatPump> mockPump_A = mocks.createMock(HeatPump.class);

    protected Mock<Floor> mockFloor_3 = mocks.createMock(Floor.class);
    protected Mock<Thermometer> mockThermometer_3 = mocks.createMock(Thermometer.class);
    protected Mock<Vent> mockVent_3 = mocks.createMock(Vent.class);
    protected Mock<HeatPump> mockPump_B = mocks.createMock(HeatPump.class);

    @Override
    protected void setUp() throws Exception {
        setupBuilding();
    }

    protected void setupBuilding() {
        setupFloor(mockFloor_1, mockThermometer_1, mockVent_1);
        setupFloor(mockFloor_2, mockThermometer_2, mockVent_2);
        setupPump(mockPump_A, mockFloor_1, mockFloor_2);

        setupFloor(mockFloor_3, mockThermometer_3, mockVent_3);
        setupPump(mockPump_B, mockFloor_3);
    }

    protected void setupFloor(Mock<Floor> mockFloor, Mock<Thermometer> mockThermometer,
            Mock<Vent> mockVent) {
        mockFloor.control.expectAndReturn(mockFloor.object.getThermometer(),
                mockThermometer.object, MockControl.ZERO_OR_MORE);
        mockThermometer.control.expectAndReturn(mockThermometer.object.getFloor(),
                mockFloor.object, MockControl.ZERO_OR_MORE);
        mockFloor.control.expectAndReturn(mockFloor.object.getVent(), mockVent.object,
                MockControl.ZERO_OR_MORE);
        mockVent.control.expectAndReturn(mockVent.object.getFloor(), mockFloor.object,
                MockControl.ZERO_OR_MORE);
    }

    protected void setupPump(Mock<HeatPump> mockPump, Mock<Floor>... mockFloors) {
        Floor[] floors = new Floor[mockFloors.length];
        for (int i = 0; i < mockFloors.length; i++) {
            floors[i] = mockFloors[i].object;
            mockFloors[i].control.expectAndReturn(mockFloors[i].object.getHeatPump(),
                    mockPump.object, MockControl.ZERO_OR_MORE);
        }
        mockPump.control.expectAndReturn(mockPump.object.getFloors(), floors,
                MockControl.ZERO_OR_MORE);
    }

    protected void setupPumpState(Mock<HeatPump> mockPump, HeatPump.State state) {
        mockPump.control.expectAndReturn(mockPump.object.getState(), state);
    }

    protected void setupVentState(Mock<Vent> mockVent, Vent.State state) {
        mockVent.control.expectAndReturn(mockVent.object.getState(), state);
    }

    protected void setupThermometerReading(Mock<Thermometer> mockThermometer, double reading) {
        mockThermometer.control.expectAndReturn(mockThermometer.object.getReading(), reading);
    }

    protected void setupControlIsCoolEnough(Mock<TempuratureControl> mockTempuratureControl,
            double expectedTempurature, boolean result) {
        mockTempuratureControl.control.expectAndReturn(mockTempuratureControl.object
                .isCoolEnough(expectedTempurature), result);
    }

    protected void setupControlIsTooCold(Mock<TempuratureControl> mockTempuratureControl,
            double expectedTempurature, boolean result) {
        mockTempuratureControl.control.expectAndReturn(mockTempuratureControl.object
                .isTooCold(expectedTempurature), result);
    }

    protected void setupControlIsWarmEnough(Mock<TempuratureControl> mockTempuratureControl,
            double expectedTempurature, boolean result) {
        mockTempuratureControl.control.expectAndReturn(mockTempuratureControl.object
                .isWarmEnough(expectedTempurature), result);
    }

    protected void setupControlIsTooHot(Mock<TempuratureControl> mockTempuratureControl,
            double expectedTempurature, boolean result) {
        mockTempuratureControl.control.expectAndReturn(mockTempuratureControl.object
                .isTooHot(expectedTempurature), result);
    }

}

