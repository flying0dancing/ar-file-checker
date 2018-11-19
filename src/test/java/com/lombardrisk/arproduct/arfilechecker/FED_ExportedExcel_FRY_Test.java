package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class FED_ExportedExcel_FRY_Test 
    extends TestCase
{
	private String downPath="Z:\\APAutomation\\results\\download\\US FED Reserve(ExportToExcelNoScale)\\";
	private String expectedPath="Z:\\APAutomation\\results\\expectation\\US FED Reserve\\ExportToExcelNoScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FED_ExportedExcel_FRY_Test( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FED_ExportedExcel_FRY_Test.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testFED() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC009A_v2_20151231.xlsx";     
    	String expectation=expectedPath+"FFIEC009A_V2_ExpectedValue_CheckExcel.xlsx";

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
    	String downloadFile=downPath+"FED_2999_FFIEC009_v1_20151231.xlsx";     
    	String expectation=expectedPath+"FFIEC009_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED2() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2052B_v3_20151231.xlsx";     
    	String expectation=expectedPath+"FR2052B_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED3() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2314_v2_20151231.xlsx";    
    	String expectation=expectedPath+"FR2314_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED4() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2314S_v1_20151231.xlsx";     
    	String expectation=expectedPath+"FR2314S_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED5() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY6_v1_20151231.xlsx";     
    	String expectation=expectedPath+"FRY6_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED6() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY9LP_v2_20151231.xlsx";    
    	String expectation=expectedPath+"FRY9LP_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED7() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY11_v2_20151231.xlsx";     String expectation=expectedPath+"FRY11_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED8() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY11S_v2_20151231.xlsx";     String expectation=expectedPath+"FRY11S_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED9() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY10_v2_20151231.xlsx";     String expectation=expectedPath+"FRY10_v2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED10() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY12_v2_20151231.xlsx";     String expectation=expectedPath+"FRY12_v2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED11() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY15_v3_20151231.xlsx";     String expectation=expectedPath+"FRY15_v3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED12() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC102_v1_20160630.xlsx";     String expectation=expectedPath+"FFIEC102_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED13() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC009_v2_20160930.xlsx";     String expectation=expectedPath+"FFIEC009_v2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED14() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC101_v7_20160930.xlsx";     
    	String expectation=expectedPath+"FFIEC101_v7_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED15() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC102_v2_20161230.xlsx";     String expectation=expectedPath+"FFIEC102_V2_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED16() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY9LP_v3_20160930.xlsx";     String expectation=expectedPath+"FRY9LP_V3_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED17() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY6_v2_20161231.xlsx";     String expectation=expectedPath+"FRY6_V2_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED18() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY10_v3_20161014.xlsx";     String expectation=expectedPath+"FRY10_v3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED19() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY15_v4_20161230.xlsx";     String expectation=expectedPath+"FRY15_v4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED20() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY9LP_v4_20170331.xlsx";     String expectation=expectedPath+"FRY9LP_V4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED21() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY9C_v6_20170331.xlsx";     String expectation=expectedPath+"FRY9C_V6_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED22() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY15_v5_20180330.xlsx";     String expectation=expectedPath+"FRY15_v5_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED23() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7Q_v2_20161230.xlsx";     String expectation=expectedPath+"FRY7Q_v2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED24() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC030S_v1_20160331.xlsx";     String expectation=expectedPath+"FFIEC030S_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED25() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY9C_v7_20180330.xlsx";     String expectation=expectedPath+"FRY9C_V7_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED26() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC031_v11_20170929.xlsx";     String expectation=expectedPath+"FFIEC031_v11_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED27() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC041_v11_20170929.xlsx";     String expectation=expectedPath+"FFIEC041_v11_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED28() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2314_v3_20180330.xlsx";     String expectation=expectedPath+"FR2314_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED29() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY11_v3_20180330.xlsx";     String expectation=expectedPath+"FRY11_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED30() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC101_v8_20170929.xlsx";     String expectation=expectedPath+"FFIEC101_v8_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED31() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY9LP_v5_20180330.xlsx";     String expectation=expectedPath+"FRY9LP_V5_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED32() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7N_v2_20180330.xlsx";     String expectation=expectedPath+"FRY7N_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED33() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7NS_v1_20151231.xlsx";     String expectation=expectedPath+"FRY7NS_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED34() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY8_v2_20151231.xlsx";     String expectation=expectedPath+"FRY8_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED35() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2886B_v2_20170929.xlsx";     String expectation=expectedPath+"FR2886B_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED36() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC031_v12_20171229.xlsx";     String expectation=expectedPath+"FFIEC031_V12_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED37() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC041_v12_20171229.xlsx";     String expectation=expectedPath+"FFIEC041_V12_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED38() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2900_v4_20150930.xlsx";     String expectation=expectedPath+"FR2900_V4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED39() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2644_v3_20180103.xlsx";     String expectation=expectedPath+"FR2644_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED40() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2420A_v3_20151020.xlsx";     String expectation=expectedPath+"FR2420A_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED41() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2420B_v3_20151020.xlsx";     String expectation=expectedPath+"FR2420B_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED42() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2420C_v4_20160115.xlsx";     String expectation=expectedPath+"FR2420C_V4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED43() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2028D_v1_20171229.xlsx";     String expectation=expectedPath+"FR2028D_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED44() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2028S_v2_20170605.xlsx";     String expectation=expectedPath+"FR2028S_V2_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED45() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2225_v1_20170831.xlsx";     String expectation=expectedPath+"FR2225_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED46() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2248_v1_20170331.xlsx";     String expectation=expectedPath+"FR2248_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED47() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2835_v1_20150831.xlsx";     String expectation=expectedPath+"FR2835_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED48() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2910A_v1_20160630.xlsx";     String expectation=expectedPath+"FR2910A_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED49() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2915_v2_20150930.xlsx";     String expectation=expectedPath+"FR2915_V2_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED50() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY9ES_v1_20151231.xlsx";     String expectation=expectedPath+"FRY9ES_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED51() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004B_v1_20150107.xlsx";     String expectation=expectedPath+"FR2004B_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED52() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004C_v1_20150107.xlsx";     String expectation=expectedPath+"FR2004C_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED53() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004FA_v1_20150102.xlsx";     String expectation=expectedPath+"FR2004FA_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED54() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004FB_v1_20150102.xlsx";     String expectation=expectedPath+"FR2004FB_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED55() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004FC_v1_20150102.xlsx";     String expectation=expectedPath+"FR2004FC_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED56() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004FM_v1_20150102.xlsx";     String expectation=expectedPath+"FR2004FM_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED57() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2004WI_v1_20150107.xlsx";     String expectation=expectedPath+"FR2004WI_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED58() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY9SP_v1_20170630.xlsx";     String expectation=expectedPath+"FRY9SP_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED59() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_TICCQ2_v1_20170929.xlsx";     String expectation=expectedPath+"TICCQ2_V1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED60() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_IRS1042S_v1_20160630.xlsx";     String expectation=expectedPath+"IRS1042S_V1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED61() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_SSOI_v1_20151231.xlsx";     String expectation=expectedPath+"FINRASSOI_v1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED62() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FOCUSP2_v1_20151231.xlsx";     String expectation=expectedPath+"FINRASSOIP2_v1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED63() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_TICCQ1_v1_20170930.xlsx";     String expectation=expectedPath+"TICCQ1_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED64() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY7_v3_20180228.xlsx";     String expectation=expectedPath+"FRY7_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED65() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC031_v13_20180330.xlsx";     String expectation=expectedPath+"FFIEC031_V13_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED66() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC041_v13_20180330.xlsx";     String expectation=expectedPath+"FFIEC041_V13_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED67() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FR2886B_v3_20180330.xlsx";     String expectation=expectedPath+"FR2886B_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED68() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC009A_v4_20160930.xlsx";     String expectation=expectedPath+"FFIEC009A_v4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED69() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC030_v3_20160930.xlsx";     String expectation=expectedPath+"FFIEC030_V3_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED70() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY12A_v1_20180330.xlsx";     String expectation=expectedPath+"FRY12A_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED71() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY15_v6_20180330.xlsx";     String expectation=expectedPath+"FRY15_v6_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED72() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FFIEC101_v9_20180330.xlsx";     String expectation=expectedPath+"FFIEC101_v9_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED73() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_NYSBD_v1_20180330.xlsx";     String expectation=expectedPath+"NYSBD_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED74() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_NYSBDWR_v1_20180330.xlsx";     String expectation=expectedPath+"NYSBD_WR_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED75() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FDIC370_v1_20180330.xlsx";     String expectation=expectedPath+"FDIC370_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED76() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_QSS1A_v1_20180330.xlsx";     String expectation=expectedPath+"QSS1A_v1_ExpectedValue_CheckExcel.xlsx";


    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED77() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_MDS_v1_20180330.xlsx";     String expectation=expectedPath+"MDS_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED78() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_OFRU51_v1_20180330.xlsx";     String expectation=expectedPath+"OFRU51_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED79() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_OFRU50_v1_20180330.xlsx";     String expectation=expectedPath+"OFRU50_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED80() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_OFRU52_v1_20180330.xlsx";     String expectation=expectedPath+"OFRU52_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED81() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_DBO523_v1_20180330.xlsx";     String expectation=expectedPath+"DBO523_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
    public void testFED82() throws Exception
    {
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_DBO95_v1_20180330.xlsx";     String expectation=expectedPath+"DBO95_v1_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
   
}
