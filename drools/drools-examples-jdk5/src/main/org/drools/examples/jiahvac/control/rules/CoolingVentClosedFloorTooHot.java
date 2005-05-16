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
public class CoolingVentClosedFloorTooHot
{
    @DroolsCondition
    public boolean isPumpCooling(@DroolsParameter("pump") HeatPump pump) {
        return pump.getState() == HeatPump.State.COOLING;
     }

    @DroolsCondition
    public boolean isVentClosed(@DroolsParameter("vent") Vent vent) {
        return vent.getState() == Vent.State.CLOSED;
     }

    @DroolsCondition
    public boolean isSameFloor(@DroolsParameter("vent") Vent vent,
                               @DroolsParameter("thermometer") Thermometer thermometer,
                               @DroolsParameter("pump") HeatPump pump) {
        return vent.getFloor() == thermometer.getFloor()
                && vent.getFloor().getHeatPump() == pump;
    }

    @DroolsCondition
    public boolean isNotCoolEnough(@DroolsParameter("thermometer") Thermometer thermometer,
                                   @DroolsParameter("control") TempuratureControl control) {
        return !control.isCoolEnough(thermometer.getReading());
    }

    @DroolsConsequence
    public void consequence(@DroolsParameter("vent") Vent vent) {
        vent.setState(Vent.State.OPEN);
        System.out.println("CoolingVentClosedFloorTooHot: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}
