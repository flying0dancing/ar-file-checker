package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class SRDD_ExportedExcel_Test 
    extends TestCase
{
	private String downPath="Z:\\ProductLine\\SRDD\\TestResult\\2.13.0\\Auto\\SRDD2.13.0.4_AR19.1.4b111\\download\\Stats and Regulatory Data Div(ExportToExcelApplyScale)\\";
	private String expectedPath="Z:/ProductLine/SRDD/TestResult/2.13.0/Auto/SRDD2.13.0.4_AR19.1.4b111/expectation/Stats and Regulatory Data Div/ExportToExcelApplyScale/";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SRDD_ExportedExcel_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SRDD_ExportedExcel_Test.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testSRDD() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MFSD_3600_BG_v4_20171231(1).xlsx";     
    	String expectation=expectedPath+"BG_V4_FA2_CheckExcel_debug.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    
}
