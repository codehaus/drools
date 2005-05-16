package org.drools.examples.jiahvac.model;

import java.beans.PropertyChangeListener;

public interface Thermometer
{
    Floor getFloor();
    double getReading();
    
    void addPropertyChangeListener(PropertyChangeListener listener);
    void removePropertyChangeListener(PropertyChangeListener listener);
}