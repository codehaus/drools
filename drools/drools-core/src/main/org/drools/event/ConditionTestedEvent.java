package org.drools.event;

/*
 * $Id: ConditionTestedEvent.java,v 1.6 2005-05-08 02:57:11 dbarnett Exp $
 *
 * Copyright 2004-2005 (C) The Werken Company. All Rights Reserved.
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

import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.Tuple;

public class ConditionTestedEvent extends WorkingMemoryEvent
{
    private final Rule rule;

    private final Condition condition;

    private final Tuple tuple;

    private final boolean passed;

    public ConditionTestedEvent(WorkingMemory workingMemory,
                                Rule rule,
                                Condition condition,
                                Tuple tuple,
                                boolean passed)
    {
        super( workingMemory );

        this.rule = rule;
        this.condition = condition;
        this.tuple = tuple;
        this.passed = passed;
    }

    public Rule getRule()
    {
        return this.rule;
    }

    public Condition getCondition()
    {
        return this.condition;
    }

    public Tuple getTuple()
    {
        return this.tuple;
    }

    public boolean getPassed()
    {
        return this.passed;
    }

    public String toString()
    {
        return "[ConditionTested: rule=" + this.rule.getName( ) + "; condition=" + this.condition + "; passed=" + this.passed + "; tuple=" + this.tuple + "]";
    }
}
