package org.drools.spring.metadata.annotation.java;

import org.drools.spring.metadata.BasicRuleMetadata;
import org.drools.spring.metadata.RuleMetadata;
import org.drools.spring.metadata.RuleMetadataSource;

public class AnnotationRuleMetadataSource implements RuleMetadataSource {

    public RuleMetadata getRuleMetadata(Class pojoClass) {
        Class<?> pojoClassJdk5 = pojoClass;
        Rule ruleAnnotation = pojoClassJdk5.getAnnotation(Rule.class);
        if (ruleAnnotation == null) {
            return null;
        }
        if (isAllValuesDefaulted(ruleAnnotation)) {
            return null;
        }
        if (ruleAnnotation.value().length() > 0) {
            return createRuleMetadataFromDefaultValue(ruleAnnotation);
        } else {
            return createRuleMetadataFromNamedValues(
                pojoClassJdk5, ruleAnnotation);
        }
    }

    private boolean isAllValuesDefaulted(Rule ruleAnnotation) {
        return ruleAnnotation.value().length() == 0
                && ruleAnnotation.value().length() == 0
                && ruleAnnotation.name().length() == 0
                && ruleAnnotation.documentation().length() == 0
                && ruleAnnotation.salience() == Integer.MIN_VALUE
                && ruleAnnotation.duration() == Long.MIN_VALUE
                && ruleAnnotation.loop() == Rule.Loop.DEFAULT;
    }

    private RuleMetadata createRuleMetadataFromDefaultValue(Rule ruleAnnotation) {
        BasicRuleMetadata metadata = new BasicRuleMetadata();
        metadata.setName(ruleAnnotation.value());
        return metadata;
    }

    /*
     * This code is ugly because JSR-175 does not allow null values, thus we are
     * forced to use sentinal values.
     */
    private RuleMetadata createRuleMetadataFromNamedValues(Class pojoClass, Rule ruleAnnotation) {
        BasicRuleMetadata metadata = new BasicRuleMetadata();

        if (ruleAnnotation.name().length() > 0) {
            metadata.setName(ruleAnnotation.name());
        } else {
            metadata.setName(pojoClass.getName());
        }
        if (ruleAnnotation.documentation().length() > 0) {
            metadata.setDocumentation(ruleAnnotation.documentation());
        }
        if (ruleAnnotation.salience() != Integer.MIN_VALUE) {
            metadata.setSalience(ruleAnnotation.salience());
        }
        if (ruleAnnotation.duration() != Long.MIN_VALUE) {
            metadata.setDuration(ruleAnnotation.duration());
        }
        if (ruleAnnotation.loop() != Rule.Loop.DEFAULT) {
            metadata.setNoLoop(ruleAnnotation.loop().getValue());
        }

        return metadata;
    }
}
