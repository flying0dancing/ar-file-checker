package com.lombardrisk.arproduct.arfilechecker;


import java.util.List;

import com.lombardrisk.arproduct.arfilechecker.ExcelChecker;
import com.lombardrisk.arproduct.utils.ExcelUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class BatchExcelCheckersTest 
    extends TestCase
{
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BatchExcelCheckersTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BatchExcelCheckersTest.class );
    }

   
    public void testBatchExcelChecker() throws Exception
    {
    	System.out.println("love");
    	String allInOneFile="Z:\\ProductLine\\FED\\TestResults\\FED_1.14.2\\Auto\\1.14.2_AR1.16.0b75\\scenarios-result\\part2test.xlsx";
    	String allInOneSheet="Optional1";
    	List<ExcelChecker> excelCheckers=ExcelUtil.getObjects(allInOneFile,allInOneSheet,false,ExcelChecker.class);
    	Boolean flag=false;
    	for(ExcelChecker achecker:excelCheckers){
    		flag=achecker.checker(achecker.getExec_DownloadFile(), achecker.getExec_ExpectationFile());
    		System.out.println(achecker.toString());
    	}
    	ExcelUtil.writeObjectsToExcel(excelCheckers, allInOneFile, allInOneSheet, false,null);
    	assertTrue( flag );
    }
	
   
}
