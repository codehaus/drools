package org.drools.examples.jiahvac.control.rules;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.Vent;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import org.drools.semantics.annotation.DroolsRule;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.DroolsConsequence;

@DroolsRule
public class HeatingVentOpenFloorWarmEnough
{
    @DroolsCondition
    public boolean isPumpHeating(@DroolsParameter("pump") HeatPump pump) {
        return pump.getState() == HeatPump.State.HEATING;
     }

    @DroolsCondition
    public boolean isVentOpen(@DroolsParameter("vent") Vent vent) {
        return vent.getState() == Vent.State.OPEN;
     }

    @DroolsCondition
    public boolean isSameFloor(@DroolsParameter("vent") Vent vent,
                               @DroolsParameter("thermometer") Thermometer thermometer,
                               @DroolsParameter("pump") HeatPump pump) {
        return vent.getFloor() == thermometer.getFloor()
                && vent.getFloor().getHeatPump() == pump;
    }

    @DroolsCondition
    public boolean isWarmEnough(@DroolsParameter("thermometer") Thermometer thermometer,
                                @DroolsParameter("control") TempuratureControl control) {
        return control.isWarmEnough(thermometer.getReading());
    }

    @DroolsConsequence
    public void consequence(@DroolsParameter("vent") Vent vent) {
        vent.setState(Vent.State.CLOSED);
        System.out.println("HeatingVentOpenFloorWarmEnough: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}
