package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ValidationRuleChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class PRA_ValidationRules_Test 
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\PRA\\Test Results\\1.3.0\\Auto\\PRA1.3.0.3_AR1.16.2.2b119\\download\\Prudential Regulation Authority(ExportValidation)\\";
	private String expectedPath="Z:\\ProductLine\\PRA\\Test Results\\1.3.0\\Auto\\PRA1.3.0.3_AR1.16.2.2b119\\expectation\\Prudential Regulation Authority\\ExportValidation\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PRA_ValidationRules_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testRule fail, others pass
        return new TestSuite( PRA_ValidationRules_Test.class );
    }
    
   
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testRule() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB001_v1_20190930_Validation (1).xlsx";     
    	String expectation=expectedPath+"RFB001_V1_Rules(1).xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker();
    	assertTrue( flag );
    }
   
   
}
