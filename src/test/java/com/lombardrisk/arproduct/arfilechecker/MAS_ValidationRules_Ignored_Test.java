package com.lombardrisk.arproduct.arfilechecker;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MAS_ValidationRules_Ignored_Test
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\PRA\\Test Results\\1.6.0\\Auto\\PRA1.6.0_AR19.3.0b207\\download\\Prudential Regulation Authority(ExportValidation)\\";
	private String expectedPath="Z:\\ProductLine\\PRA\\Test Results\\1.6.0\\Auto\\PRA1.6.0_AR19.3.0b207\\expectation\\Prudential Regulation Authority\\ExportValidation\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MAS_ValidationRules_Ignored_Test(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testRule fail, others pass
        return new TestSuite( MAS_ValidationRules_Ignored_Test.class );
    }
    
   
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testRule() throws Exception
    {
        //version 19.3
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_MRL003_v1_20190930_Validation.xlsx";
    	String expectation=expectedPath+"MRL003_V1_Rules.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker();
    	assertTrue( flag );
    }


   
}
