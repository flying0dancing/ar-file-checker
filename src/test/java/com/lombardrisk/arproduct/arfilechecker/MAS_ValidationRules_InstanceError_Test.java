package com.lombardrisk.arproduct.arfilechecker;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MAS_ValidationRules_InstanceError_Test
    extends TestCase
{
	private String downPath="D:\\";
	private String expectedPath="D:\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MAS_ValidationRules_InstanceError_Test(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testRule fail, others pass
        return new TestSuite( MAS_ValidationRules_InstanceError_Test.class );
    }
    
   
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testRule() throws Exception
    {
        //version 19.3
    	System.out.println("love");
    	String downloadFile=downPath+"HKMA_0002_IB_V2_v2_20200731_Validation(2).xlsx";
    	String expectation=expectedPath+"IB_V2_v2_ValidationResult(2).xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker();
    	assertTrue( flag );
    }


   
}
