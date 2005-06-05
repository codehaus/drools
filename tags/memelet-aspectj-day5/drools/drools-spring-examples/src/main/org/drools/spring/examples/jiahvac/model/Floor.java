package org.drools.spring.examples.jiahvac.model;

public interface Floor
{
    int getNumber();
    Thermometer getThermometer();
    Vent getVent();
    HeatPump getHeatPump();
}
