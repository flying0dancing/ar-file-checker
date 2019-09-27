package com.lombardrisk.arproduct.arfilechecker;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MAS_ValidationRules_ARPA214_Test
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\MAS\\autoResults\\demokun\\download\\Monetary Authority of Singapore(ExportValidation)\\";
	private String expectedPath="Z:\\ProductLine\\MAS\\autoResults\\demokun\\expectation\\Monetary Authority of Singapore\\ExportValidation\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MAS_ValidationRules_ARPA214_Test(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testRule fail, others pass
        return new TestSuite( MAS_ValidationRules_ARPA214_Test.class );
    }
    
   
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testRule() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_O610A1_v1_20181031_Validation.xlsx";
    	String expectation=expectedPath+"O610A1_v1_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker();
    	assertTrue( flag );
    }
   
   
}
