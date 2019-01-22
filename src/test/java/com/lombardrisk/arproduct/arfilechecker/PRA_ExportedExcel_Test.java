package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class PRA_ExportedExcel_Test 
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\PRA\\Test Results\\1.3.0\\Auto\\PRA1.3.0.1_AR1.16.2b107_4\\download\\Prudential Regulation Authority(ExportToExcelApplyScale)\\";
	private String expectedPath="Z:/ProductLine/PRA/Test Results/1.3.0/Auto/PRA1.3.0.1_AR1.16.2b107_4/expectation/Prudential Regulation Authority/ExportToExcelApplyScale/";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PRA_ExportedExcel_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PRA_ExportedExcel_Test.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testPRA() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB008_v1_20190930.xlsx";     
    	String expectation=expectedPath+"RFB008_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    
}
