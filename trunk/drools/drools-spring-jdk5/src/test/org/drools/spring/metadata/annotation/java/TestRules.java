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

