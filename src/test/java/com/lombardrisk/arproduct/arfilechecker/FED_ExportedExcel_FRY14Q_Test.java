package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class FED_ExportedExcel_FRY14Q_Test 
    extends TestCase
{
	private String downPath="Z:\\APAutomation\\results\\download\\US FED Reserve(ExportToExcelApplyScale)\\";
	private String expectedPath="Z:\\APAutomation\\results\\expectation\\US FED Reserve\\ExportToExcelApplyScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FED_ExportedExcel_FRY14Q_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//testFED10-testFED14 are failed for scaled.
        return new TestSuite( FED_ExportedExcel_FRY14Q_Test.class );
    }
    
    
    public void testFED() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QMSR_v1_20151231.xlsx";     String expectation=expectedPath+"FRY14QMSR_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    
    public void testFED1() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QPPNR_v3_20160630.xlsx";     String expectation=expectedPath+"FRY14QPPNR_V3_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED2() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QSUPMNT_v2_20151231.xlsx";     String expectation=expectedPath+"FRY14QSUPMNT_V2_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   public void testFED3() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QRCI_v4_20160630.xlsx";     String expectation=expectedPath+"FRY14QRCI_V4_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   public void testFED4() throws Exception
   {
   	System.out.println("love");
   	String downloadFile=downPath+"FED_2999_FRY14QRCT_v4_20160630.xlsx";     String expectation=expectedPath+"FRY14QRCT_V4_ExpectedValue_CheckExcel.xlsx";


   	String log="";
   	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
   	Boolean flag=achecker.checker(downloadFile, expectation);
   	assertTrue( flag );
   }
    public void testFED5() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QSEC_v5_20161230.xlsx";     String expectation=expectedPath+"FRY14QSEC_v5_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED6() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QBAL_v3_20170630.xlsx";     String expectation=expectedPath+"FRY14QBAL_V3_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED7() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QFVOHFS_v3_20170630.xlsx";     String expectation=expectedPath+"FRY14QFVOHFS_V3_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED8() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QRCI_v5_20170630.xlsx";     String expectation=expectedPath+"FRY14QRCI_V5_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED9() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QBAL_v4_20170929.xlsx";     String expectation=expectedPath+"FRY14QBAL_V4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED10() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QCNTPY_v4_20170929.xlsx";     String expectation=expectedPath+"FRY14QCNTPY_V4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED11() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14QCNTPY_v5_20180330.xlsx";     String expectation=expectedPath+"FRY14QCNTPY_V5_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED12() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004A_v1_20150107.xlsx";     String expectation=expectedPath+"FR2004A_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED13() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004SI_v1_20150107.xlsx";     String expectation=expectedPath+"FR2004SI_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED14() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004SD_v1_20150102.xlsx";     String expectation=expectedPath+"FR2004SD_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    
   
}
