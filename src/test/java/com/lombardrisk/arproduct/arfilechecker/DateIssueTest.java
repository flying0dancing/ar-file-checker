package com.lombardrisk.arproduct.arfilechecker;


import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;
import com.lombardrisk.arproduct.utils.ExcelUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class DateIssueTest 
    extends TestCase
{
	private String downPath="Z:\\APAutomation\\results\\download\\US FED Reserve(ExportToExcelNoScale)\\";
	private String expectedPath="Z:\\APAutomation\\results\\expectation\\US FED Reserve\\ExportToExcelNoScale\\";
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DateIssueTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DateIssueTest.class );
    }

   
    public void testFED2() throws Exception
    {
    	/*
    	System.out.println("love");
    	String downloadFile=downPath+"FED_2999_FRY14ARCI_v4_20161230.xlsx";     
    	String expectation=expectedPath+"FRY14ARCI_v4_ExpectedValue_CheckExcel.xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );*/
    	
    	int a=ExcelUtil.compareDates("8/26/2015", "08/26/2015");
    	a=ExcelUtil.compareDates("8-26-2015", "08-26-2015");
    	System.out.println("love");
    	String downloadFile="Z:/APAutomation/results/expectation/US FED Reserve/ExportToExcelNoScale/FRY14QFVOHFS_v3_ARDisplay(3).xlsx";     
    	String expectation="Z:\\APAutomation\\results\\download\\US FED Reserve(ExportToExcelNoScale)\\FED_2999_FRY14QFVOHFS_v3_20170630(3).xlsx";

    	String log="";
    	ExcelChecker achecker=new ExcelChecker( downloadFile, expectation);
    	Boolean flag=achecker.checker(downloadFile, expectation);
    	assertTrue( flag );
    }
	
   
}
