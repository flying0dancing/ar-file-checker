package com.lombardrisk.arproduct.arfilechecker;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Row;

import com.lombardrisk.arproduct.arfilechecker.IFuncChecker;
import com.lombardrisk.arproduct.arfilechecker.ValidationRuleChecker;
import com.lombardrisk.arproduct.utils.ExcelUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class BatchValRuleCheckersTest 
    extends TestCase
{
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BatchValRuleCheckersTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BatchValRuleCheckersTest.class );
    }

   
   
    public void testBatchValRuleChecker1() throws Exception
    {
    	System.out.println("love");
    	String allInOneFile="Z:\\ProductLine\\FED\\TestResults\\FED_1.14.2\\Auto\\1.14.2_AR1.16.0b75\\scenarios-result\\part2test.xlsx";
    	String allInOneSheet="Val";
    	List<Map<Integer,String>> checkers=ExcelUtil.getObjects(allInOneFile,allInOneSheet,false);
    	Boolean flag=false;
    	Map<Integer,String> map;
    	int exec_DownloadFile_Index=0,exec_ExpectationFile_Index=0,executionStatus_Index=0;
    	if(checkers!=null && !checkers.isEmpty()){
    		map=checkers.get(0);
    		for(Map.Entry<Integer,String> entry:map.entrySet())
    		{
    			if(entry.getValue().equalsIgnoreCase("Exec_ExpectationFile")){
    				exec_ExpectationFile_Index=entry.getKey();
    			}
    			if(entry.getValue().equalsIgnoreCase("Exec_DownloadFile")){
    				exec_DownloadFile_Index=entry.getKey();
    			}
    			if(entry.getValue().equalsIgnoreCase("executionStatus")){
    				executionStatus_Index=entry.getKey();
    			}
    		}
    		IFuncChecker achecker=null;
    		for(int i=1;i<checkers.size();i++){
        		map=checkers.get(i);
        		achecker=new ValidationRuleChecker(map.get(exec_DownloadFile_Index),map.get(exec_ExpectationFile_Index));
        		flag=achecker.checker();
        		map.put(executionStatus_Index, achecker.getExecutionStatus());
        		System.out.println(achecker.toString());
        	}
    		System.out.println("bbb");
    		ExcelUtil.writeObjectsToExcel(checkers, allInOneFile, allInOneSheet, false);
    	}
    	
    	
    	assertTrue( flag );
    }
    /*
    public void testBatchValRuleChecker() throws Exception
    {
    	System.out.println("love");
    	String allInOneFile="Z:\\ProductLine\\HKMA\\autoResults\\HKMA_1201_ValCheck\\scenarios-result\\archecker.xlsx";
    	String allInOneSheet="Optional1";
    	List<ValidationRuleChecker> excelCheckers=ExcelUtil.getObjects(allInOneFile,allInOneSheet,false,ValidationRuleChecker.class);
    	Boolean flag=false;
    	for(ValidationRuleChecker achecker:excelCheckers){
    		flag=achecker.checker(achecker.getExec_DownloadFile(), achecker.getExec_ExpectationFile());
    		System.out.println(achecker.toString());
    	}
    	ExcelUtil.writeObjectsToExcel(excelCheckers, allInOneFile, allInOneSheet, false,null);
    	assertTrue( flag );
    }
    */
   
}
