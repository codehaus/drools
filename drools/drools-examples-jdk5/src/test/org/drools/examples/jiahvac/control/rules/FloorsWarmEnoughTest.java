package org.drools.examples.jiahvac.control.rules;

import org.drools.examples.jiahvac.control.rules.FloorsWarmEnough;
import org.drools.examples.jiahvac.model.HeatPump;

public class FloorsWarmEnoughTest extends HVACRuleTestCase
{
    private FloorsWarmEnough rule = new FloorsWarmEnough();

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

        boolean result = rule.isAllFloorsWarmEnough(mockPump_A.object,
                                                    mockThermometer_1.object,
                                                    mockTempuratureControl.object);
        mocks.verify();
        assertFalse(result);
    }

    public void testIsAllFloorsWarmEnoughSomeFloorsWarmEnough() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupThermometerReading(mockThermometer_2, 90.0);
        setupControlIsWarmEnough(mockTempuratureControl, 80.0, true);
        setupControlIsWarmEnough(mockTempuratureControl, 90.0, false);
        mocks.replay();

        boolean result = rule.isAllFloorsWarmEnough(mockPump_A.object,
                                                    mockThermometer_1.object,
                                                    mockTempuratureControl.object);
        mocks.verify();
        assertFalse(result);
    }

    public void testIsAllFloorsWarmEnoughAllFloorsWarmEnough() {
        setupThermometerReading(mockThermometer_1, 80.0);
        setupThermometerReading(mockThermometer_2, 90.0);
        setupControlIsWarmEnough(mockTempuratureControl, 80.0, true);
        setupControlIsWarmEnough(mockTempuratureControl, 90.0, true);
        mocks.replay();

        boolean result = rule.isAllFloorsWarmEnough(mockPump_A.object,
                                                    mockThermometer_1.object,
                                                    mockTempuratureControl.object);
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
