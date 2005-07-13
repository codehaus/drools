package org.drools.spring.examples.jiahvac.control.rules;

import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.Vent;

public class HeatingVentClosedFloorTooColdTest extends HVACRuleTestCase
{
    private HeatingVentClosedFloorTooCold rule;

    @Override
    protected void setupBuilding() {
        super.setupBuilding();
        
        rule = new HeatingVentClosedFloorTooCold();
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
