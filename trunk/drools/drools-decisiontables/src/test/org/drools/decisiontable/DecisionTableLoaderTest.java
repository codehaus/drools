package org.drools.decisiontable;

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

import java.io.InputStream;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.decisiontable.model.TestModel;

import junit.framework.TestCase;

public class DecisionTableLoaderTest extends TestCase
{

    /**
     * This is an end to end test, actually lighting up drools.
     * It is really an integration test, if you have to give it a name.
     * 
     * IMPORTANT NOTE: If you see errors with this, look for other unit test errors fist, 
     * and correct them, rather then try and debug this outright. 
     * 
     * Refer to the examples for more, well, examples !
     * 
     */
    public void testIntegrationLoadBasicWorkbook() throws Exception
    {
        InputStream stream = this.getClass( ).getResourceAsStream( "/data/TestRuleFire.xls" );

        
        RuleBase rb = DecisionTableLoader.loadFromInputStream( stream );
        assertNotNull( rb );

        WorkingMemory engine = rb.newWorkingMemory( );

        TestModel model = new TestModel( );
        model.setFireRule( true );
        assertFalse( model.isRuleFired( ) );
        engine.assertObject( model );
        engine.fireAllRules( );
        assertTrue( model.isRuleFired( ) );

    }
    
    public void testIntegrationLoadBasicWorkbookCSV() throws Exception
    {
        InputStream stream = this.getClass( ).getResourceAsStream( "/data/TestRuleFire.csv" );

        RuleBase rb = DecisionTableLoader.loadFromInputStream( stream, InputType.CSV );
        assertNotNull( rb );

        WorkingMemory engine = rb.newWorkingMemory( );

        TestModel model = new TestModel( );
        model.setFireRule( true );
        assertFalse( model.isRuleFired( ) );
        engine.assertObject( model );
        engine.fireAllRules( );
        assertTrue( model.isRuleFired( ) );

    }    
    
    /**
     * for debugging purposes only if needed
     *
     */
    public void xxxtestLoadAndPrint() {        
        InputStream stream = this.getClass( ).getResourceAsStream( "/data/TestRuleFire.csv" );
        SpreadsheetDRLConverter converter = new SpreadsheetDRLConverter();
        System.out.println(converter.convertToDRL(stream, InputType.CSV));        
    }

}

