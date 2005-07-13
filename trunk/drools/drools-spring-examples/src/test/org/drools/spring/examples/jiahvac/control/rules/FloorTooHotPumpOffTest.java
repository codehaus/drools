package org.drools.spring.examples.jiahvac.control.rules;

import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.control.rules.FloorTooHotPumpOff;

public class FloorTooHotPumpOffTest extends HVACRuleTestCase
{
    private FloorTooHotPumpOff rule;

    @Override
    protected void setupBuilding() {
        super.setupBuilding();
        
        rule = new FloorTooHotPumpOff();
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

    public void testIsTooHotFalse() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupControlIsTooHot(mockTempuratureControl, 80.0, false);
        mocks.replay();

        boolean result = rule.isTooHot( mockThermometer_1.object);
        mocks.verify();
        assertFalse(result);
    }

    public void testIsTooHotTrue() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupControlIsTooHot(mockTempuratureControl, 80.0, true);
        mocks.replay();

        boolean result = rule.isTooHot( mockThermometer_1.object);
        mocks.verify();
        assertTrue(result);
    }

    public void testConsequence() {
        mockPump_A.object.setState(HeatPump.State.COOLING);
        mocks.replay();

        rule.consequence(mockPump_A.object);

        mocks.verify();
    }
}
