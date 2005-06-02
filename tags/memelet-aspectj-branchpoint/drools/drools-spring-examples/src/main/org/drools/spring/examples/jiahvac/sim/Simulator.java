package org.drools.spring.examples.jiahvac.sim;

import org.drools.spring.examples.jiahvac.model.Floor;
import org.drools.spring.examples.jiahvac.model.HeatPump;
import org.drools.spring.examples.jiahvac.model.Vent;

public class Simulator implements Runnable {

    private final double RATE = 0.01;
    private final double HOT = 100;
    private final double COLD = 50;
    
    private SimFloor[] floors;
    private SimHeatPump[] heatPumps;
    private double outdoorTempurature = 90;

    public Simulator(int numOfFloors, double initialTempuratureReading, Vent.State initialVentState) {
        if (numOfFloors % 3 != 0) {
            throw new RuntimeException("numOfFloors must be divisible by 3");
        }
        buildFloors(numOfFloors, initialTempuratureReading, initialVentState);
        buildHeatPumps();
        new Thread(this).start();
    }
    
    private void buildFloors(int numOfFloors, double initialTempuratureReading, Vent.State initialVentState) {
        floors = new SimFloor[numOfFloors];
        for (int i = 0; i < numOfFloors; i++) {
            floors[i] = new SimFloor(i);
            
            SimThermometer thermometer = new SimThermometer(floors[i]);
            thermometer.setReading(initialTempuratureReading);
            floors[i].setThermometer(thermometer);
            
            SimVent vent = new SimVent(floors[i]);
            vent.setState(initialVentState);
            floors[i].setVent(vent);
        }
    }
    
    private void buildHeatPumps() {
        int numHeatPumps = floors.length / 3;
        heatPumps = new SimHeatPump[numHeatPumps];
        
        int floorNumber = 0;
        for (int i = 0; i < numHeatPumps; i++) {
            SimFloor[] servicedFloors = new SimFloor[] {
                    floors[floorNumber++],
                    floors[floorNumber++],
                    floors[floorNumber++]
            };
            heatPumps[i] = new SimHeatPump(i, servicedFloors);
            
            servicedFloors[0].setHeatPump(heatPumps[i]);
            servicedFloors[1].setHeatPump(heatPumps[i]);
            servicedFloors[2].setHeatPump(heatPumps[i]);
        }
    }
    
    public void run() {
        for (int i = 0; i < floors.length; i++) {
            SimThermometer thermometer = floors[i].getThermometer();
            Vent vent = floors[i].getVent();
            HeatPump pump = floors[i].getHeatPump();
            
            double temperature = thermometer.getReading();
            temperature += leakage( temperature );            
            
            switch (pump.getState()) {
            case HEATING:
                temperature += heatingTempuratureDelta( vent, temperature ); 
                break;
            case COOLING:
                temperature += coolingTempuratureDelta( vent, temperature ); 
                break;
            case OFF:
                temperature += offTempuratureDelta( i );
                break;
            }
            
            thermometer.setReading(temperature);
        }
    }

    private double coolingTempuratureDelta(Vent vent, double temperature) {
        return (COLD - temperature) * RATE * vent.getState().getValue();
    }

    private double heatingTempuratureDelta(Vent vent, double temperature) {
        return (HOT - temperature) * RATE * vent.getState().getValue();
    }

    private double offTempuratureDelta(int i) {
        return (i + 1 ) * 0.005;
    }

    private double leakage(double temperature) {
        return (outdoorTempurature - temperature) * RATE / 2;
    }

    public Floor[] getFloors() {
        return floors;
    }
    
    public HeatPump[] getHeatPumps() {
        return heatPumps;
    }
    
    public void setOutdoorTempurature(double tempurature) {
        this.outdoorTempurature = tempurature;
    }
    
    public double getOutdoorTempurature() {
        return outdoorTempurature;
    }
}
