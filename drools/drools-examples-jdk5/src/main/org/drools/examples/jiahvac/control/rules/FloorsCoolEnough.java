package org.drools.examples.jiahvac.control.rules;

import org.drools.examples.jiahvac.model.Floor;
import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import static org.drools.examples.jiahvac.model.HeatPump.State.*;
import org.drools.semantics.annotation.Rule;
import org.drools.semantics.annotation.Parameter;
import org.drools.semantics.annotation.Condition;
import org.drools.semantics.annotation.Consequence;

@Rule
public class FloorsCoolEnough
{
    @Condition
    public boolean isPumpCooling(@Parameter HeatPump pump) {
        return pump.getState() == COOLING;
    }

    @Condition
    public boolean isPumpServicingFloor(@Parameter HeatPump pump,
                                        @Parameter Thermometer thermometer) {
        return thermometer.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isAllFloorsCoolEnough(@Parameter HeatPump pump,
                                         @Parameter Thermometer thermometer,
                                         @Parameter TempuratureControl control) {
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
    public void consequence(@Parameter HeatPump pump) {
        pump.setState(OFF);
        System.out.println("FloorsCoolEnough: " + pump);
    }
}
