package org.drools.jsr94;

/*
 * $Id: AllTests.java,v 1.1 2004-11-19 14:07:05 dbarnett Exp $
 *
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.drools.jsr94.benchmark.drools.DroolsBenchmarkTestCase;
import org.drools.jsr94.rules.RuleRuntimeTestCase;
import org.drools.jsr94.rules.RuleServiceProviderTestCase;
import org.drools.jsr94.rules.StatefulRuleSessionTestCase;
import org.drools.jsr94.rules.StatelessRuleSessionTestCase;
import org.drools.jsr94.rules.admin.LocalRuleExecutionSetProviderTestCase;
import org.drools.jsr94.rules.admin.RuleAdministratorTestCase;
import org.drools.jsr94.rules.admin.RuleExecutionSetProviderTestCase;
import org.drools.jsr94.rules.admin.RuleExecutionSetTestCase;
import org.drools.jsr94.rules.admin.RuleTestCase;

/**
 * Runs all the tests in the <code>org.drools.jsr94</code> hierarchy.
 * JUnit must be set to fork these tests, so it is much faster to run them
 * under a single forked <code>TestSuite</code> than to run them each
 * individually under several separate forked <code>TestCase</code>s.
 * <p/>
 * The negative side of this is that any new <code>TestCase</code>s in the
 * <code>org.drools.jsr94</code> hierarchy must also be added to this class
 * otherwise Maven will not run them as part of its normal test cycle.
 */
public class AllTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite( "Drools JSR-94 Test Suite" );

        suite.addTestSuite(DroolsBenchmarkTestCase.class);
        
        suite.addTestSuite(RuleRuntimeTestCase.class);
        suite.addTestSuite(RuleServiceProviderTestCase.class);
        suite.addTestSuite(StatefulRuleSessionTestCase.class);
        suite.addTestSuite(StatelessRuleSessionTestCase.class);

        suite.addTestSuite(LocalRuleExecutionSetProviderTestCase.class);
        suite.addTestSuite(RuleAdministratorTestCase.class);
        suite.addTestSuite(RuleExecutionSetProviderTestCase.class);
        suite.addTestSuite(RuleExecutionSetTestCase.class);
        suite.addTestSuite(RuleTestCase.class);

        return suite;
   }
}
