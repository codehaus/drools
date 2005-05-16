package org.drools.semantics.annotation.examples.helloworld;

import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.semantics.annotation.model.AnnonatedPojoRuleBuilder;

public class HelloWorldNativeExample {

    public static void main(String[] args) throws Exception {
       
        RuleSet ruleSet = new RuleSet("native rule-set");
        AnnonatedPojoRuleBuilder annoRuleBuilder = new AnnonatedPojoRuleBuilder();
        
        ruleSet.addRule(annoRuleBuilder.buildRule(new Rule("Hello"), new HelloWorldRules.Hello()));
        ruleSet.addRule(annoRuleBuilder.buildRule(new Rule("Goodbye"), new HelloWorldRules.Goodbye()));
        ruleSet.addRule(annoRuleBuilder.buildRule(new Rule("Debug"), new HelloWorldRules.Debug()));
        
        RuleBaseBuilder builder = new RuleBaseBuilder();
        builder.addRuleSet(ruleSet);
        RuleBase ruleBase = builder.build();
        
        HelloWorldRunner.run(ruleBase);
    }
}
