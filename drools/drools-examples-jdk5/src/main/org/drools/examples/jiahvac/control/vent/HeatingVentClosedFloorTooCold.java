package org.drools.examples.jiahvac.control.vent;

import org.drools.examples.jiahvac.model.HeatPump;
import org.drools.examples.jiahvac.model.Vent;
import org.drools.examples.jiahvac.model.TempuratureControl;
import org.drools.examples.jiahvac.model.Thermometer;
import org.drools.semantics.annotation.DroolsRule;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.DroolsConsequence;

@DroolsRule
public class HeatingVentClosedFloorTooCold
{
    @DroolsCondition
    public boolean condition(@DroolsParameter("vent") Vent vent,
                             @DroolsParameter("thermometer") Thermometer thermometer,
                             @DroolsParameter("pump") HeatPump pump,
                             @DroolsParameter("control") TempuratureControl control) {
        return isPumpHeating(pump) 
                && isVentClosed(vent)
                && isNotWarmEnough(thermometer, control)
                && isSameFloor(vent, thermometer, pump);
    }
    
    //@Drools.Condition
    public boolean isPumpHeating(@DroolsParameter("pump") HeatPump pump) {
        return pump.getState() == HeatPump.State.HEATING;
     }
    
    //@Drools.Condition
    public boolean isVentClosed(@DroolsParameter("vent") Vent vent) {
        return vent.getState() == Vent.State.CLOSED;
     }
    
    //@Drools.Condition
    public boolean isNotWarmEnough(@DroolsParameter("thermometer") Thermometer thermometer,
                                   @DroolsParameter("control") TempuratureControl control) {
        return !control.isWarmEnough(thermometer.getReading());
    }
    
    //@Drools.Condition
    public boolean isSameFloor(@DroolsParameter("vent") Vent vent,
                               @DroolsParameter("thermometer") Thermometer thermometer,
                               @DroolsParameter("pump") HeatPump pump) {
        return vent.getFloor() == thermometer.getFloor()
                && vent.getFloor().getHeatPump() == pump;
    }
    
    @DroolsConsequence
    public void consequence(@DroolsParameter("vent") Vent vent) {
        vent.setState(Vent.State.OPEN);
        System.out.println("HeatingVentClosedFloorTooCold: " + vent
                           + ", " + vent.getFloor().getThermometer());
    }
}
