package org.drools.spring.factory;

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



import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.reteoo.FactHandleFactory;
import org.drools.rule.RuleSet;
import org.drools.spi.ConflictResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;

public class RuleBaseFactoryBean implements FactoryBean, BeanFactoryAware {

    private static Log log = LogFactory.getLog(RuleBaseFactoryBean.class);
    
    private ConflictResolver conflictResolver;
    private FactHandleFactory factHandleFactory;
    private boolean autoDetectRuleSets;
    private Set ruleSets;
    private BeanFactory beanFactory;
    
    private RuleBase ruleBase;

    public void setConflictResolver(ConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;
    }

    public void setFactHandleFactory(FactHandleFactory factHandleFactory) {
        this.factHandleFactory = factHandleFactory;
    }
    
    public void setAutoDetectRuleSets(boolean autoDetectRuleSets) {
        this.autoDetectRuleSets = autoDetectRuleSets;
    }
    
    public void setRuleSets(Set ruleSets) {
        this.ruleSets = ruleSets;
    }
    
    public void setBeanFactory(BeanFactory factory) throws BeansException {
        this.beanFactory = factory;
    }
    
    private RuleBase createObject() {
        RuleBaseBuilder builder = new RuleBaseBuilder();
        if (conflictResolver != null) {
            builder.setConflictResolver(conflictResolver);
        }
        if (factHandleFactory != null) {
            builder.setFactHandleFactory(factHandleFactory);
        }
        if (ruleSets == null) {
            ruleSets = new HashSet();
        }
        if (autoDetectRuleSets) {
            autoDetectRuleSets(ruleSets);
        }
        for (Iterator iter = ruleSets.iterator(); iter.hasNext();) {
            RuleSet ruleSet = (RuleSet) iter.next();
            try {
                builder.addRuleSet(ruleSet);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return builder.build();
    }

    private void autoDetectRuleSets(Set ruleSets) {
        if (!(beanFactory instanceof ListableBeanFactory)) {
            log.warn("Cannot auto-detect RuleSets, beanFactory is not instanceof ListableBeanFactory: beanFactory.class=" + beanFactory.getClass());
            return;
        }
        ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
        String[] factoryNames = factory.getBeanNamesForType(RuleSetFactoryBean.class);
        for (int i = 0; i < factoryNames.length; i++) {
            String beanName = factoryNames[i].substring(1);
            RuleSet ruleSet = (RuleSet) factory.getBean(beanName);
            ruleSets.add(ruleSet);
        }
    }

    public Class getObjectType() {
        return RuleBase.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getObject() throws Exception {
        if (ruleBase == null) {
            ruleBase = createObject();
        }
        return ruleBase;
    }
}

