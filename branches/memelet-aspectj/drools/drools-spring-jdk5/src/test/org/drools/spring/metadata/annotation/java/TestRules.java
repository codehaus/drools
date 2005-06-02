package org.drools.spring.metadata.annotation.java;

import org.drools.spi.KnowledgeHelper;
import org.drools.spring.metadata.annotation.java.Rule.Loop;

public class TestRules {

    @Rule
    public static class PojoRule_A {

        public boolean condition(@Fact String name, @Fact int age) {
            return false;
        }

        public void consequence(@Fact String name, @Fact int age, KnowledgeHelper kh) {
        }
    }

    @Rule(documentation="my B docs", salience=10)
    public static class PojoRule_B {

        public boolean condition(String name, int age) {
            return false;
        }

        public void consequence(String name, int age, KnowledgeHelper kh) {
        }
    }

    @Rule(name="myC", documentation="my C docs", loop=Loop.ALLOW)
    public static class PojoRule_C {

        @SuppressWarnings("all") // unused
        @Condition
        private boolean condition(@Fact String name, @Data("myTime") long start) {
            return false;
        }

        @Consequence
        public String consequence(@Fact String name, @Fact int age, KnowledgeHelper kh) {
            return "some value";
        }
    }

    public static class PojoRule_D {

        public boolean condition_1(@Fact("left") String left, @Fact("right") String right) {
            return false;
        }
        public boolean condition_2(@Fact("left") String left, @Fact("right") String right) {
            return false;
        }

        public void consequence_1(@Fact("left") String left, @Fact("right") String right) {
        }

        public void consequence_2(@Fact("left") String left, @Fact("right") String right) {
        }
    }

}
