package org.drools.examples.jiahvac.sim;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.drools.examples.jiahvac.model.Vent;

public class SimVent implements Vent {

    private SimFloor floor;
    private State state = State.CLOSED;
    private State prevState = State.CLOSED;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public SimVent(SimFloor floor) {
        this.floor = floor;
    }

    public SimFloor getFloor() {
        return floor;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.prevState = this.state;
        this.state = state;
        pcs.firePropertyChange("state", prevState, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public String toString() {
        return "vent("+floor.getNumber()+")=" + state;
    }
}


