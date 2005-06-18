/*
 * Created on 15/06/2005
 */
package org.drools.decisiontable;

import java.io.InputStream;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.decisiontable.model.TestModel;

import junit.framework.TestCase;

public class DecisionTableLoaderTest extends TestCase
{

    /**
     * This is an end to end test, actually lighting up drools.
     * Refer to the examples for more, well, examples !
     * @throws Exception
     */
    public void testLoadBasicWorkbook() throws Exception
    {
        InputStream stream = this.getClass( ).getResourceAsStream( "/data/TestRuleFire.xls" );

        /*
         * SpreadsheetDRLConverter converter = new SpreadsheetDRLConverter();
         * System.out.println(converter.convertToDRL(stream));
         */
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

}
