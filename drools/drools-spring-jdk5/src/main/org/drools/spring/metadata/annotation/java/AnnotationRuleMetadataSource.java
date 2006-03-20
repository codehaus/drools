package org.drools.spring.metadata.annotation.java;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */



import org.drools.spring.metadata.BasicRuleMetadata;
import org.drools.spring.metadata.RuleMetadata;
import org.drools.spring.metadata.RuleMetadataSource;

public class AnnotationRuleMetadataSource implements RuleMetadataSource {

    public RuleMetadata getRuleMetadata(Class pojoClass) {
        Rule ruleAnnotation = ((Class<?>)pojoClass).getAnnotation(Rule.class);
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
                    pojoClass, ruleAnnotation);
        }
    }

    private boolean isAllValuesDefaulted(Rule ruleAnnotation) {
        return ruleAnnotation.value().length() == 0
                && ruleAnnotation.value().length() == 0
                && ruleAnnotation.name().length() == 0
                && ruleAnnotation.documentation().length() == 0
                && ruleAnnotation.salience() == Integer.MIN_VALUE
                && ruleAnnotation.duration() == Long.MIN_VALUE
                && ruleAnnotation.loop() == Rule.Loop.DEFAULT
                && ruleAnnotation.xorGroup().length() == 0;
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
        if (ruleAnnotation.xorGroup().length() > 0)
        {
            metadata.setXorGroup(ruleAnnotation.xorGroup());
        }

        return metadata;
    }
}
