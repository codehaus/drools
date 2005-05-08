package org.drools.spring.examples.jiahvac.model;

public interface TempuratureControl
{
    double getSetPoint();
    
    boolean isTooCold(double temperature);
    boolean isTooHot(double temperature);
    
    boolean isCoolEnough(double tempurature);
    boolean isWarmEnough(double tempurature);    
}
