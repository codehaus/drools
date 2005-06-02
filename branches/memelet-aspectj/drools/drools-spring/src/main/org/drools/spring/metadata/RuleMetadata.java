package org.drools.spring.metadata;

public interface RuleMetadata {

    String getName();
    String getDocumentation();
    Integer getSalience();
    Long getDuration();
    Boolean getNoLoop();

}