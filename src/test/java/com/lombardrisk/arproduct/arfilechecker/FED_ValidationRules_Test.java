package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ValidationRuleChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class FED_ValidationRules_Test 
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\FED\\TestResults\\FED_1.14.2\\Auto\\1.14.2_AR1.16.0b75\\download\\US FED Reserve(ExportValidation)_arfileck\\";
	private String expectedPath="Z:\\ProductLine\\FED\\TestResults\\FED_1.14.2\\Auto\\1.14.2_AR1.16.0b75\\expectation\\US FED Reserve\\ExportValidation_arfileck\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FED_ValidationRules_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testRule fail, others pass
        return new TestSuite( FED_ValidationRules_Test.class );
    }
    
   
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testRule() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FFIEC009_v2_2999_09302016_validations.xlsx";     
    	String expectation=expectedPath+"FFIEC009_v2_Rules.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker();
    	assertTrue( flag );
    }
   
   
}
