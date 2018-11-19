package com.lombardrisk.arproduct.arfilechecker;

import java.io.File;

import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;
import com.lombardrisk.arproduct.utils.CsvDBUtil;
import com.lombardrisk.arproduct.utils.ExcelUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	private String downPath="Z:\\APAutomation\\results\\download\\Prudential Regulation Authority(ExportToExcelApplyScale)\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//fail testApp2 cannot find cell in cellInfo
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testApp() throws Exception
    {
    	System.out.println("love");
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_PRA109_v1_20190930.xlsx";
    	String expectation="E:\\abc\\PRA109_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testApp1() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_0001_BRANCH_v1_20190930.xlsx";
    	String expectation="E:\\abc\\BRANCH_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp2() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_0001_Capital_Plus_v1_20190930.xlsx";
    	String expectation="E:\\abc\\Capital_Plus_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp3() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_0001_FS_GAAP_v1_20190930.xlsx";
    	String expectation="E:\\abc\\FS_GAAP_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp4() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_0001_FS_IFRS_v1_20190930.xlsx";
    	String expectation="E:\\abc\\FS_IFRS_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp5() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_0001_FS_Memo_v1_20190930.xlsx";
    	String expectation="E:\\abc\\FS_Memo_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp6() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB001_v1_20190930.xlsx";
    	String expectation="E:\\abc\\RFB001_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp7() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB002_v1_20190930.xlsx";
    	String expectation="E:\\abc\\RFB002_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp8() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB003_v1_20190930.xlsx";
    	String expectation="E:\\abc\\RFB003_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp9() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB004_v1_20190930.xlsx";
    	String expectation="E:\\abc\\RFB004_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp10() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB005_v1_20190930.xlsx";
    	String expectation="E:\\abc\\RFB005_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp11() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB006_v1_20190930.xlsx";
    	String expectation="E:\\abc\\RFB006_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp12() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB007_v1_20190930.xlsx";
    	String expectation="E:\\abc\\RFB007_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp13() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_RFB008_v1_20190930.xlsx";
    	String expectation="E:\\abc\\RFB008_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testApp14() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"PRA_3000_PRA109_v1_20190930.xlsx";
    	String expectation="E:\\abc\\PRA109_V1_ExpectedValue_CheckExcel.xlsx";
    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
}
