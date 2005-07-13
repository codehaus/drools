package org.drools.spring.examples.jiahvac.control.rules;

import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.control.rules.PumpOffWhenFloorsWarmEnough;

public class PumpOffWhenFloorsWarmEnoughTest extends HVACRuleTestCase {
    
    private PumpOffWhenFloorsWarmEnough rule;

    @Override
    protected void setupBuilding() {
        super.setupBuilding();
        
        rule = new PumpOffWhenFloorsWarmEnough();
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

    public void testIsAllFloorsWarmEnoughNoFloorWarmEnough() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupControlIsWarmEnough(mockTempuratureControl, 80.0, false);
        mocks.replay();

        boolean result = rule.isAllFloorsWarmEnough(mockPump_A.object, mockThermometer_1.object);
        mocks.verify();
        assertFalse(result);
    }

    public void testIsAllFloorsWarmEnoughSomeFloorsWarmEnough() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupThermometerReading(mockThermometer_2, 90.0);
        setupControlIsWarmEnough(mockTempuratureControl, 80.0, true);
        setupControlIsWarmEnough(mockTempuratureControl, 90.0, false);
        mocks.replay();

        boolean result = rule.isAllFloorsWarmEnough(mockPump_A.object, mockThermometer_1.object);
        mocks.verify();
        assertFalse(result);
    }

    public void testIsAllFloorsWarmEnoughAllFloorsWarmEnough() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupThermometerReading(mockThermometer_2, 90.0);
        setupControlIsWarmEnough(mockTempuratureControl, 80.0, true);
        setupControlIsWarmEnough(mockTempuratureControl, 90.0, true);
        mocks.replay();

        boolean result = rule.isAllFloorsWarmEnough(mockPump_A.object, mockThermometer_1.object);
        mocks.verify();
        assertTrue(result);
    }

    public void testConsequence() {
        mockPump_A.object.setState(HeatPump.State.OFF);
        mocks.replay();

        rule.consequence(mockPump_A.object);

        mocks.verify();
    }
}
