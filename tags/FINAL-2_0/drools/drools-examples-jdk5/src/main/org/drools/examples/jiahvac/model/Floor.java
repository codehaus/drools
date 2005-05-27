package org.drools.examples.jiahvac.model;

public interface Floor
{
    int getNumber();
    Thermometer getThermometer();
    Vent getVent();
    HeatPump getHeatPump();
}
