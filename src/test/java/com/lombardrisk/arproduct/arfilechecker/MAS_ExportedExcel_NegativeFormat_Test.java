package com.lombardrisk.arproduct.arfilechecker;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MAS_ExportedExcel_NegativeFormat_Test
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\MAS\\autoResults\\MAS233_DetailUAT194\\download\\Monetary Authority of Singapore(ExportToExcelApplyScale)\\";
	private String expectedPath="Z:\\ProductLine\\MAS\\autoResults\\MAS233_DetailUAT194\\expectation\\Monetary Authority of Singapore\\ExportToExcelApplyScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MAS_ExportedExcel_NegativeFormat_Test(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testMAS,testMAS2 are failed for data's display format negative are contains () actually.
        return new TestSuite( MAS_ExportedExcel_NegativeFormat_Test.class );
    }
    
    
    public void testMAS() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_B1_B2_v1_20181231.xlsx";
    	String expectation=expectedPath+"MAS610_B1_B2_V1_0001_20190731_ARDisplay1.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    

   
}
