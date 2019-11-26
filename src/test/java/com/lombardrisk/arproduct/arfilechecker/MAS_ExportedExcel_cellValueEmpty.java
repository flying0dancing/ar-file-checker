package com.lombardrisk.arproduct.arfilechecker;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MAS_ExportedExcel_cellValueEmpty
    extends TestCase
{
    private String downPath="Z:\\ProductLine\\MAS\\autoResults\\MAS2.33_Stanalone_19.4UAT\\download\\Monetary Authority of Singapore(ExportToExcelApplyScale)\\";
    private String expectedPath="Z:\\ProductLine\\MAS\\autoResults\\MAS2.33_Stanalone_19.4UAT\\expectation\\Monetary Authority of Singapore\\ExportToExcelApplyScale\\";
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MAS_ExportedExcel_cellValueEmpty(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MAS_ExportedExcel_cellValueEmpty.class );
    }

    /**
     * Rigourous Test :-)
     * @throws Exception
     */
    public void testMAS() throws Exception
    {
        System.out.println("love");
        String downloadFile=downPath+"MAS_0002_MAS610_B3_v1_20181231(1).xlsx";
        String expectation=expectedPath+"MAS610_B3_V1_0001_20190731_ARDisplay(1).xlsx";

        String log="";
        ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
        Boolean flag=achecker.checker(downloadFile, expectation);
        assertTrue( flag );
    }



}
