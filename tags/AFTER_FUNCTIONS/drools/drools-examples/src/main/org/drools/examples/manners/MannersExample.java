package org.drools.examples.manners;

/*
 * $Id: MannersExample.java,v 1.7 2004-12-04 14:59:45 simon Exp $
 *
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
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
 */

import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;

import java.util.Iterator;
import java.util.List;

/**
 * An example of executing a rulebase with Drools without
 * all the JSR-94 cruft to solve the Miss Manners problem.
 */
public class MannersExample extends MannersBase
{
    /** Drools working memory. */
    private WorkingMemory workingMemory;

    public static void main( String[] args ) throws Exception
    {
        new MannersExample( args ).run( );
    }

    public MannersExample( String[] args )
    {
        super( args );
    }

    protected void setUp( ) throws Exception
    {
        RuleBase ruleBase = RuleBaseLoader.loadFromUrl(
            MannersExample.class.getResource( ruleUri ) );

        workingMemory = ruleBase.newWorkingMemory( );
    }

    protected void tearDown( )
    {
        workingMemory = null;
    }

    protected List test( List inList ) throws FactException
    {
        for ( Iterator i = inList.iterator( ); i.hasNext( ); )
        {
            workingMemory.assertObject( i.next( ) );
        }
        workingMemory.fireAllRules( );

        return workingMemory.getObjects( );
    }
}