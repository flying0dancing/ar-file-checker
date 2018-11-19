package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class FED_ExportedExcel_FRY14A_Test 
    extends TestCase
{
	private String downPath="Z:\\APAutomation\\results\\download\\US FED Reserve(ExportToExcelNoScale)\\";
	private String expectedPath="Z:\\APAutomation\\results\\expectation\\US FED Reserve\\ExportToExcelNoScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FED_ExportedExcel_FRY14A_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//all pass
        return new TestSuite( FED_ExportedExcel_FRY14A_Test.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testFED() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14AOR_v4_20161230.xlsx";     
    	String expectation=expectedPath+"FRY14AOR_v4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testFED1() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14ARRE_v1_20160630.xlsx";     
    	String expectation=expectedPath+"FRY14ARRE_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED2() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14ARCI_v4_20161230.xlsx";     
    	String expectation=expectedPath+"FRY14ARCI_v4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED3() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14ASCENR_v2_20151231.xlsx";    
    	String expectation=expectedPath+"FRY14ASCENRO_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   
   
}
