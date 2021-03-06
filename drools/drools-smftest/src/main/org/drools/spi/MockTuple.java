package org.drools.spi;

/*
 * $Id: MockTuple.java,v 1.2 2005-05-07 03:54:54 dbarnett Exp $
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;

public class MockTuple implements Tuple
{
    private Rule          rule;

    private WorkingMemory workingMemory;

    private Map           tuple;

    private long          mostRecentTimeStamp;

    private long          leastRecentTimeStamp;

    private long[]        conditionTimeStamps;

    public MockTuple()
    {
        this.tuple = new HashMap( );
    }

    public Object get(Declaration declaration)
    {
        return this.tuple.get( declaration );
    }

    public void put(Declaration declaration, Object value)
    {
        this.tuple.put( declaration, value );
    }

    public Set getDeclarations()
    {
        return this.tuple.keySet( );
    }

    public FactHandle getFactHandleForObject(Object object)
    {
        return null;
    }

    public void setRule(Rule rule)
    {
        this.rule = rule;
    }

    public Rule getRule()
    {
        return this.rule;
    }

    public void setWorkingMemory(WorkingMemory workingMemory)
    {
        this.workingMemory = workingMemory;
    }

    public WorkingMemory getWorkingMemory()
    {
        return this.workingMemory;
    }

    public void setMostRecentFactTimeStamp(long timeStamp)
    {
        this.mostRecentTimeStamp = timeStamp;
    }

    public void setLeastRecentFactTimeStamp(long timeStamp)
    {
        this.leastRecentTimeStamp = timeStamp;
    }

    public void setConditionTimeStamps(long[] timeStamps)
    {
        this.conditionTimeStamps = timeStamps;
    }

    public long getMostRecentFactTimeStamp()
    {
        return this.mostRecentTimeStamp;
    }

    public long getLeastRecentFactTimeStamp()
    {
        return this.leastRecentTimeStamp;
    }

    public void setConditionTimeStamp(int order, long timeStamp)
    {
        this.conditionTimeStamps[order] = timeStamp;
    }

    public long getConditionTimeStamp(int order)
    {
        return this.conditionTimeStamps[order];
    }

    public long[] getConditionTimeStamps()
    {
        return this.conditionTimeStamps;
    }
}