package org.drools.spring.examples.helloworld;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.rule.RuleSet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloWorldSpringOuterPojosExample {

    public static void main(String[] args) throws Exception {
        Logger.getLogger("org.springframework").setLevel(Level.WARNING);
        ApplicationContext context = new ClassPathXmlApplicationContext("org/drools/spring/examples/helloworld/helloworld.appctx.xml");

        RuleSet ruleSet = (RuleSet) context.getBean("outerPojosRuleSet");
        RuleBaseBuilder builder = new RuleBaseBuilder();
        builder.addRuleSet(ruleSet);
        RuleBase ruleBase = builder.build();

        HelloWorldRunner.run(ruleBase);
    }
}
