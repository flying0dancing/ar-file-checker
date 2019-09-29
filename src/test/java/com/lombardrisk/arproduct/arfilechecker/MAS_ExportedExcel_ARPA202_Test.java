package com.lombardrisk.arproduct.arfilechecker;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MAS_ExportedExcel_ARPA202_Test
    extends TestCase
{
    private String downPath="Z:\\ProductLine\\MAS\\autoResults\\MAS2.23_1_4\\download\\Monetary Authority of Singapore(ExportToExcelApplyScale)\\";
    private String expectedPath="Z:\\ProductLine\\MAS\\autoResults\\MAS2.23_1_4\\expectation\\Monetary Authority of Singapore\\ExportToExcelApplyScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MAS_ExportedExcel_ARPA202_Test(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MAS_ExportedExcel_ARPA202_Test.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception
     */
    public void testHKMA() throws Exception
    {
        System.out.println("love");
        String downloadFile=downPath+"MAS_0002_MAS610_K_v1_20181231.xlsx";
        String expectation=expectedPath+"MAS610_K_V1_0001_20190731_test.xlsx";

        String log="";
        ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
        Boolean flag=achecker.checker(downloadFile, expectation);
        assertTrue( flag );
    }

    public void testHKMA0929() throws Exception
    {
        System.out.println("love");
        String downloadFile=downPath+"MAS_0002_MAS1111_v1_20181231.xlsx";
        String expectation=expectedPath+"MAS1111_V1_0001_20190731_ExcelDisplay.xlsx";

        String log="";
        ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
        Boolean flag=achecker.checker(downloadFile, expectation);
        assertTrue( flag );
    }

}
