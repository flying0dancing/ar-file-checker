package com.lombardrisk.arproduct.arfilechecker;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class PRA_ExportedExcel_ARPA301_Test
        extends TestCase
{
    private String downPath="Z:\\ProductLine\\EBA\\TestResults\\AR4ECR_1.35.0\\Auto\\ECR1.35.0_AR19.4.1b156\\download\\European Common Reporting(ExportToExcelApplyScale)\\";
    private String expectedPath="Z:\\ProductLine\\EBA\\TestResults\\AR4ECR_1.35.0\\Auto\\ECR1.35.0_AR19.4.1b156\\expectation\\European Common Reporting\\ExportToExcelApplyScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PRA_ExportedExcel_ARPA301_Test(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PRA_ExportedExcel_ARPA301_Test.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception
     */
    public void testPRA1() throws Exception
    {
        System.out.println("love");
        String downloadFile=downPath+"ECR_2999_C107_v3_20200630(1).xlsx";
        String expectation=expectedPath+"C107_V3_2999_ARDisplay(1).xlsx";

        String log="";
        ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
        Boolean flag=achecker.checker(downloadFile, expectation);
        assertTrue( flag );
    }
}
