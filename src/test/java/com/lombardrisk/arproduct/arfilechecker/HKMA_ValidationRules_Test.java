package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ValidationRuleChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class HKMA_ValidationRules_Test 
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\HKMA\\autoResults\\HKMA_1201_ValCheck\\download\\Hong Kong Monetary Authority(arfilechecker)\\";
	private String expectedPath="Z:\\ProductLine\\HKMA\\autoResults\\HKMA_1201_ValCheck\\expectation\\Hong Kong Monetary Authority\\ExportValidation_arfilechecker\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HKMA_ValidationRules_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testRule fail, others pass
        return new TestSuite( HKMA_ValidationRules_Test.class );
    }
    
    public void testRule5() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"IB_V1_v1_0002_31072019_validations.xlsx";    
    	String expectation=expectedPath+"IB_V1_V1_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testRule() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"AA_v0_0002_31072019_validations.xlsx";     
    	String expectation=expectedPath+"AA_V0_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testRule1() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"DS_v0_0002_31072019_validations.xlsx";     
    	String expectation=expectedPath+"DS_V0_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testRule2() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"E_v0_0002_31072019_validations.xlsx";     
    	String expectation=expectedPath+"E_V0_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testRule3() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"F_v0_0002_31072019_validations.xlsx";    
    	String expectation=expectedPath+"F_V0_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    
    public void testRule4() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"O_v0_0002_31072019_validations.xlsx";    
    	String expectation=expectedPath+"O_V0_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    
  
    
    public void testRule6() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"J_V0_0002_31072019_validations.xlsx";    
    	String expectation=expectedPath+"J_V0_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   
}
