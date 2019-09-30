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
        //version 19.3
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_O610A1_v1_20181031_Validation.xlsx";
    	String expectation=expectedPath+"O610A1_v1_ValidationResult.xlsx";

    	String log="";
    	ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker();
    	assertTrue( flag );
    }

    public void testRule1() throws Exception
    {
        //version 16.2
        System.out.println("love");
        String downloadFile=downPath+"HKMA_0002_DS_v0_20200731_Validation.xlsx";
        String expectation=expectedPath+"DS_v0_ValidationResult.xlsx";

        String log="";
        ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
        Boolean flag=achecker.checker();
        assertTrue( flag );
    }

    public void testRule2() throws Exception
    {
        //version 16.2
        System.out.println("love");
        String downloadFile=downPath+"HKMA_0002_IB_V1_v1_20200731_Validation.xlsx";
        String expectation=expectedPath+"IB_V1_v1_ValidationResult.xlsx";

        String log="";
        ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
        Boolean flag=achecker.checker();
        assertTrue( flag );
    }

    public void testRule3() throws Exception
    {
        //version 16.2
        System.out.println("love");
        String downloadFile=downPath+"HKMA_0002_IB_V2_v2_20200731_Validation.xlsx";
        String expectation=expectedPath+"IB_V2_v2_ValidationResult.xlsx";

        String log="";
        ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
        Boolean flag=achecker.checker();
        assertTrue( flag );
    }

    public void testRule4() throws Exception
    {
        //version 19.3
        System.out.println("love19.3:Row columns are more");
        String downloadFile=downPath+"MAS_0002_MAS649_v1_20200731_Validation.xlsx";
        String expectation=expectedPath+"MAS649_v1_ValidationResult.xlsx";

        String log="";
        ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
        Boolean flag=achecker.checker();
        assertTrue( flag );
    }

    public void testRule5() throws Exception
    {
        //version 19.3
        System.out.println("love16.1");
        String downloadFile=downPath+"AA_v0_0002_07312020_validations.xlsx";
        String expectation=expectedPath+"AA_v0_ValidationResult.xlsx";

        String log="";
        ValidationRuleChecker achecker=new ValidationRuleChecker( downloadFile, expectation);
        Boolean flag=achecker.checker();
        assertTrue( flag );
    }
   
}
