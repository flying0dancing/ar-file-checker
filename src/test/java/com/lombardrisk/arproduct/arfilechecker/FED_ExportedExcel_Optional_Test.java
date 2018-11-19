package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class FED_ExportedExcel_Optional_Test 
    extends TestCase
{
	private String downPath="Z:\\APAutomation\\results\\download\\US FED Reserve(ExportToExcelNoScale)\\";
	private String expectedPath="Z:\\APAutomation\\results\\expectation\\US FED Reserve\\ExportToExcelNoScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FED_ExportedExcel_Optional_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
    	//pass all 
        return new TestSuite( FED_ExportedExcel_Optional_Test.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testFED() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FDIC8020_v1_20160630.xlsx";     String expectation=expectedPath+"FDIC8020_v1_ExpectedValue_CheckExcel.xlsx";


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
    	String downloadFile=downPath+"FED_2999_FFIEC002_v1_20151231.xlsx";     String expectation=expectedPath+"FFIEC002_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED2() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC002S_v1_20151231.xlsx";     String expectation=expectedPath+"FFIEC002S_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED3() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2028_v1_20151231.xlsx";     String expectation=expectedPath+"FR2028_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED4() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2420A_v2_20151231.xlsx";     String expectation=expectedPath+"FR2420A_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED5() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2420B_v2_20151231.xlsx";     String expectation=expectedPath+"FR2420B_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED6() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2420C_v2_20151231.xlsx";     String expectation=expectedPath+"FR2420C_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED7() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2420C_v3_20160131.xlsx";     String expectation=expectedPath+"FR2420C_v3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED8() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2502Q_v2_20151231.xlsx";     String expectation=expectedPath+"FR2502Q_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED9() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2572_v2_20160630.xlsx";     String expectation=expectedPath+"FR2572_v2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED10() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2644_v2_20151231.xlsx";     String expectation=expectedPath+"FR2644_v2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED11() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2835A_v2_20151231.xlsx";     String expectation=expectedPath+"FR2835A_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED12() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2886B_v1_20151231.xlsx";     String expectation=expectedPath+"FR2886B_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED13() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2900_v3_20151231.xlsx";     String expectation=expectedPath+"FR2900_v3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED14() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2900CB_v1_20151231.xlsx";     String expectation=expectedPath+"FR2900CB_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED15() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2915_v1_20151231.xlsx";     String expectation=expectedPath+"FR2915_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED16() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2930_v1_20151231.xlsx";     String expectation=expectedPath+"FR2930_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED17() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY12A_v1_20151231.xlsx";     String expectation=expectedPath+"FRY12A_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED18() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY16_v1_20151231.xlsx";     String expectation=expectedPath+"FRY16_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED19() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY20_v2_20151231.xlsx";     String expectation=expectedPath+"FRY20_v2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED20() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7_v1_20151231.xlsx";     String expectation=expectedPath+"FRY7_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED21() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7_v2_20161231.xlsx";     String expectation=expectedPath+"FRY7_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED22() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7N_v1_20151231.xlsx";     String expectation=expectedPath+"FRY7N_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED23() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7NS_v1_20151231.xlsx";     String expectation=expectedPath+"FRY7NS_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED24() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7Q_v1_20151231.xlsx";     String expectation=expectedPath+"FRY7Q_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED25() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY8_v2_20151231.xlsx";     String expectation=expectedPath+"FRY8_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED26() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FDIC8020_v2_20170630.xlsx";     String expectation=expectedPath+"FDIC8020_v2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   
   
}
