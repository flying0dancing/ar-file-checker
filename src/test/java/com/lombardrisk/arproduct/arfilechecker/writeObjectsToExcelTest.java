package com.lombardrisk.arproduct.arfilechecker;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.lombardrisk.arproduct.pojo.Expected4ExportToExcel;
import com.lombardrisk.arproduct.utils.ExcelUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class writeObjectsToExcelTest 
    extends TestCase
{
	private String downPath="Z:\\APAutomation\\results\\download\\US FED Reserve(ExportToExcelNoScale)\\";
	private String expectedPath="Z:\\APAutomation\\results\\expectation\\US FED Reserve\\ExportToExcelNoScale\\";
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public writeObjectsToExcelTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( writeObjectsToExcelTest.class );
    }

   
    public void test1() throws Exception
    {
    	
    	String excelFileStr="";
    	List<Expected4ExportToExcel> it=ExcelUtil.getObjects(expectedPath+"DBO95_v1_ExpectedValue_CheckExcel.xlsx", null, true, Expected4ExportToExcel.class);
    	
    	for(Expected4ExportToExcel obj:it){
    		obj.setNotes("haha");
    	}
    	ExcelUtil.writeObjectsToExcel(it.iterator(),expectedPath+"DBO95_v1_ExpectedValue_CheckExcel.xlsx",null,true,null);
    	assertTrue( true );
    }

	
	
   
}
