package org.drools.spring.examples.jiahvac.control.rules;

import static org.drools.spring.examples.jiahvac.model.HeatPump.State.*;

import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControl;
import org.drools.spring.examples.jiahvac.model.Thermometer;
import org.drools.spring.metadata.annotation.java.*;

@Rule
public class FloorTooHotPumpOff
{
    @Condition
    public boolean isPumpOff(HeatPump pump) {
        return pump.getState() == OFF;
     }

    @Condition
    public boolean isPumpServicingFloor(HeatPump pump, Thermometer thermometer) {
        return thermometer.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isTooHot(Thermometer thermometer, TempuratureControl control) {
        return control.isTooHot(thermometer.getReading());
    }

    @Consequence
    public void consequence(HeatPump pump) {
        pump.setState(COOLING);
        System.out.println("FloorTooHotPumpOff: " + pump);
    }
}
