package com.lombardrisk.arproduct.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lombardrisk.arproduct.poi.ExcelXlsxReader;
import com.lombardrisk.arproduct.pojo.Expected4ExportToExcel;


/**
 * many functions for treating excel, i.e. compare two excel's format, write result to excel, open/close excel,
 * @author kun shen
 *
 */
public class ExcelUtil {
	private final static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * get last row number(0-base index), ignore last empty rows.
	 * @param workBook
	 * @param sheetName
	 * @return
	 */
	public static int getLastRowNum(Workbook workBook, String sheetName) 
	{
		int amt = 0;
		Sheet sheet = getSheet(workBook,sheetName);
		amt = sheet.getLastRowNum();
		Row row =null;
		for(;amt>=0;amt--){
			row = sheet.getRow(amt);
			try
			{
				row.getCell(0).setCellType(1);
			}catch (Exception e){}
			if(row!=null && row.getCell(0)!=null && StringUtils.isNotBlank(row.getCell(0).getStringCellValue())){
				break;
			}
		}
		if(amt<=0)amt=sheet.getLastRowNum();
		return amt;
	}
	
	/***
	 * get last row number(0-base index), ignore last empty rows.
	 * @param file
	 * @param sheetName get the first sheet if null
	 * @return
	 * @throws Exception
	 */
	public static int getLastRowNum(File file, String sheetName) throws Exception
	{
		int amt = 0;
		InputStream inp = new FileInputStream(file);
		try
		{
			Workbook workBook = WorkbookFactory.create(inp);
			amt=getLastRowNum(workBook, sheetName);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(),e);
		}
		finally
		{
			try
			{
				if (inp != null)
				{
					inp.close();
				}
			}catch (Exception e)
			{
				logger.error(e.getMessage(),e);
			}
		}
		return amt;
	}
	/***
	 * delete sheet with sheetName from wb
	 * @param wb
	 * @param sheetName
	 */
	public static void deleteSheet(Workbook wb,String sheetName){
		//delete sheet named log
		int amt_sheets_expected=wb.getNumberOfSheets();
		for(int i=0;i<amt_sheets_expected;i++){
			String sheetN=wb.getSheetAt(i).getSheetName();
			if(StringUtils.isNotBlank(sheetName) && sheetN.equalsIgnoreCase(sheetName)){
				wb.removeSheetAt(i);
				break;
			}
		}
		
	}
	/***
	 * get sheet with sheetName, if sheetName doesn't exist, return first sheet.
	 * @param wb
	 * @param sheetName
	 * @return
	 */
	public static Sheet getSheet(Workbook wb,String sheetName){
		Sheet sheet=null;
		if (StringUtils.isNotBlank(sheetName))
		{ sheet = wb.getSheet(sheetName);}
		if(sheet==null)
		{ sheet = wb.getSheetAt(0);}
		return sheet;
	}
	/***
	 * get rule no like "Validation1","Cross Validation2","User Validation3","User Cross Validation4", return null if wrong shortRuleType
	 * @param shortRuleType like Val,XVal,UVal,UXVal
	 * @param ruleNumber
	 * @return
	 */
	private static String getFullRuleNo(String shortRuleType,String ruleNumber){
		String longRT=null;
		if(shortRuleType.toLowerCase().startsWith("val")){
			longRT="Validation" +ruleNumber;
		}else if(shortRuleType.toLowerCase().startsWith("xval")){
			longRT="Cross Validation" +ruleNumber;
		}else if(shortRuleType.toLowerCase().startsWith("uval")){
			longRT="User Validation" +ruleNumber;
		}else if(shortRuleType.toLowerCase().startsWith("uxval")){
			longRT="User Cross Validation" + ruleNumber;
		}
		return longRT;
	}
	/**
	 * get short rule type(Val,XVal,UVal,UXVal), return null if no matches.
	 * @param ruleNo like "Validation1","Cross Validation2","User Validation3","User Cross Validation4"
	 * @return
	 */
	private static String getShortRuleType(String ruleNo){
		String rt=null;
		if(ruleNo.startsWith("Validation")){
			rt="Val";
		}else if(ruleNo.startsWith("Cross Validation")){
			rt="XVal";
		}else if(ruleNo.startsWith("User Validation")){
			rt="UVal";
		}else if(ruleNo.startsWith("User Cross Validation")){
			rt="UXVal";
		}
		return rt;
	}
	/***
	 * get instance from exported excel's Message column
	 * @param message
	 * @return "All Instance" if no PageInstance in Message column
	 */
	private static String getInstanceFromExportedExcel(String message){
		String str="All Instance";
		if(StringUtils.isNotBlank(message) && message.contains("[PageInstance")){
			str=message.replace("\n", "").replaceAll("\\[PageInstance:(.+?)\\].*","$1");
		}
		return str;
	}
	/**
	 * get rowID from exported excel's Message column
	 * @param message
	 * @return return "" if no Row ID
	 */
	private static String getRowIDFromExportedExcel(String message){
		String str="";
		if(StringUtils.isNotBlank(message) && message.contains("[Row")){
			str=message.replace("\n", "").replaceAll(".*?\\[Row:(.+?)\\].*","$1");
		}
		return str;
	}
	/***
	 * deprecated, unable to handler bigger excel
	 * @param fileFullName_expected
	 * @param sheetName_expected
	 * @param fileFullName_exported
	 * @return
	 */
	public static String writeValidationRulesResult0(String fileFullName_expected, String sheetName_expected,String fileFullName_exported)
	{
		String flagStr="pass";
		Workbook wb_expected, wb_exported;
		//InputStream inp_expected;
		//InputStream inp_exported;
		//FileOutputStream out_expected, out_exported;
		String ewTestLog="log";
		try
		{
			File file_expected=new File(fileFullName_expected);
			if(!file_expected.isFile()){
				flagStr="error: File Not Found "+fileFullName_expected;
				return flagStr;
			}
			File file_exported=new File(fileFullName_exported);
			if(!file_exported.isFile()){
				flagStr="error: File Not Found "+fileFullName_exported;
				return flagStr;
			}
			wb_expected=openWorkbook(file_expected);
			wb_exported=openWorkbook(file_exported);
			logger.info("exported file(need to be checked):"+fileFullName_exported);
			logger.info("expected file:"+fileFullName_expected);
			//delete sheet named log
			deleteSheet(wb_expected,ewTestLog);
			Sheet sheet_expected = null, sheet_exported=null;
			int amt_expected = 0,amt_exported=0;
			
			amt_expected = getLastRowNum(wb_expected,sheetName_expected);
			amt_exported = getLastRowNum(wb_exported,null);
			sheet_expected = getSheet(wb_expected,sheetName_expected);
			sheet_exported=wb_exported.getSheetAt(0);
			if(sheet_exported==null || sheet_expected==null){
				flagStr="error: cannot get first sheet ";
				return flagStr;
			}
			ExcelUtil.saveWorkbook(file_exported, wb_exported);
			Row row_expected=null,row_exported=null;
			String check_expected=null, ruleType_expected=null, ruleNo_expected=null,ruleTypeNo_expected=null, instance_expected = null, rowIdStr_expected = null, status_expected = null, ruleMsg_expected = null;
			int address1;
			Boolean search;
			String no_A=null,status_E = null, msg_G = null, rowID = null,instance_D = null, checked_T=null,rst=null, instance_G;//in exported file
			logger.info("Verify row:1 skip head row (expectedValue vs actualValue)");
			for(long i=1;i<=amt_expected;i++){
				//initial running
				search=true;
				rst=null;
				address1=-1;
				//part1 set expected info
				row_expected=sheet_expected.getRow((int) i);
				if(row_expected==null)continue;
				check_expected=getCellValue_expected(row_expected,1);//column B
				if(!check_expected.equalsIgnoreCase("y")){continue;}

				//get expected info
				ruleNo_expected=getCellValue_expected(row_expected,3);//column D
				ruleType_expected=getCellValue_expected(row_expected,2);//column C
				ruleTypeNo_expected=getFullRuleNo(ruleType_expected,ruleNo_expected);
				if(ruleTypeNo_expected==null){
					logger.error("Verify row:"+(i+1)+" fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal");
					row_expected.createCell(7).setCellValue("fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal");//column H
					row_expected.createCell(12).setCellValue("fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal");//column M
					flagStr="fail";
					continue;
				}

				instance_expected=getCellValue_expected(row_expected,4);//column E
				rowIdStr_expected=getCellValue_expected(row_expected,5);//column F
				status_expected=getCellValue_expected(row_expected,6);//column G
				ruleMsg_expected=getCellValue_expected(row_expected,8);//column I
				//find rule from exported excel
				address1=findCell(sheet_exported,ruleTypeNo_expected,1, 0, amt_exported,0);
				if(address1<0){
					logger.error("Verify row:"+(i+1)+" fail to find");
					row_expected.createCell(7).setCellValue("fail to find");//column H
					row_expected.createCell(12).setCellValue("fail to find");//column M
					flagStr="fail";
					continue;
				}
				
				while(address1>0 && search){
					search = false;//reset search for false means no need to search again.
                    Boolean asiaFlag = false;
                    status_E = null; msg_G = null; rowID = null; instance_D = null;rst=null;instance_G="";//clear info
                    row_exported=sheet_exported.getRow(address1);
                    instance_D=getCellValue_expected(row_exported,3);//column D
                    status_E=getCellValue_expected(row_exported,4);//column E
                    msg_G=getCellValue_expected(row_exported,6);//column G
                    if(StringUtils.isNotBlank(msg_G)){
                    	if(msg_G.contains("Row:")){
                    		rowID=msg_G.replace("\n", "").replaceAll(".*\\[Row:(.+?)\\].*", "$1");
                    	}
                    	if( msg_G.contains("PageInstance:")){
                        	instance_G=msg_G.replace("\n", "").replaceAll("\\[PageInstance:(.+?)\\].*", "$1");
                        }
                    }
                    if(instance_expected.equals("") || instance_expected.equals("0") || instance_expected.equalsIgnoreCase("All Instance") || instance_G.equals("")){
                    	if(StringUtils.isNoneBlank(rowIdStr_expected)){
                    		if(rowID==null){
                    			asiaFlag = true;//used for asia, for wrong setting RowID(For extendgrid)=1
                    		}else{
                    			if(!rowID.equals(rowIdStr_expected)){
                   				 search = true;
                    			}
                    		}
                    	}
                    }else{
                    	if(msg_G.toLowerCase().startsWith("[pageinstance")){
                    		if(instance_expected.equalsIgnoreCase("Each Instance")){
                    			if(instance_D.equalsIgnoreCase(instance_expected) || instance_G.equalsIgnoreCase(instance_expected)){
                    				if(!msg_G.equalsIgnoreCase(ruleMsg_expected)){
                    					search = true;
                    				}
                    			}
                    		}else{
                    			if(instance_expected.equalsIgnoreCase(instance_G)){
                    				if(StringUtils.isNoneBlank(rowIdStr_expected) && rowID!=null){
                    					if(!rowID.equals(rowIdStr_expected)){
                    						search = true;
                    					}
                    				}else{
                    					if((StringUtils.isBlank(rowIdStr_expected) && rowID!=null) || (StringUtils.isNotBlank(rowIdStr_expected) && rowID==null)){
                    						search = true;
                    						break;//break for fail
                    					}
                    				}
                    			}else{search = true;}
                    		}
                    	}
                    }

                    if(!search && ( StringUtils.isBlank(rowIdStr_expected) ||  StringUtils.equals(rowID, rowIdStr_expected) || asiaFlag )){
						row_expected.createCell(7).setCellValue(status_E);//column H
						if(StringUtils.isNotBlank(msg_G)){
							row_expected.createCell(9).setCellValue(msg_G);//column J
						}
						row_exported.createCell(19).setCellValue("c");// column T, add flag for checked row
						rst=setValRuleCompareRst(status_expected,status_E,ruleMsg_expected,msg_G);
						logger.info("Verify row:"+(i+1)+" "+rst);
						row_expected.createCell(12).setCellValue(rst);//column M
						if(rst.equals("fail")){flagStr="fail";}
						break;
                    }
                    address1=findCell(sheet_exported,ruleTypeNo_expected,address1+1, 0, amt_exported,0); 
				}
				if(search || rst==null){
					flagStr="fail";
					logger.error("Verify row:"+(i+1)+" "+flagStr);
					row_expected.createCell(7).setCellValue(flagStr);//column H
					row_expected.createCell(12).setCellValue(flagStr);//column M
					
				}
				
				Runtime.getRuntime().gc();
			}
			//saved excels
			ExcelUtil.saveWorkbook(file_expected, wb_expected);
			ExcelUtil.saveWorkbook(file_exported, wb_exported);
			
			Sheet log_expected = null;
			int uncheckederrNo=0;
			String shortRuleType=null;
			for(long i=1;i<=amt_exported;i++){//skip head row
				row_exported=sheet_exported.getRow((int)i);
				no_A=null;checked_T=null;status_E = null; //clear info
				no_A=getCellValue_expected(row_exported,0);//column A
				status_E=getCellValue_expected(row_exported,4);//column E
				checked_T=getCellValue_expected(row_exported,19);//column T
				//if(checked_T.equals("c")){row_exported.removeCell(row_exported.getCell(19));}
				if(StringUtils.isNotBlank(no_A) && !checked_T.equals("c") && !status_E.equalsIgnoreCase("pass")){
					msg_G = null; rowID = null; instance_D = null;//clear info
					if(uncheckederrNo==0){
						log_expected = wb_expected.createSheet(ewTestLog);
						row_expected=log_expected.createRow(uncheckederrNo);
						//set head row
						String[] columnNames=new String[]{"CaseID(QC)","Check","RuleType","RuleID","Instance","RowID(For extendgrid)","Expected Status","Acctual Status","Expected Error","Acctual Error","Expected Cell Counts","Acctual Cell Counts","Test Result"};
						for(int c=0;c<=12;c++){
							row_expected.createCell(c).setCellValue(columnNames[c]);
						}
						log_expected.setAutoFilter(CellRangeAddress.valueOf("A1:M1"));
					}else if(uncheckederrNo==1){
						log_expected = wb_expected.getSheet(ewTestLog);
					}
					uncheckederrNo++;
					row_expected=log_expected.createRow(uncheckederrNo);
					//set value
					shortRuleType=getShortRuleType(no_A);
					msg_G=getCellValue_expected(row_exported,6);//column G
					instance_D=getInstanceFromExportedExcel(msg_G);
					rowID=getRowIDFromExportedExcel(msg_G);
					row_expected.createCell(1).setCellValue("Y");//Check
					row_expected.createCell(2).setCellValue(shortRuleType);//RuleType
					row_expected.createCell(3).setCellValue(no_A.replaceAll(".*?(\\d+)", "$1"));//RuleID
					
					row_expected.createCell(4).setCellValue(instance_D);//Instance
					row_expected.createCell(5).setCellValue(rowID);//RowID
					row_expected.createCell(6).setCellValue(status_E);//Expected Status
					row_expected.createCell(8).setCellValue(msg_G);//Expected Error
				}
			}
			
			//saved excels
			ExcelUtil.saveWorkbook(file_expected, wb_expected);
			ExcelUtil.saveWorkbook(file_exported, wb_exported);
			
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(),e);
			flagStr="error\n"+e.getMessage();
		}
		Runtime.getRuntime().gc();
		return flagStr;
	}
	public static String writeValidationRulesResult(String fileFullName_expected, String sheetName_expected,String fileFullName_exported)
	{
		String flagStr="pass";
		Workbook wb_expected;
		String ewTestLog="log";
		List<List<String>> list_exported;
		List<String> row_exported;
		try
		{
			File file_expected=new File(fileFullName_expected);
			if(!file_expected.isFile()){
				flagStr="error: File Not Found "+fileFullName_expected;
				return flagStr;
			}
			File file_exported=new File(fileFullName_exported);
			if(!file_exported.isFile()){
				flagStr="error: File Not Found "+fileFullName_exported;
				return flagStr;
			}
			wb_expected=openWorkbook(file_expected);

			ExcelXlsxReader excelXlsxReader=new ExcelXlsxReader();
			list_exported=excelXlsxReader.processOneSheet(fileFullName_exported, null);
			String exportedFileV="1.16.1";
			if(list_exported.get(0).get(0).equalsIgnoreCase("Rule Type")){
				//validation rules' export file are updated. started from agile reporter v1.16.2
				exportedFileV="1.16.2";
			}
            String sheet_exported=excelXlsxReader.getSheetName();
			
			logger.info("exported file(need to be checked):"+fileFullName_exported);
			logger.info("expected file:"+fileFullName_expected);
			//delete sheet named log
			deleteSheet(wb_expected,ewTestLog);
			Sheet sheet_expected = null;
			Row row_expected=null;
			int amt_expected = 0,amt_exported=0;
			
			amt_expected = getLastRowNum(wb_expected,sheetName_expected);
			//amt_exported = getLastRowNum(wb_exported,null);
			amt_exported=list_exported.size();
			sheet_expected = getSheet(wb_expected,sheetName_expected);
			//sheet_exported=wb_exported.getSheetAt(0);
			if(sheet_expected==null){
				flagStr="error: cannot get first sheet ";
				return flagStr;
			}
			
			String check_expected=null, ruleType_expected=null, ruleNo_expected=null,ruleTypeNo_expected=null, instance_expected = null, rowIdStr_expected = null, status_expected = null, ruleMsg_expected = null;
			Map<Integer,List<String>> addresses;
			Iterator<Map.Entry<Integer, List<String>>> entries;
			Entry<Integer, List<String>> map;
			Boolean search;
			String no_A=null,status_E = null, msg_G = null, rowID = null,instance_D = null, checked_T=null,rst=null, instance_G;//in exported file
			logger.info("Verify row:1 skip head row (expectedValue vs actualValue)");
			for(long i=1;i<=amt_expected;i++){
				//initial running
				search=true;
				rst=null;
				//part1 set expected info
				row_expected=sheet_expected.getRow((int) i);
				if(row_expected==null)continue;
				check_expected=getCellValue_expected(row_expected,1);//column B
				if(!check_expected.equalsIgnoreCase("y")){continue;}

				//get expected info
				ruleNo_expected=getCellValue_expected(row_expected,3);//column D
				ruleType_expected=getCellValue_expected(row_expected,2);//column C
				ruleTypeNo_expected=getFullRuleNo(ruleType_expected,ruleNo_expected);
				if(ruleTypeNo_expected==null && exportedFileV.equals("1.16.1")){
					logger.error("Verify row:"+(i+1)+" fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal, Cross-Val");
					row_expected.createCell(7).setCellValue("fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal, Cross-Val");//column H
					row_expected.createCell(12).setCellValue("fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal, Cross-Val");//column M
					flagStr="fail";
					continue;
				}

				instance_expected=getCellValue_expected(row_expected,4);//column E
				rowIdStr_expected=getCellValue_expected(row_expected,5);//column F
				status_expected=getCellValue_expected(row_expected,6);//column G
				ruleMsg_expected=getCellValue_expected(row_expected,8);//column I
				//find rule from exported excel
				if(exportedFileV.equals("1.16.1")){
					addresses=findCell(list_exported,ruleTypeNo_expected);
				}else{
					addresses=findCell(list_exported,ruleType_expected,ruleNo_expected);
				}
				
				entries = addresses.entrySet().iterator(); 
				if(addresses==null || addresses.size()<=0){
					logger.error("Verify row:"+(i+1)+" fail to find");
					row_expected.createCell(7).setCellValue("fail to find");//column H
					row_expected.createCell(12).setCellValue("fail to find");//column M
					flagStr="fail";
					continue;
				}
				
				while(entries.hasNext() && search){
					search = false;//reset search for false means no need to search again.
                    Boolean asiaFlag = false;
                    status_E = null; msg_G = null; rowID = null; instance_D = null;rst=null;instance_G="";//clear info
                    map=entries.next();
                    row_exported=map.getValue();
                    if(exportedFileV.equals("1.16.1")){
                    	instance_D=row_exported.get(3);//column D
                        status_E=row_exported.get(4);//column E
                        msg_G=row_exported.get(6);//column G
                    }else{
                    	instance_D=row_exported.get(4);//column E
                        status_E=row_exported.get(5);//column F
                        msg_G=row_exported.get(7);//column H
                    }
                    
                    if(StringUtils.isNotBlank(msg_G)){
                    	if(msg_G.contains("Row:")){
                    		rowID=msg_G.replace("\n", "").replaceAll(".*\\[Row:(.+?)\\].*", "$1");
                    	}
                    	if( msg_G.contains("PageInstance:")){
                        	instance_G=msg_G.replace("\n", "").replaceAll("\\[PageInstance:(.+?)\\].*", "$1");
                        }
                    }
                    if(instance_expected.equals("") || instance_expected.equals("0") || instance_expected.equalsIgnoreCase("All Instance") || instance_G.equals("")){
                    	if(StringUtils.isNoneBlank(rowIdStr_expected)){
                    		if(rowID==null){
                    			asiaFlag = true;//used for asia, for wrong setting RowID(For extendgrid)=1
                    		}else{
                    			if(!rowID.equals(rowIdStr_expected)){
                   				 search = true;
                    			}
                    		}
                    	}
                    }else{
                    	if(msg_G.toLowerCase().startsWith("[pageinstance")){
                    		if(instance_expected.equalsIgnoreCase("Each Instance")){
                    			if(instance_D.equalsIgnoreCase(instance_expected) || instance_G.equalsIgnoreCase(instance_expected)){
                    				if(!msg_G.equalsIgnoreCase(ruleMsg_expected)){
                    					search = true;
                    				}
                    			}
                    		}else{
                    			if(instance_expected.equalsIgnoreCase(instance_G)){
                    				if(StringUtils.isNoneBlank(rowIdStr_expected) && rowID!=null){
                    					if(!rowID.equals(rowIdStr_expected)){
                    						search = true;
                    					}
                    				}else{
                    					if((StringUtils.isBlank(rowIdStr_expected) && rowID!=null) || (StringUtils.isNotBlank(rowIdStr_expected) && rowID==null)){
                    						search = true;
                    						break;//break for fail
                    					}
                    				}
                    			}else{search = true;}
                    		}
                    	}
                    }

                    if(!search && ( StringUtils.isBlank(rowIdStr_expected) ||  StringUtils.equals(rowID, rowIdStr_expected) || asiaFlag )){
						row_expected.createCell(7).setCellValue(status_E);//column H
						if(StringUtils.isNotBlank(msg_G)){
							row_expected.createCell(9).setCellValue(msg_G);//column J
						}
						//add flag at column U for checked row
						for(int colIndex=row_exported.size();colIndex<20;colIndex++){
							row_exported.add("");
						}
						row_exported.add("checkedForValidation");//add flag at column U for checked row
						list_exported.set(map.getKey(), row_exported);//add flag at column U for checked row
						rst=setValRuleCompareRst(status_expected,status_E,ruleMsg_expected,msg_G);
						logger.info("Verify row:"+(i+1)+" "+rst);
						row_expected.createCell(12).setCellValue(rst);//column M
						if(rst.equals("fail")){flagStr="fail";}
						break;
                    }
                    
				}
				if(search || rst==null){
					flagStr="fail";
					logger.error("Verify row:"+(i+1)+" "+flagStr);
					row_expected.createCell(7).setCellValue(flagStr);//column H
					row_expected.createCell(12).setCellValue(flagStr);//column M
				}
				
			}
			
			
			Sheet log_expected = null;
			int uncheckederrNo=0;
			String shortRuleType=null;
			for(int i=1;i<amt_exported;i++){//skip head row
				row_exported=list_exported.get(i);
				no_A=null;checked_T=null;status_E = null; //clear info
				no_A=row_exported.get(0);//column A
				if(exportedFileV.equals("1.16.1")){
					status_E=row_exported.get(4);//column E
				}else{
					status_E=row_exported.get(5);//column F
				}
				
				checked_T=row_exported.get(row_exported.size()-1);//column U
				//if(checked_T.equals("c")){row_exported.removeCell(row_exported.getCell(19));}
				if(StringUtils.isNotBlank(no_A) && !checked_T.equals("checkedForValidation") && !status_E.equalsIgnoreCase("pass") && !status_E.equalsIgnoreCase("Ignored")){
					msg_G = null; rowID = null; instance_D = null;//clear info
					if(uncheckederrNo==0){
						flagStr="fail";
						log_expected = wb_expected.createSheet(ewTestLog);
						row_expected=log_expected.createRow(uncheckederrNo);
						//set head row
						String[] columnNames=new String[]{"CaseID(QC)","Check","RuleType","RuleID","Instance","RowID(For extendgrid)","Expected Status","Acctual Status","Expected Error","Acctual Error","Expected Cell Counts","Acctual Cell Counts","Test Result"};
						for(int c=0;c<=12;c++){
							row_expected.createCell(c).setCellValue(columnNames[c]);
						}
						log_expected.setAutoFilter(CellRangeAddress.valueOf("A1:M1"));
					}else if(uncheckederrNo==1){
						log_expected = wb_expected.getSheet(ewTestLog);
					}
					//add flag at column U for checked row
					for(int colIndex=row_exported.size();colIndex<20;colIndex++){
						row_exported.add("");
					}
					row_exported.add("checkedForValidation");//add flag at column U for checked row
					list_exported.set(i, row_exported);//add flag at column U for checked row
					uncheckederrNo++;
					row_expected=log_expected.createRow(uncheckederrNo);
					//set value
					
					if(exportedFileV.equals("1.16.1")){
						shortRuleType=getShortRuleType(no_A);
						msg_G=row_exported.get(6);//column G
						row_expected.createCell(2).setCellValue(shortRuleType);//RuleType
						row_expected.createCell(3).setCellValue(no_A.replaceAll(".*?(\\d+)", "$1"));//RuleID
					}else{
						msg_G=row_exported.get(7);//column H
						row_expected.createCell(2).setCellValue(no_A.replace("Reg ", ""));//RuleType
						row_expected.createCell(3).setCellValue(row_exported.get(1));//RuleID
					}
					
					instance_D=getInstanceFromExportedExcel(msg_G);
					rowID=getRowIDFromExportedExcel(msg_G);
					row_expected.createCell(1).setCellValue("Y");//Check
					
					
					row_expected.createCell(4).setCellValue(instance_D);//Instance
					row_expected.createCell(5).setCellValue(rowID);//RowID
					row_expected.createCell(6).setCellValue(status_E);//Expected Status
					row_expected.createCell(8).setCellValue(msg_G);//Expected Error
				}
			}
			
			//saved excels
			ExcelUtil.saveWorkbook(file_expected, wb_expected);
			String name=Helper.getFileNameWithoutSuffix(fileFullName_exported);
			
			ExcelUtil.saveWorkbook(Helper.getParentPath(fileFullName_exported)+name+"_checked.xlsx",sheet_exported, list_exported);
			
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(),e);
			flagStr="error\n"+e.getMessage();
		}
		System.gc();
		return flagStr;
	}
	
	/**
	 * compare these status and message, return "pass" or "fail"
	 * @param status_ex
	 * @param status_actual
	 * @param message_ex
	 * @param message_actual if message_actual contains "[PageInstance:1]" but message_ex doesn't, this will ignore "[PageInstance:1]"
	 * @return
	 */
	private static String setValRuleCompareRst(String status_ex,String status_actual,String message_ex,String message_actual){
		String flag="fail";
		if(StringUtils.equalsIgnoreCase(status_ex, status_actual)){
			if(StringUtils.equalsIgnoreCase(status_ex, "pass") || StringUtils.equalsIgnoreCase(message_ex, message_actual)){
				flag="pass";//if expectedStatus is Pass, it will ignore expectedError checking.
			}else{
				if(StringUtils.isNotBlank(message_ex)){
					String a=message_actual.replace(message_ex, "").replaceAll("\\s", "");
					if(StringUtils.equalsIgnoreCase(a, "[PageInstance:1]") || a.equals("")){
						flag="pass";
						return flag;
					}
					//ignore blank
					String ex=message_ex.replace(" ", "").replace("[PageInstance:1]", "");
					a=message_actual.replace(" ", "").replace("[PageInstance:1]", "");
					if(ex.equalsIgnoreCase(a)){
						flag="pass";//after ignore blank
					}
				}
			}
		}
		return flag;
	}
	
	

	/**
	 * It can handler two formatted excel,"UIDisplay and exporttoexcel"
	 * @param fileFullName_expected
	 * @param sheetName_expected
	 * @param fileFullName_exported
	 * @param csvUtil
	 * @return
	 */
	public static String writeExport2ExcelResult(String fileFullName_expected, String sheetName_expected,String fileFullName_exported,CsvDBUtil csvUtil)
	{
		String flagStr="pass";
		Workbook  wb_exported;
		//InputStream inp_expected;
		InputStream inp_exported;
		
		try
		{
			File file_expected=new File(fileFullName_expected);
			if(!file_expected.isFile()){
				flagStr="error: File Not Found "+fileFullName_expected;
				return flagStr;
			}
			File file_exported=new File(fileFullName_exported);
			if(!file_exported.isFile()){
				flagStr="error: File Not Found "+fileFullName_exported;
				return flagStr;
			}
			
			inp_exported = new FileInputStream(file_exported);
			wb_exported=WorkbookFactory.create(inp_exported);
			inp_exported.close();
			logger.info("exported cell info stored in:"+csvUtil.getCsvPath()+csvUtil.getTableName()+".csv");
			logger.info("exported file(need to be checked):"+fileFullName_exported);
			logger.info("expected file:"+fileFullName_expected);
			List<Expected4ExportToExcel> it=getObjects(fileFullName_expected, sheetName_expected, false, Expected4ExportToExcel.class);
			//----
			Sheet sheet_exported=null;
			
			//part1 set expected info
			String cellName_expected = null, instance_expected = null, expectedValue_expected = null, actualValue_expected = null;
			int rowId_expected = 0;
			String rowIdStr_expected = null;//add rowStr for rowId is a string
			//part2 set search info
			String sheetName_of_cellInfo = null, colName_of_cellInfo = null, cellID_of_cellInfo = null, instance_of_cellInfo = null;
			int rowId_of_cellInfo = 0;
			List<String> cellInfo=null;
			//part3
			int amt_exported=0,rowIndex_exported,colIndex_exported;
			String aNamedAddress=null;
			logger.info("Verify row:1 skip head row (expectedValue vs actualValue)");
			//----
			int i=1;
			for(Expected4ExportToExcel expected_obj:it){
				//part1 set expected info
				//clear setting
				expected_obj.setNotes(null);
				expected_obj.setAcctualValue(null);
				expected_obj.setTestResult(null);
				cellName_expected=expected_obj.getCellName();//column A
				cellName_expected=cellName_expected.replaceAll("^_{1,}(.*)", "$1");
				rowIdStr_expected=expected_obj.getRowID();
				rowId_expected = 0;
				if(StringUtils.isNotBlank(rowIdStr_expected) && rowIdStr_expected.matches("[0-9]+")){
					rowId_expected=Integer.parseInt(rowIdStr_expected);
					rowIdStr_expected=null;
				}
				instance_expected=expected_obj.getInstance();//column C
				expectedValue_expected=expected_obj.getExpectedValue();//column D
				if(StringUtils.isBlank(expectedValue_expected)){expectedValue_expected="";}
				//part 2
				cellInfo=csvUtil.getCellInfo(cellName_expected, instance_expected);
				if(cellInfo!=null && cellInfo.size()>0){
					cellID_of_cellInfo=cellInfo.get(0);
					sheetName_of_cellInfo=cellInfo.get(1);
					instance_of_cellInfo=cellInfo.get(2);
					rowId_of_cellInfo=Integer.parseInt(cellInfo.get(3));
					colName_of_cellInfo=cellInfo.get(4);
				}else{
					logger.error("Verify row:"+(i+1)+" cannot find expected cell info");
					expected_obj.setTestResult("fail to find this cell");//column F testResult
					flagStr="fail";
					i++;
					continue;
				}
				//part 3
				if(rowId_expected>1){
					rowId_of_cellInfo = rowId_of_cellInfo + rowId_expected - 1; // used for extend grid, calculate id of row by the first cell.
				}
				
				if(StringUtils.isBlank(instance_of_cellInfo)){
					sheet_exported=wb_exported.getSheet(sheetName_of_cellInfo);
					amt_exported=getLastRowNum(wb_exported,sheetName_of_cellInfo);
				}else{
					String sheet_tmp=sheetName_of_cellInfo+"|"+instance_of_cellInfo;
					sheet_exported=wb_exported.getSheet(sheet_tmp);
					amt_exported=getLastRowNum(wb_exported,sheet_tmp);
				}
				
				aNamedAddress="$"+colName_of_cellInfo+"$"+rowId_of_cellInfo; //$A$3
				AreaReference[] arefs=AreaReference.generateContiguous(aNamedAddress);
				CellReference crefs=arefs[0].getFirstCell();
				rowIndex_exported=crefs.getRow();
				colIndex_exported=crefs.getCol();
				if(StringUtils.isNotBlank(rowIdStr_expected)){
					rowIndex_exported=findCell(sheet_exported,rowIdStr_expected,rowIndex_exported,0,amt_exported,colIndex_exported);
				}
				if(rowIndex_exported<0){
					logger.error("Verify row:"+(i+1)+" cannot find exported cell info");
					expected_obj.setTestResult("fail to find this cell");//column F testResult
					flagStr="fail";
					i++;
					continue;
				}
				Row row_exported=sheet_exported.getRow(rowIndex_exported);
				actualValue_expected=getCellValue_expected(row_exported,colIndex_exported);
				expected_obj.setAcctualValue(actualValue_expected);;//column E
				
				if(cellID_of_cellInfo!=null && !cellID_of_cellInfo.equalsIgnoreCase(expected_obj.getCellName())){
					expected_obj.setNotes("actual CellName:"+cellID_of_cellInfo);//column G
				}

				if(actualValue_expected.equalsIgnoreCase(expectedValue_expected)){
					expected_obj.setTestResult("pass");//column F testResult
					logger.info("Verify row:"+(i+1)+" pass");
				}else{
					int days=ExcelUtil.compareDates(actualValue_expected, expectedValue_expected);
					if(days==0){
						expected_obj.setTestResult("pass");//column F testResult
						expected_obj.setNotes("similiar date format:"+expectedValue_expected+" vs "+actualValue_expected);
						logger.info("Verify row:"+(i+1)+" pass "+expectedValue_expected+" vs "+actualValue_expected);
					}else{
						expected_obj.setTestResult("fail");
						logger.info("Verify row:"+(i+1)+" fail "+expectedValue_expected+" vs "+actualValue_expected);
						flagStr="fail";
					}
				}
				i++;
			}
			
			//saved excel
			writeObjectsToExcel(it,fileFullName_expected,sheetName_expected,true,null);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(),e);
			flagStr="error\n"+e.getMessage();
		}
		return flagStr;
	}	
	
	/***
	 * wirte "export to excel" result, compare rows one by one
	 * @param fileFullName_expected
	 * @param sheetName_expected null, means first sheet
	 * @param fileFullName_exported
	 * @param csvUtil you need to create a new instance of CsvDBUtil before invoking this method
	 * @return
	 */
	public static String writeExport2ExcelRst(String fileFullName_expected, String sheetName_expected,String fileFullName_exported,CsvDBUtil csvUtil){
		String flagStr=null;
		File file_expected=new File(fileFullName_expected);
		File file_exported=new File(fileFullName_exported);
		if(file_expected.exists() && file_expected.isFile()){
			if(file_exported.exists() && file_exported.isFile()){
				flagStr=writeExport2ExcelRst( file_expected,  sheetName_expected, file_exported, csvUtil);
			}else{
				flagStr="error: File Not Found "+fileFullName_exported;
			}
		}else{
			flagStr="error: File Not Found "+fileFullName_expected;
		}
		return flagStr;
	}
	
	/**
	 * 
	 * @param file_expected
	 * @param sheetName_expected
	 * @param file_exported
	 * @param csvUtil
	 * @return
	 */
	public static String writeExport2ExcelRst(File file_expected, String sheetName_expected,File file_exported,CsvDBUtil csvUtil)
	{
		String flagStr="pass";
		int amt_expected = 0;
		Workbook wb_expected, wb_exported;
		InputStream inp_expected;
		InputStream inp_exported;
		FileOutputStream out_expected;
		
		try
		{
			inp_expected = new FileInputStream(file_expected);
			wb_expected=WorkbookFactory.create(inp_expected);
			inp_expected.close();
			inp_exported = new FileInputStream(file_exported);
			wb_exported=WorkbookFactory.create(inp_exported);
			inp_exported.close();
			logger.info("exported file(need to be checked):"+file_exported.toString());
			logger.info("expected file:"+file_expected.toString());
			Sheet sheet_expected = null, sheet_exported=null;
			if (sheetName_expected != null)
			{ sheet_expected = wb_expected.getSheet(sheetName_expected);}
			if(sheet_expected==null)
			{ sheet_expected = wb_expected.getSheetAt(0);}
			amt_expected = getLastRowNum(wb_expected,sheetName_expected);
			Row row_expected=null;
			Cell cell_expected=null;
			Cell cell_comments=null;
			//part1 set expected info
			String cellName_O_expected=null, cellName_expected = null, instance_expected = null, expectedValue_expected = null, actualValue_expected = null;
			int rowId_expected = 0;
			String rowIdStr_expected = null;//add rowStr for rowId is a string
			//part2 set search info
			String sheetName_of_cellInfo = null, colName_of_cellInfo = null, cellID_of_cellInfo = null, instance_of_cellInfo = null;
			int rowId_of_cellInfo = 0;
			List<String> cellInfo=null;
			//part3
			int amt_exported=0,rowIndex_exported,colIndex_exported;
			String aNamedAddress=null;
			logger.info("Verify row:1 skip head row (expectedValue vs actualValue)");
			for(int i=1;i<=amt_expected;i++){
				//clear setting
				cell_comments=null;
				//part1 set expected info
				row_expected=sheet_expected.getRow(i);
				if(row_expected==null)continue;
				
				//--
				cellName_expected=getCellValue_expected(row_expected,0);//column A
				cellName_O_expected=cellName_expected;
				if(StringUtils.isBlank(cellName_expected)) continue;
				cellName_expected=cellName_expected.replaceAll("^_{1,}(.*)", "$1");
				rowIdStr_expected=getCellValue_expected(row_expected,1);//column B
				rowId_expected = 0;
				if(rowIdStr_expected.matches("[0-9]+")){
					rowId_expected=Integer.parseInt(rowIdStr_expected);
					rowIdStr_expected=null;
				}
				instance_expected=getCellValue_expected(row_expected,2);//column C
				expectedValue_expected=getCellValue_expected(row_expected,3).trim();//column D
				
				//part 2
				cellInfo=csvUtil.getCellInfo(cellName_expected, instance_expected);
				if(cellInfo!=null && cellInfo.size()>0){
					cellID_of_cellInfo=cellInfo.get(0);
					sheetName_of_cellInfo=cellInfo.get(1);
					instance_of_cellInfo=cellInfo.get(2);
					rowId_of_cellInfo=Integer.parseInt(cellInfo.get(3));
					colName_of_cellInfo=cellInfo.get(4);
				}else{
					logger.error("cannot find expected cell in "+csvUtil.getCsvPath()+csvUtil.getTableName()+".csv");
					row_expected.createCell(5).setCellValue("fail to find this cell");//column F
					flagStr="fail";
					continue;
				}
				//part 3
				if(rowId_expected>1){
					rowId_of_cellInfo = rowId_of_cellInfo + rowId_expected - 1; // used for extend grid, calculate id of row by the first cell.
				}
				
				if(StringUtils.isBlank(instance_of_cellInfo)){
					sheet_exported=wb_exported.getSheet(sheetName_of_cellInfo);
					amt_exported=getLastRowNum(wb_exported,sheetName_of_cellInfo);
				}else{
					String sheet_tmp=sheetName_of_cellInfo+"|"+instance_of_cellInfo;
					sheet_exported=wb_exported.getSheet(sheet_tmp);
					amt_exported=getLastRowNum(wb_exported,sheet_tmp);
				}
				
				aNamedAddress="$"+colName_of_cellInfo+"$"+rowId_of_cellInfo; //$A$3
				AreaReference[] arefs=AreaReference.generateContiguous(aNamedAddress);
				CellReference crefs=arefs[0].getFirstCell();
				rowIndex_exported=crefs.getRow();
				colIndex_exported=crefs.getCol();
				if(StringUtils.isNotBlank(rowIdStr_expected)){
					rowIndex_exported=findCell(sheet_exported,rowIdStr_expected,rowIndex_exported,0,amt_exported,colIndex_exported);
				}
				if(rowIndex_exported<0){
					logger.error("cannot find expected cell in "+csvUtil.getCsvPath()+csvUtil.getTableName()+".csv");
					row_expected.createCell(5).setCellValue("fail to find this cell");//column F
					flagStr="fail";
					continue;
				}
				Row row_exported=sheet_exported.getRow(rowIndex_exported);
				actualValue_expected=getCellValue_expected(row_exported,colIndex_exported);
				cell_expected=getCell(row_expected, 4);//column E
				cell_expected.setCellValue(actualValue_expected);
				
				cell_comments=getCell(row_expected, 6);//column G
				if(cellID_of_cellInfo!=null && !cellID_of_cellInfo.equalsIgnoreCase(cellName_O_expected)){
					cell_comments.setCellValue("actual CellName:"+cellID_of_cellInfo);
				}
				cell_expected=getCell(row_expected, 5);//column F
				if(actualValue_expected.equalsIgnoreCase(expectedValue_expected)){
					cell_expected.setCellValue("pass");
					logger.info("Verify row:"+(i+1)+" pass");
				}else{
					int days=ExcelUtil.compareDates(actualValue_expected, expectedValue_expected);
					if(days==0){
						cell_expected.setCellValue("pass");
						cell_comments.setCellValue("similiar date format:"+expectedValue_expected+" vs "+actualValue_expected);
						logger.info("Verify row:"+(i+1)+" pass "+expectedValue_expected+" vs "+actualValue_expected);
					}else{
						cell_expected.setCellValue("fail");
						logger.info("Verify row:"+(i+1)+" fail "+expectedValue_expected+" vs "+actualValue_expected);
						flagStr="fail";
					}
				}
				
			}
			//saved excel
			out_expected = new FileOutputStream(file_expected);
			wb_expected.write(out_expected);
			out_expected.flush();
			out_expected.close();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(),e);
			flagStr="error\n"+e.getMessage();
		}
		return flagStr;
	}
	
	public static Cell getCell(Row row, int colIndex){
		Cell cell=null;
		cell=row.getCell(colIndex);
		if(cell==null){
			cell=row.createCell(colIndex);
		}
		return cell;
	}
	
	/**
	 * find cell and return its row id (0-based)
	 * @param sheet
	 * @param searchcontent
	 * @param startRow if set <0, startRow=0
	 * @param startColumn if set <0, startColumn=0
	 * @param endRow if set <0 or >lastRow, endRow=lastRow
	 * @param endColumn if set <0 or >lastColumn, endColumn=lastColumn
	 * @return
	 */
	public static int findCell(Sheet sheet,String searchcontent,int startRow, int startColumn, int endRow, int endColumn){
		int rowId=-1;
		Boolean flag=false;
		if(endRow<0 || endRow>sheet.getLastRowNum()){endRow=sheet.getLastRowNum();}
		if(startRow<0 ){startRow=0;}
		if(startRow>endRow){return rowId;}
		String rowId_tmp=null;
		Row r;
		Cell c;
		for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
		       r = sheet.getRow(rowNum);
		       if (r == null) { continue;}
		       if(startColumn<0){startColumn=0;}
		       if(endColumn<0 || endColumn>r.getLastCellNum()-1){endColumn=r.getLastCellNum()-1;}
		       if(startColumn>endColumn) break;
		       for (int cn = startColumn; cn <=endColumn; cn++) {
		          c = r.getCell(cn);
		          if (c == null) {
		             continue;
		          } else {
		        	  rowId_tmp=getDisplayCellValue(c);
		        	  if(StringUtils.equalsIgnoreCase(rowId_tmp, searchcontent)){
		        		  rowId=rowNum;
		        		  flag=true;
		        		  break;
		        	  }
		          }
		       }
		       if(flag)break;
		    }
		return rowId;
	}
	/***
	 * get a list of matching rows
	 * @param list
	 * @param searchcontent
	 * @param startColumn
	 * @param endColumn
	 * @return
	 */
	public static Map<Integer,List<String>> findCell(List<List<String>> list,String searchcontent,int startColumn,int endColumn){
		Map<Integer,List<String>> result=new HashMap<Integer,List<String>>();
		List<String> row;
		if(startColumn<0 ){startColumn=0;}
		if(endColumn<0){endColumn=0;}
		if(startColumn>endColumn){return result;}
		for(int i=0;i<list.size();i++){
			row=list.get(i);
			if(startColumn>=row.size()){startColumn=row.size()-1;}
			if(endColumn>=row.size()){endColumn=row.size()-1;}
			for(int j=startColumn;j<=endColumn;j++){
				if(StringUtils.equalsIgnoreCase(searchcontent, row.get(j))){
					result.put(i, row);
				}
			}
		}
		return result;
	}
	/***
	 * get a list of matching rows, match exported file version smaller than or equal to 1.16.1
	 * @param list
	 * @param searchcontent
	 * @return
	 */
	public static Map<Integer,List<String>> findCell(List<List<String>> list,String searchcontent){
		Map<Integer,List<String>> result=new HashMap<Integer,List<String>>();
		List<String> row;
		for(int i=0;i<list.size();i++){
			row=list.get(i);
			if(StringUtils.equalsIgnoreCase(searchcontent, row.get(0))){
				result.put(i, row);
			}
		}
		return result;
	}
	/**
	 * get a list of matching rows, match exported file version larger than 1.16.2
	 * @param list
	 * @param searchRuleType
	 * @param searchId
	 * @return
	 */
	public static Map<Integer,List<String>> findCell(List<List<String>> list,String searchRuleType,String searchId){
		Map<Integer,List<String>> result=new HashMap<Integer,List<String>>();
		List<String> row;
		int startColumn=0;int endColumn=1;
		
		if(startColumn>endColumn){return result;}
		for(int i=0;i<list.size();i++){
			row=list.get(i);
			if(StringUtils.equalsIgnoreCase("Reg "+searchRuleType, row.get(0)) && searchId.equalsIgnoreCase(row.get(1))){
				result.put(i, row);
			} 
		}
		return result;
	}
	/***
	 * get the cell's display value
	 * @param row
	 * @param colIndex index 0-based
	 * @return if cannot found the cell return ""
	 */
	private static String getCellValue_expected(Row row,int colIndex){
		String cellValue="";
		Cell cell=null;
		cell=row.getCell(colIndex);
		if(cell!=null){
			cellValue=getDisplayCellValue(cell);
		}
		return cellValue;
	}
	
	/**
	 * open a Workbook.<br>created by Kun.Shen
	 * <p>create a FileInputStream for excel file, and read this file into workbook, then close this FileInputStream.<br>
	 * @param filename: a excel file.
	 * @return Workbook
	 * @throws Exception
	 */
	public static Workbook openWorkbook(File filename)
	{
		FileInputStream inp;
		Workbook workBook=null;
		try {
			inp = new FileInputStream(filename);
			ZipSecureFile.setMinInflateRatio(-1.0d);
			workBook = WorkbookFactory.create(inp);
			inp.close();
		} catch (EncryptedDocumentException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		return workBook;
	}
	
	
	/***
	 * get defined names to a csv
	 * @param excelFullName
	 * @param csvFullName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Boolean getNameInfosToCsv(String excelFullName,String csvFullName) {
		Boolean flag=false;
		try{
			if(StringUtils.isAnyBlank(excelFullName,csvFullName)){
				return flag;
			}else{
				File excelFH=new File(excelFullName);
				File csvFH=new File(csvFullName);
				if(excelFH.exists()){

					Workbook workbook=openWorkbook(excelFH);
					if(workbook!=null){
						//List<Name> allNames=(List<Name>) workbook.getAllNames();
						int nameCount = workbook.getNumberOfNames();
						if(nameCount<=0){
							logger.warn("no names in this excel"+excelFullName);
							flag=false;
						}else{
							FileUtil.writeContentToEmptyFile(csvFH, "\"CellName\",\"SheetName\",\"Instance\",\"RowRef\",\"ColumnRef\"\n");//csv header
							String sheetName,instance="";
							String rowRef,columnRef;
							String nameInfo=null;
							Name cellName=null;
							for (int nameIndex = 0; nameIndex < nameCount; nameIndex++){
								cellName=workbook.getNameAt(nameIndex);
								sheetName=cellName.getSheetName();
								instance="";
								if(sheetName.contains("|")){
									String[] tmpArr=sheetName.split("\\|");
									instance=tmpArr[1];
									sheetName=tmpArr[0];
								}
								String formula=cellName.getRefersToFormula();
								String[] formulaArr=formula.split("\\$");
								columnRef=formulaArr[1];
								rowRef=formulaArr[2];
								nameInfo="\""+cellName.getNameName()+"\",\""+sheetName+"\",\""+instance+"\",\""+rowRef+"\",\""+columnRef+"\"\n";
								FileUtil.writeContent(csvFH, nameInfo);
								//logger.info(nameInfo);
							}
							/*for(Name cellName:allNames){
								sheetName=cellName.getSheetName();
								instance="";
								if(sheetName.contains("|")){
									String[] tmpArr=sheetName.split("\\|");
									instance=tmpArr[1];
									sheetName=tmpArr[0];
								}
								String formula=cellName.getRefersToFormula();
								String[] formulaArr=formula.split("\\$");
								columnRef=formulaArr[1];
								rowRef=formulaArr[2];
								nameInfo="\""+cellName.getNameName()+"\",\""+sheetName+"\",\""+instance+"\",\""+rowRef+"\",\""+columnRef+"\"\n";
								FileUtil.writeContent(csvFH, nameInfo);
								//logger.info(nameInfo);
							}*/
							flag=true;
						}						
					}
				}else{
					logger.error("error: File Not Found: "+excelFullName);
				}
			}
		}catch(IOException e){
			logger.error(e.getMessage(),e);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return flag;
	}
	
	
	
	private static String getDisplayCellValue(Cell cell){
		String displayValue=null;
		if(cell==null){return null;}
		DataFormatter formatter=new DataFormatter();
		switch(cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC:
				String dataFormatStr=cell.getCellStyle().getDataFormatString();
				short dataIndex=cell.getCellStyle().getDataFormat();
				double numericCellVal=cell.getNumericCellValue();
				if (DateUtil.isCellDateFormatted(cell))
				{
					//displayValue = formatter.formatCellValue(cell,cell.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()).trim();

		    		if(dataIndex==14){
		    			displayValue=formatter.formatRawCellContents(numericCellVal,dataIndex,"MM/dd/yyyy").trim();
		    		}else{
		    			displayValue = formatter.formatRawCellContents(numericCellVal,dataIndex,dataFormatStr).trim();
		    		}
				}else{
					if(dataFormatStr.contains(")")){
						displayValue=formatter.formatRawCellContents(numericCellVal, dataIndex, "#,##0;-#,##0").trim();
					}else{
						displayValue = formatter.formatRawCellContents(numericCellVal,dataIndex,dataFormatStr).trim();
					}

				}
				break;
			case Cell.CELL_TYPE_STRING:
				displayValue = cell.getStringCellValue().trim();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				displayValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				displayValue = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				displayValue = "";
				break;
			case Cell.CELL_TYPE_ERROR:
				displayValue = "";
				break;
			default:
				displayValue=cell.toString().trim();
				break;
		}
		
		/*if(cell.getCellType().equals(CellType.NUMERIC)){
	    	
	    	
	    }else if(cell.getCellType().equals(CellType.STRING)){
	    	
	    }else if(cell.getCellType().equals(CellType.BOOLEAN)){
	    	
	    }else if(cell.getCellType().equals(CellType.FORMULA)){
	    	
	    }else if(cell.getCellType().equals(CellType.BLANK) || cell.getCellType().equals(CellType.ERROR)){
	    	displayValue = "";
	    }else{
	    	displayValue=cell.toString().trim();
	    }*/
		return displayValue;
	}
	

	
	/**
	 * save Workbook.<br>created by Kun.Shen
	 * <p>after editing all cells, then write it to Workbook.
	 * @param filename a excel file.
	 * @param wb a Workbook
	 * @throws Exception
	 */
	public static void saveWorkbook(File filename,Workbook wb)
	{
		FileOutputStream out;
		try {
			out = new FileOutputStream(filename);
			wb.write(out);
			out.flush();
			out.close();
			wb.close();
		} catch ( IOException e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
	/**
	 * save Workbook.<br>created by Kun.Shen
	 * <p>after editing all cells, then write it to Workbook.
	 * @param filename a excel file.
	 * @param sheetName a Workbook
	 * @param list
	 * @throws Exception
	 */
	public static void saveWorkbook(String filename,String sheetName,List<List<String>> list) throws Exception
	{
		SXSSFWorkbook workBook=new SXSSFWorkbook(100);
		Sheet sheet;
		if(StringUtils.isBlank(sheetName)){
			sheet=workBook.createSheet();
		}else{
			sheet=workBook.createSheet(sheetName);
		}
		List<String> r;
		Row row ;
		Cell cell;
		for(int i=0;i<list.size();i++){
			r=list.get(i);
			row=sheet.createRow(i);
			for(int j=0;j<r.size();j++){
				cell=row.createCell(j);
				cell.setCellValue(r.get(j));
			}
		}
		FileOutputStream out = new FileOutputStream(filename);
		workBook.write(out);
		out.flush();
		out.close();
		workBook.close();
		workBook.dispose();
	}
	
	/***
	 * date1 equals date2 return 0; date1 > date2 or date1 < date2 return 1; date1 or date2 is blank return -1
	 * @param date1 date1 is blank return -1
	 * @param date2 date2 is blank return -1
	 * @return
	 */
	public static int compareDates(String date1, String date2) {
		int a=-1;
	    if (date1 == null || date1.equals(""))
	      return a;
	    if (date2 == null || date2.equals(""))
	      return a;
	   /* String en_US="MM/dd/yyyy";
	    String en_GB="dd/MM/yyyy";
	    String zh_CN="yyyy-MM-dd";
	   */
	    String[] dateFormats=new String[]{"MM/dd/yyyy","dd/MM/yyyy","yyyy-MM-dd"};
	    for(String dateFormat:dateFormats){
	    	 // 转换为标准时间
		    SimpleDateFormat myFormatter = new SimpleDateFormat(dateFormat);
		    java.util.Date dateOne = null;
		    java.util.Date dateTwo = null;
		    try {
		    	dateOne = myFormatter.parse(date1);
		    	dateTwo = myFormatter.parse(date2);
		    	long day = (dateOne.getTime() - dateTwo.getTime()) / (24 * 60 * 60 * 1000);
			    if(day>0 || day<0){ a=1;}else{a=0;}
			    break;
		    } catch (Exception e) {
		    }
	    }
	   
	    
	    return a;
	  }
	/***
	 * get objects from excel
	 * @param excelFileStr excel's full path and name
	 * @param sheetName excel's sheet name, if {sheetName} is null, {getLastOne} is true, get the last sheet; if {sheetName} is null, {getLastOne} is false, get the first sheet
	 * @param getLastOne true get last sheet start with {sheetName} plus numbers, like ExportToCSV3, false get the sheet named {sheetName}
	 * @return
	 */
		public static List<Map<Integer,String>> getObjects(String excelFileStr,String sheetName,Boolean getLastOne)
		{
			List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>(); 
			Workbook xwb =null;
			try
			{
				xwb =ExcelUtil.openWorkbook(new File(excelFileStr));
				Sheet sheet = null;
				if(StringUtils.isNotBlank(sheetName))
				{
					sheet=xwb.getSheet(sheetName);
					if(getLastOne)
					{
						int lastIndex=xwb.getNumberOfSheets()-1;
						for(int index=lastIndex;index>=0;index--)
						{
							String sheetNameTmp=xwb.getSheetName(index);
							if(sheetNameTmp.equalsIgnoreCase(sheetName)){break;}
							if(sheetNameTmp.startsWith(sheetName)){
								String a=sheetNameTmp.substring(sheetName.length());
								Pattern patern=Pattern.compile("\\d+");
								Matcher isNum=patern.matcher(a);
								if(isNum.matches())
								{
									sheet=xwb.getSheet(sheetNameTmp);
									break;
								}
							}
						}
					}
				}else{
					if(getLastOne){
						sheet=xwb.getSheetAt(xwb.getNumberOfSheets()-1);
					}
				}
				if(sheet==null)
				{sheet = xwb.getSheetAt(0);}
				int rowNum=sheet.getLastRowNum();
				Map<Integer,String> map;
				Row row;
				for(int i=0;i<=rowNum;i++)
				{
					row=sheet.getRow(i);
					if(row==null){continue;}
					map = new HashMap<Integer,String>();
					int cellNum=row.getLastCellNum();
					for(int j=row.getFirstCellNum();j<cellNum;j++){
						map.put(j, getCellValue_expected(row,j));
					}
					list.add(map);
				}
				
			}catch(Exception e)
			{
				logger.error(e.getMessage(),e);
			}
			Runtime.getRuntime().gc();
			return list;
		}
	/***
	 * get objects from excel
	 * @param excelFileStr excel's full path and name
	 * @param sheetName excel's sheet name, if {sheetName} is null, {getLastOne} is true, get the last sheet; if {sheetName} is null, {getLastOne} is false, get the first sheet
	 * @param getLastOne true get last sheet start with {sheetName} plus numbers, like ExportToCSV3, false get the sheet named {sheetName}
	 * @param pojo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	public static <T> Iterator<T> getObjects1(String excelFileStr,String sheetName,Boolean getLastOne,Class pojo)
	{
		List<T> list = new ArrayList<T>(); 
		Workbook xwb =null;
		try
		{
			xwb =ExcelUtil.openWorkbook(new File(excelFileStr));
			Sheet sheet = null;
			if(StringUtils.isNotBlank(sheetName))
			{
				sheet=xwb.getSheet(sheetName);
				if(getLastOne)
				{
					int lastIndex=xwb.getNumberOfSheets()-1;
					for(int index=lastIndex;index>=0;index--)
					{
						String sheetNameTmp=xwb.getSheetName(index);
						if(sheetNameTmp.equalsIgnoreCase(sheetName)){break;}
						if(sheetNameTmp.startsWith(sheetName)){
							String a=sheetNameTmp.substring(sheetName.length());
							Pattern patern=Pattern.compile("\\d+");
							Matcher isNum=patern.matcher(a);
							if(isNum.matches())
							{
								sheet=xwb.getSheet(sheetNameTmp);
								break;
							}
						}
					}
				}
			}else{
				if(getLastOne){
					sheet=xwb.getSheetAt(xwb.getNumberOfSheets()-1);
				}
			}
			if(sheet==null)
			{sheet = xwb.getSheetAt(0);}
			Row titleRow=sheet.getRow(0);
			int rowNum=sheet.getLastRowNum();
			for(int i=1;i<=rowNum;i++)
			{
				Row row=sheet.getRow(i);
				if(row==null){continue;}
				T form=(T)fromRowToBean(titleRow,row,pojo,null);
				if(StringUtils.isNoneBlank(form.toString()))
				{
					list.add(form);
				}
			}
			
		}catch(Exception e)
		{
			System.out.println("data parsed error");  
		}
		Runtime.getRuntime().gc();
		return list.iterator();
	}
/***
 * get objects from excel
 * @param excelFileStr excel's full path and name
 * @param sheetName excel's sheet name, if {sheetName} is null, {getLastOne} is true, get the last sheet; if {sheetName} is null, {getLastOne} is false, get the first sheet
 * @param getLastOne true get last sheet start with {sheetName} plus numbers, like ExportToCSV3, false get the sheet named {sheetName}
 * @param pojo
 * @return
 */
	@SuppressWarnings({ "rawtypes", "unchecked" }) 
	public static <T> List<T> getObjects(String excelFileStr,String sheetName,Boolean getLastOne,Class pojo)
	{
		List<T> list = new ArrayList<T>(); 
		Workbook xwb =null;
		try
		{
			xwb =ExcelUtil.openWorkbook(new File(excelFileStr));
			Sheet sheet = null;
			if(StringUtils.isNotBlank(sheetName))
			{
				sheet=xwb.getSheet(sheetName);
				if(getLastOne)
				{
					int lastIndex=xwb.getNumberOfSheets()-1;
					for(int index=lastIndex;index>=0;index--)
					{
						String sheetNameTmp=xwb.getSheetName(index);
						if(sheetNameTmp.equalsIgnoreCase(sheetName)){break;}
						if(sheetNameTmp.startsWith(sheetName)){
							String a=sheetNameTmp.substring(sheetName.length());
							Pattern patern=Pattern.compile("\\d+");
							Matcher isNum=patern.matcher(a);
							if(isNum.matches())
							{
								sheet=xwb.getSheet(sheetNameTmp);
								break;
							}
						}
					}
				}
			}else{
				if(getLastOne){
					sheet=xwb.getSheetAt(xwb.getNumberOfSheets()-1);
				}
			}
			if(sheet==null)
			{sheet = xwb.getSheetAt(0);}
			Row titleRow=sheet.getRow(0);
			int rowNum=sheet.getLastRowNum();
			for(int i=1;i<=rowNum;i++)
			{
				Row row=sheet.getRow(i);
				if(row==null){continue;}
				T form=(T)fromRowToBean(titleRow,row,pojo,null);
				if(StringUtils.isNoneBlank(form.toString()))
				{
					list.add(form);
				}
			}
			
		}catch(Exception e)
		{
			logger.error(e.getMessage(),e);
		}
		Runtime.getRuntime().gc();
		return list;
	}
	/**
	 * get object from a excel row<br>created by Kun.Shen
	 * @param titleRow excel's sheet title row
	 * @param rootRow the row can translate to object
	 * @param pojo which object's class you want to translate
	 * @return
	 * @throws Exception
	 */

	@SuppressWarnings("rawtypes")  
	private static Object fromRowToBean(Row titleRow,Row rootRow, Class pojo, List<Class> claes) throws Exception  
	{  
	    // 首先得到pojo所定义的字段  
	    Field[] fields = pojo.getDeclaredFields();  
	    // 根据传入的Class动态生成pojo对象  
	    Object obj = pojo.newInstance();  
	    
	    for (Field field : fields)  
	    {  
	    	int mod=field.getModifiers();
	    	if(Modifier.isFinal(mod) && Modifier.isStatic(mod)&& field.getType().equals(Logger.class)) continue;
	        // 设置字段可访问（必须，否则报错）  
	        field.setAccessible(true);  
	        // 得到字段的属性名  
	        String name = field.getName();
	        // 这一段的作用是如果字段在Element中不存在会抛出异常，如果出异常，则跳过。
	        String rootCellValue=null;
	        try  
	        {
	        	if (claes!=null && claes.contains(field.getType()))  
	        	{  
	        		 field.set(obj,fromRowToBean(titleRow,rootRow,field.getType(),claes));
	        		 continue;
	        	}
	        	int lastCellNum=titleRow.getLastCellNum();
	        	Cell titleCell=null;
	        	String titleCellValue=null;
	        	for(int cellNum=0;cellNum<lastCellNum;cellNum++)
	        	{
	        		titleCell=titleRow.getCell(cellNum);
	        		if(titleCell==null){continue;}
	        		titleCellValue=getDisplayCellValue(titleCell).trim();
	        		
	        		if(titleCellValue!=null && (name.equalsIgnoreCase(titleCellValue) || titleCellValue.equalsIgnoreCase(pojo.getSimpleName()+"."+name)))
	        		{
	        			Cell rootCell=rootRow.getCell(cellNum);
	        			if(rootCell!=null) {rootCellValue=getDisplayCellValue(rootCell).trim();}
	        			break;
	        			
	        		}
	        	}
	        	
	        }catch (Exception ex)  
	        {  
	        	continue;  
	        }  
	        if (StringUtils.isNotBlank(rootCellValue))  
	        {  
	            // 根据字段的类型将值转化为相应的类型，并设置到生成的对象中。  
	            if (field.getType().equals(String.class))  
	            {  
	            	field.set(obj, rootCellValue);   
	            }
	            else if (field.getType().equals(Long.class) || field.getType().equals(long.class))  
	            {  
	                field.set(obj, Long.parseLong(rootCellValue));
	            }  
	            else if (field.getType().equals(Double.class) || field.getType().equals(double.class))  
	            {  
	                field.set(obj, Double.parseDouble(rootCellValue));  
	            }  
	            else if (field.getType().equals(Integer.class) || field.getType().equals(int.class))  
	            {  
	                field.set(obj, Integer.parseInt(rootCellValue));  
	            }
	        }  
	        
	    }  
	    return obj;  
	}
	
	/***
	 * translate a object to a excel row
	 * @param rootRow the row you want to write object, skip head row(index=0)
	 * @param obj the object you want to write to the rootRow
	 * @param claes inner classes of this obj's class
	 * @param indexOfColumn start index of column
	 * @return index of column
	 * @throws Exception
	 * @author kun shen
	 * @since 2018/11/12
	 */
	@SuppressWarnings("rawtypes")  
	private static int fromBeanToRow(Row rootRow, Object obj, List<Class> claes,int indexOfColumn) throws Exception  
	{  
		Class pojo=obj.getClass();
	    // 首先得到pojo所定义的字段  
	    Field[] fields = pojo.getDeclaredFields();
	    
	    Boolean flagForWriteTitle=false;
	    int index=rootRow.getRowNum();
	    Row titleRow=null;
	    if(index==1)
	    {
	    	flagForWriteTitle=true;
	    	titleRow=rootRow.getSheet().getRow(0);
	    	if(titleRow==null){
	    		titleRow=rootRow.getSheet().createRow(0);
	    	}
	    }

	    for (Field field : fields)  
	    {  
	    	int mod=field.getModifiers();
	    	if(Modifier.isFinal(mod) && Modifier.isStatic(mod)&& field.getType().equals(Logger.class)) continue;
	        // 设置字段可访问（必须，否则报错）  
	        field.setAccessible(true);  
	        // 得到字段的属性名  
	        String name = field.getName();  
	        Object valueObj=field.get(obj);
	        Cell cell=null;
	        try  
	        {  	
	        	if(claes!=null && claes.contains(field.getType())){
	        		indexOfColumn=fromBeanToRow(rootRow,valueObj,claes,indexOfColumn);
	        		continue;
	        	}
	        	if(valueObj!=null)
	        	{
	        		cell=rootRow.createCell(indexOfColumn);
	        		cell.setCellType(1);//CellType.STRING
	        		cell.setCellValue(valueObj.toString());
	        	}
	        	if(flagForWriteTitle)
	        	{
	        		cell=titleRow.createCell(indexOfColumn);
	        		cell.setCellType(1);//CellType.STRING
	        		if(claes!=null && claes.contains(pojo))
	        		{
	        			cell.setCellValue(pojo.getSimpleName()+"."+name);
	        		}else
	        		{
	        			cell.setCellValue(name);
	        		}
	        	}
	        }  
	        catch (Exception ex)  
	        {  
	        	continue;  
	        }  
	        indexOfColumn++;
	        
	    }  
	    return indexOfColumn;
	} 

	/**
	 * write objects to Excel 
	 * @author kun shen
	 * @param list
	 * @param excelFileStr fullpath with file name (support .xlsx and .xls formats)
	 * @param sheetName, if {sheetName} is null, {rewrite} is true, get the last sheet
	 * @param rewrite
	 * @since 2017.10.25
	 */
	public static void writeObjectsToExcel(List<Map<Integer,String>> list,String excelFileStr,String sheetName,Boolean rewrite)
	{
		File excelFile=new File(excelFileStr);
		Workbook xwb=null;
		FileInputStream fileInputStream=null;
		try
		{
			if(!excelFile.exists())
			{
				//excelFile.createNewFile();
				if(excelFileStr.endsWith(".xls"))
				{
					xwb=new HSSFWorkbook();
				}
				if(excelFileStr.endsWith(".xlsx"))
				{
					xwb=new XSSFWorkbook();
				}
				
			}
			else
			{
				fileInputStream = new FileInputStream(excelFile);
				xwb = WorkbookFactory.create(fileInputStream);
				fileInputStream.close();
				int lastIndex=xwb.getNumberOfSheets()-1;
				if(rewrite)
				{
					
					String sheetNameTmp=null;
					int index=lastIndex;
					for(;index>=0;index--)
					{
						sheetNameTmp=xwb.getSheetName(index);
						if(StringUtils.isNotBlank(sheetName))
						{
							if(sheetNameTmp.equalsIgnoreCase(sheetName)|| sheetName.equalsIgnoreCase("null")){
								break;
							}
							if(sheetNameTmp.startsWith(sheetName)){
								String a=sheetNameTmp.substring(sheetName.length());
								Pattern patern=Pattern.compile("\\d+");
								Matcher isNum=patern.matcher(a);
								if(isNum.matches())
								{
									break;
								}
							}
						}else{
							break;
						}
					}
					sheetName=sheetNameTmp;
					xwb.removeSheetAt(index);//remove it, create new one at next step
				}else{
					if(StringUtils.isBlank(sheetName)|| sheetName.equalsIgnoreCase("null")){
						sheetName=xwb.getSheetName(lastIndex);
					}
				}
			}
			Sheet sheet = null;
			int i=1;
			String sheetNameVar=sheetName;
			while(sheet==null)
			{
				try
				{
					sheet=xwb.createSheet(sheetName);
				}catch(Exception e)
				{
					sheetName = sheetNameVar+String.valueOf(i);
					i++;
					continue;
				}
			}
			Map<Integer,String> map;
			for(i=0;i<list.size();i++)
			{
				Row row=sheet.createRow(i);
				map=list.get(i);
				for(Map.Entry<Integer, String> entry: map.entrySet()){
					row.createCell(entry.getKey()).setCellValue(entry.getValue());
				}
				
			}
			FileOutputStream out = new FileOutputStream(excelFileStr);
			xwb.write(out);
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			if(fileInputStream!=null)
			{
				
				try {  
					fileInputStream.close();  
					} catch (IOException e) {logger.error(e.getMessage());}  

			}
		}
		Runtime.getRuntime().gc();
	}
	
	/**
	 * write objects to Excel 
	 * @author kun shen
	 * @param forms
	 * @param excelFileStr fullpath with file name (support .xlsx and .xls formats)
	 * @param sheetName, if {sheetName} is null, {rewrite} is true, get the last sheet
	 * @param rewrite
	 * @param claes classes
	 * @since 2017.10.25
	 */
	@SuppressWarnings("rawtypes")
	public static <T> void writeObjectsToExcel(List<T> forms,String excelFileStr,String sheetName,Boolean rewrite,List<Class> claes)
	{
		File excelFile=new File(excelFileStr);
		Workbook xwb=null;
		FileInputStream fileInputStream=null;
		try
		{
			if(!excelFile.exists())
			{
				//excelFile.createNewFile();
				if(excelFileStr.endsWith(".xls"))
				{
					xwb=new HSSFWorkbook();
				}
				if(excelFileStr.endsWith(".xlsx"))
				{
					xwb=new XSSFWorkbook();
				}
				
			}
			else
			{
				fileInputStream = new FileInputStream(excelFile);
				xwb = WorkbookFactory.create(fileInputStream);
				fileInputStream.close();
				int lastIndex=xwb.getNumberOfSheets()-1;
				if(rewrite)
				{
					String sheetNameTmp=null;
					int index=lastIndex;
					for(;index>=0;index--)
					{
						sheetNameTmp=xwb.getSheetName(index);
						if(StringUtils.isNotBlank(sheetName))
						{
							if(sheetNameTmp.equalsIgnoreCase(sheetName)|| sheetName.equalsIgnoreCase("null")){
								break;
							}
							if(sheetNameTmp.startsWith(sheetName)){
								String a=sheetNameTmp.substring(sheetName.length());
								Pattern patern=Pattern.compile("\\d+");
								Matcher isNum=patern.matcher(a);
								if(isNum.matches())
								{
									break;
								}
							}
						}else{
							break;
						}
					}
					sheetName=sheetNameTmp;
					xwb.removeSheetAt(index);//remove it, create new one at next step
					
				}else{
					if(StringUtils.isBlank(sheetName)|| sheetName.equalsIgnoreCase("null")){
						sheetName=xwb.getSheetName(lastIndex);
					}
				}
			}
			Sheet sheet = null;
			int i=1;
			String sheetNameVar=sheetName;
			while(sheet==null)
			{
				try
				{
					sheet=xwb.createSheet(sheetName);
				}catch(Exception e)
				{
					sheetName = sheetNameVar+String.valueOf(i);
					i++;
					continue;
				}
			}

			for(i=0;i<forms.size();i++)
			{
				Row row=sheet.createRow(i+1);
				fromBeanToRow(row,forms.get(i),claes,0);
			}
			FileOutputStream out = new FileOutputStream(excelFileStr);
			xwb.write(out);
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			if(fileInputStream!=null)
			{
				
				try {  
					fileInputStream.close();  
					} catch (IOException e) {logger.error(e.getMessage());}  

			}
		}
		Runtime.getRuntime().gc();
	}
	
	/**
	 * write objects to Excel 
	 * @author kun shen
	 * @param forms
	 * @param excelFileStr fullpath with file name (support .xlsx and .xls formats)
	 * @param sheetName, if {sheetName} is null, {rewrite} is true, get the last sheet
	 * @param rewrite
	 * @param claes classes
	 * @since 2017.10.25
	 */
	@SuppressWarnings("rawtypes")
	public static <T> void writeObjectsToExcel(Iterator<T> forms,String excelFileStr,String sheetName,Boolean rewrite,List<Class> claes)
	{
		File excelFile=new File(excelFileStr);
		Workbook xwb=null;
		FileInputStream fileInputStream=null;
		try
		{
			if(!excelFile.exists())
			{
				//excelFile.createNewFile();
				if(excelFileStr.endsWith(".xls"))
				{
					xwb=new HSSFWorkbook();
				}
				if(excelFileStr.endsWith(".xlsx"))
				{
					xwb=new XSSFWorkbook();
				}
				
			}else
			{
				fileInputStream = new FileInputStream(excelFile);
				xwb = WorkbookFactory.create(fileInputStream);
				fileInputStream.close();
				int lastIndex=xwb.getNumberOfSheets()-1;
				if(rewrite)
				{
					String sheetNameTmp=null;
					int index=lastIndex;
					for(;index>=0;index--)
					{
						sheetNameTmp=xwb.getSheetName(index);
						if(StringUtils.isNotBlank(sheetName))
						{
							if(sheetNameTmp.equalsIgnoreCase(sheetName)|| sheetName.equalsIgnoreCase("null")){
								break;
							}
							if(sheetNameTmp.startsWith(sheetName)){
								String a=sheetNameTmp.substring(sheetName.length());
								Pattern patern=Pattern.compile("\\d+");
								Matcher isNum=patern.matcher(a);
								if(isNum.matches())
								{
									break;
								}
							}
						}else{
							break;
						}
					}
					sheetName=sheetNameTmp;
					xwb.removeSheetAt(index);//remove it, create new one at next step
				}else{
					if(StringUtils.isBlank(sheetName)|| sheetName.equalsIgnoreCase("null")){
						sheetName=xwb.getSheetName(lastIndex);
					}
				}
			}
			Sheet sheet = null;
			int i=1;
			String sheetNameVar=sheetName;
			while(sheet==null)
			{
				try
				{
					sheet=xwb.createSheet(sheetName);
				}catch(Exception e)
				{
					sheetName = sheetNameVar+String.valueOf(i);
					i++;
					continue;
				}
			}
			i=0;
			
			while(forms.hasNext()){
				Row row=sheet.createRow(i+1);
				fromBeanToRow(row,forms.next(),claes,0);
				i++;
			}
			FileOutputStream out = new FileOutputStream(excelFileStr);
			xwb.write(out);
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			if(fileInputStream!=null)
			{
				
				try {  
					fileInputStream.close();  
					} catch (IOException e) {logger.error(e.getMessage());}  

			}
		}
		Runtime.getRuntime().gc();
	}
	
	
}
