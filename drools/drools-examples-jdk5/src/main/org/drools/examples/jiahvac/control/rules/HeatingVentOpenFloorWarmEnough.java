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
public class HeatingVentOpenFloorWarmEnough
{
    @Condition
    public boolean isPumpHeating(@Parameter("pump") HeatPump pump) {
        return pump.getState() == HeatPump.State.HEATING;
     }

    @Condition
    public boolean isVentOpen(@Parameter("vent") Vent vent) {
        return vent.getState() == Vent.State.OPEN;
     }

    @Condition
    public boolean isSameFloor(@Parameter("vent") Vent vent,
                               @Parameter("thermometer") Thermometer thermometer,
                               @Parameter("pump") HeatPump pump) {
        return vent.getFloor() == thermometer.getFloor()
                && vent.getFloor().getHeatPump() == pump;
    }

    @Condition
    public boolean isWarmEnough(@Parameter("thermometer") Thermometer thermometer,
                                @Parameter("control") TempuratureControl control) {
        return control.isWarmEnough(thermometer.getReading());
    }

    @Consequence
    public void consequence(@Parameter("vent") Vent vent) {
        vent.setState(Vent.State.CLOSED);
        System.out.println("HeatingVentOpenFloorWarmEnough: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}
