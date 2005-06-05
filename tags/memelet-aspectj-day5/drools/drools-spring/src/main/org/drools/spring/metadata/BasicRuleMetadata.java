package org.drools.spring.metadata;

public class BasicRuleMetadata implements RuleMetadata {

    private String name;
    private String documentation;
    private Integer salience;
    private Long duration;
    private Boolean noLoop;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public Integer getSalience() {
        return salience;
    }

    public void setSalience(Integer salience) {
        this.salience = salience;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Boolean getNoLoop() {
        return noLoop;
    }

    public void setNoLoop(Boolean noLoop) {
        this.noLoop = noLoop;
    }
}