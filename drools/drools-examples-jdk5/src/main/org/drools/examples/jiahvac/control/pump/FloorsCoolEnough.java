package org.drools.examples.jiahvac.control.pump;

import org.drools.examples.jiahvac.model.Floor;
import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import static org.drools.examples.jiahvac.model.HeatPump.State.*;
import org.drools.semantics.annotation.Drools;

@Drools.Rule
public class FloorsCoolEnough
{
    @Drools.Condition
    public boolean isPumpCooling(@Drools.Parameter("pump") HeatPump pump) {
        return pump.getState() == COOLING;
     }
    
    @Drools.Condition
    public boolean isAllFloorsCoolEnough(@Drools.Parameter("pump") HeatPump pump,
                                         @Drools.Parameter("thermometer") Thermometer thermometer,
                                         @Drools.Parameter("control") TempuratureControl control) {
        if (thermometer.getFloor().getHeatPump() != pump) {
            return false;
        }
        boolean result = true;
        for (Floor floor : pump.getFloors()) {
            double temp = floor.getThermometer().getReading();
            result &= control.isCoolEnough(temp);
        }
        return result;
     }
    
    @Drools.Consequence
    public void consequence(@Drools.Parameter("pump") HeatPump pump) {
        pump.setState(OFF);
        System.out.println("FloorsCoolEnough: " + pump);
    }
}
