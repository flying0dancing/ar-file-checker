package com.lombardrisk.arproduct.arfilechecker;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class HKMA_ExportedExcel_Test
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\HKMA\\autoResults\\HKMA5.29.0_NewReturns-2\\download\\Hong Kong Monetary Authority(ExportToExcelApplyScale)\\";
	private String expectedPath="Z:/ProductLine/HKMA/autoResults/HKMA5.29.0_NewReturns-2/expectation/Hong Kong Monetary Authority/ExportToExcelApplyScale/";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HKMA_ExportedExcel_Test(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( HKMA_ExportedExcel_Test.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testHKMA() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"HKMA_0001_T10LE_v1_20180330.xlsx";
    	String expectation=expectedPath+"T10LE_V1_0001_ARDisplay.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    /**
     * Rigourous Test :-)
     * @throws Exception
     */
    public void testHKMA2() throws Exception
    {
        System.out.println("love");
        String downloadFile=downPath+"HKMA_0001_T10BLGE_v1_20180330.xlsx";
        String expectation=expectedPath+"T10BLGE_V1_0001_ARDisplay.xlsx";

        String log="";
        ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
        Boolean flag=achecker.checker(downloadFile, expectation);
        assertTrue( flag );
    }
}
