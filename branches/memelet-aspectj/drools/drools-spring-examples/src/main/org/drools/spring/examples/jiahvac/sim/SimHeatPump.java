package org.drools.spring.examples.jiahvac.sim;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.drools.spring.examples.jiahvac.model.Floor;
import org.drools.spring.examples.jiahvac.model.HeatPump;

public class SimHeatPump implements HeatPump {

    private final int id;
    private final Floor[] floors;
    private State state = State.OFF;
    private State prevState = State.OFF;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public SimHeatPump(int id, Floor[] floors) {
        this.id = id;
        this.floors = floors;
    }

    public int getId() {
        return id;
    }
    
    public Floor[] getFloors() {
        return floors; 
    }
    
    public boolean isServicing(Floor floor) {
        for (Floor servicedFloor : floors) {
            if (floor.equals(servicedFloor)) {
                return true;
            }
        }
        return false;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        pcs.firePropertyChange("state", prevState, state);
        prevState = state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
    @Override
    public String toString() {
        return "pump("+id+")=" + state;
    }
}
