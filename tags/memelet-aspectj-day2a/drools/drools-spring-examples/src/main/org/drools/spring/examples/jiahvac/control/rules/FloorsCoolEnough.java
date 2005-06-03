package org.drools.spring.examples.jiahvac.control.rules;

import static org.drools.spring.examples.jiahvac.model.HeatPump.State.*;

import org.drools.spring.examples.jiahvac.model.Floor;
import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.TempuratureControl;
import org.drools.spring.examples.jiahvac.model.Thermometer;
import org.drools.spring.metadata.annotation.java.*;

@Rule
public class FloorsCoolEnough
{
    @Condition
    public boolean isPumpCooling(HeatPump pump) {
        return pump.getState() == COOLING;
    }

    @Condition
    public boolean isPumpServicingFloor(HeatPump pump, Thermometer thermometer) {
        return thermometer.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isAllFloorsCoolEnough(HeatPump pump, Thermometer thermometer,
                                         TempuratureControl control) {
        if (!control.isCoolEnough(thermometer.getReading())) {
            return false;
        }
        for (Floor floor : pump.getFloors()) {
            if (floor == thermometer.getFloor()) {
                continue;
            }
            if (!control.isCoolEnough(floor.getThermometer().getReading())) {
                return false;
            }
        }
        return true;
     }

    @Consequence
    public void consequence(HeatPump pump) {
        pump.setState(OFF);
        System.out.println("FloorsCoolEnough: " + pump);
    }
}
