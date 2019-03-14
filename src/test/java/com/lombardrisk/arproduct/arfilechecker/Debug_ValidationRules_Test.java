package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ValidationRuleChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class Debug_ValidationRules_Test 
    extends TestCase
{
	private String downPath="H:\\ar-file-checker-debug\\Hong Kong Monetary Authority(ExportValidation)\\";
	private String expectedPath="H:\\ar-file-checker-debug\\ExportValidation\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Debug_ValidationRules_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testRule fail, others pass
        return new TestSuite( Debug_ValidationRules_Test.class );
    }
    /**
     * debug for cross-val
     * @throws Exception
     */
    public void testRule() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"HKMA_0002_J_v0_20200731_Validation(2).xlsx";    
    	String expectation=expectedPath+"J_v0_ValidationResult(1).xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
}
    