package org.drools.examples.jiahvac.control.rules;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.Vent;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import org.drools.semantics.annotation.Rule;
import org.drools.semantics.annotation.Parameter;
import org.drools.semantics.annotation.Condition;
import org.drools.semantics.annotation.Consequence;

@Rule
public class CoolingVentOpenFloorCoolEnough
{
    @Condition
    public boolean isPumpCooling(@Parameter HeatPump pump) {
        return pump.getState() == HeatPump.State.COOLING;
     }

    @Condition
    public boolean isVentOpen(@Parameter Vent vent) {
        return vent.getState() == Vent.State.OPEN;
     }

    @Condition
    public boolean isSameFloor(@Parameter Vent vent,
                               @Parameter Thermometer thermometer,
                               @Parameter HeatPump pump) {
        return vent.getFloor() == thermometer.getFloor()
                && vent.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isCoolEnough(@Parameter Thermometer thermometer,
                                @Parameter TempuratureControl control) {
        return control.isCoolEnough(thermometer.getReading());
    }

    @Consequence
    public void consequence(@Parameter Vent vent) {
        vent.setState(Vent.State.CLOSED);
        System.out.println("CoolingVentOpenFloorCoolEnough: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}
