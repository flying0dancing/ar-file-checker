package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MAS_ExportedExcel_Test 
    extends TestCase
{
	private String downPath="Z:\\APAutomation\\results\\download\\Monetary Authority of Singapore(ExportToExcelApplyScale)\\";
	private String expectedPath="Z:\\APAutomation\\results\\expectation\\Monetary Authority of Singapore\\ExportToExcelApplyScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MAS_ExportedExcel_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testMAS,testMAS2 are failed for data's display format negative are contains () actually.
        return new TestSuite( MAS_ExportedExcel_Test.class );
    }
    
    
    public void testMAS() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_B1_B2_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_B1_B2_V1_0001_CheckCellValue.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    
    public void testMAS1() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_B3_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_B3_V1_0001_CheckCellValue.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testMAS2() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_C1_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_C1_V1_0001_CheckCellValue.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   public void testMAS3() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_D3_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_D3_V1_0001_CheckCellValue.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   public void testMAS4() throws Exception
   {
   	System.out.println("love");
   	String downloadFile=downPath+"MAS_0002_MAS610_E1_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_E1_V1_0001_CheckCellValue.xlsx";


   	String log="";
   	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
   	Boolean flag=achecker.checker(downloadFile, expectation);
   	assertTrue( flag );
   }
    public void testMAS5() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_E2_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_E2_V1_0001_CheckCellValue.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testMAS6() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_E3_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_E3_V1_0001_CheckCellValue.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testMAS7() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_F_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_F_V1_0001_CheckCellValue.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testMAS8() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"MAS_0002_MAS610_H_v1_20190731.xlsx";     String expectation=expectedPath+"MAS610_H_V1_0001_CheckCellValue.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   
}
