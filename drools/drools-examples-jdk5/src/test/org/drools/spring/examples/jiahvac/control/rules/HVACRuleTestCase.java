package org.drools.spring.examples.jiahvac.control.rules;

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

    protected Mock<TempuratureControl> mockTempuratureControl = mocks
            .createMock(TempuratureControl.class);

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
