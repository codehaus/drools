package org.drools.spring.examples.jiahvac.sim;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.drools.spring.examples.jiahvac.model.Thermometer;

public class SimThermometer implements Thermometer {

    private final SimFloor floor;
    private double reading;
    private double prevReading;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public SimThermometer(SimFloor floor, double reading) {
        this.floor = floor;
        this.reading = reading;
        this.prevReading = reading-1;
    }

    public SimThermometer(SimFloor floor) {
        this(floor, 0);
    }

    public SimFloor getFloor() {
        return floor;
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        prevReading = this.reading;
        this.reading = reading;
        pcs.firePropertyChange("reading", new Double(prevReading), new Double(reading));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "t("+floor.getNumber()+")=" + reading;
    }
}


