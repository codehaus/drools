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
public class FloorsWarmEnough
{
    @Condition
    public boolean isPumpHeating(@Parameter("pump") HeatPump pump) {
        return pump.getState() == HEATING;
     }

    @Condition
    public boolean isPumpServicingFloor(@Parameter("pump") HeatPump pump,
                                        @Parameter("thermometer") Thermometer thermometer) {
        return thermometer.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isAllFloorsWarmEnough(@Parameter("pump") HeatPump pump,
                                         @Parameter("thermometer") Thermometer thermometer,
                                         @Parameter("control") TempuratureControl control) {
        if (!control.isWarmEnough(thermometer.getReading())) {
            return false;
        }
        for (Floor floor : pump.getFloors()) {
            if (floor == thermometer.getFloor()) {
                continue;
            }
            if (!control.isWarmEnough(floor.getThermometer().getReading())) {
                return false;
            }
        }
        return true;
     }

    @Consequence
    public void consequence(@Parameter("pump") HeatPump pump) {
        pump.setState(OFF);
        System.out.println("FloorsWarmEnough: " + pump);
    }
}
