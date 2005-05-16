package org.drools.examples.jiahvac.model;

public interface HeatPump
{
    int getId();
    
    Floor[] getFloors();
    boolean isServicing(Floor floor);
    
    enum State { 
        COOLING("cooling"), 
        OFF("off"), 
        HEATING("heating");
        
        private final String name;
        
        State(String name) { 
            this.name = name;
        }
        
        public String toString() {
            return name;
        }
    };
    
    State getState();
    void setState(State state);
}