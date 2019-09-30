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


	/**
	 * It can handler two formatted excel,"UIDisplay and exporttoexcel"
	 * @param fileFullName_expected
	 * @param sheetName_expected
	 * @param fileFullName_exported
	 * @param csvUtil
	 * @return
	 */
	public static String writeExport2ExcelResult(String logPrefix, String fileFullName_expected, String sheetName_expected,String fileFullName_exported,CsvDBUtil csvUtil)
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
					logger.error(logPrefix+" Verify row:"+(i+1)+" cannot find expected cell info");
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
					logger.error(logPrefix+" Verify row:"+(i+1)+" cannot find exported cell info");
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


				String[] resultArr=compareValue(logPrefix,String.valueOf(i+1),actualValue_expected,expectedValue_expected);
				expected_obj.setTestResult(resultArr[0]);
				expected_obj.setNotes(resultArr[1]);
				if(resultArr[0].equalsIgnoreCase("fail")){
					flagStr=resultArr[0];
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
	 * get the cell's display value
	 * @param row
	 * @param colIndex index 0-based
	 * @return if cannot found the cell return ""
	 */
	protected static String getCellValue_expected(Row row,int colIndex){
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
	
	
	
	protected static String getDisplayCellValue(Cell cell){
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
				}else {
					if (dataFormatStr.contains("#,##0_)")) {
						displayValue =
								formatter.formatRawCellContents(numericCellVal, dataIndex, "#,##0;-#,##0").trim();
					} else if (dataFormatStr.contains("\"%\"")) {
						dataFormatStr=dataFormatStr.replace("\"%\"","");
						displayValue =
								formatter.formatRawCellContents(numericCellVal, dataIndex, dataFormatStr).trim()+"%";
					} else{
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

	private static String[] compareValue(String logPrefix, String rowId,String actual, String expected){
		String[] ret=new String[2];
		ret[0]="pass";//result
		ret[1]=null;//notes
		if(actual.equalsIgnoreCase(expected)){
			//logger.info(logPrefix+" Verify row:"+rowId+" "+ret[0]);
		}else{
			int days=compareDates(actual,expected);
			if(days==0){
				ret[1]="similar date format:"+expected+" vs "+actual;
			}else{
				actual=actual.replaceAll("\\s","");
				expected=expected.replaceAll("\\s","");
				if(!actual.equalsIgnoreCase(expected)){
					ret[0]="fail";
				}
			}
			logger.info(logPrefix+" Verify row:"+rowId+" "+ret[0]+" "+expected+" vs "+actual);
		}
		return ret;
	}
	
}
