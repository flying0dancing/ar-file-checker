package com.lombardrisk.arproduct.arfilechecker;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lombardrisk.arproduct.utils.CsvDBUtil;
import com.lombardrisk.arproduct.utils.ExcelUtil;
import com.lombardrisk.arproduct.utils.Helper;

public class ExcelChecker  implements IFuncChecker{
	private final static Logger logger = LoggerFactory.getLogger(ExcelChecker.class);
	private String exec_DownloadFile;
	private String exec_ExpectationFile;
	private String executionStatus;
	public ExcelChecker(){}
	public ExcelChecker(String downloadFile,String expectation){
		setExec_DownloadFile(downloadFile);
		setExec_ExpectationFile(expectation);
	}

	public String getExec_DownloadFile() {
		return exec_DownloadFile;
	}

	public void setExec_DownloadFile(String exec_downloadFile) {
		this.exec_DownloadFile = Helper.reviseFilePath(exec_downloadFile);
	}

	public String getExec_ExpectationFile() {
		return exec_ExpectationFile;
	}

	public void setExec_ExpectationFile(String exec_ExpectationFile) {
		this.exec_ExpectationFile = Helper.reviseFilePath(exec_ExpectationFile);
	}
	
	public String getExecutionStatus() {
		return executionStatus;
	}
	
	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}
	public Boolean checker(){
		return checker(this.getExec_DownloadFile(),this.getExec_ExpectationFile());
	}
	public Boolean checker(String exportedExcelFullName,String expectedExcelFullName){
		logger.info("start checking exported excel");
		if(!new File(exportedExcelFullName).isFile()){
			setExecutionStatus("error: File Not Found "+exportedExcelFullName);
			return false;
		}
		String csvFullName=exportedExcelFullName.substring(0,exportedExcelFullName.lastIndexOf(".")).concat(".csv");
		Boolean flag=ExcelUtil.getNameInfosToCsv(exportedExcelFullName, csvFullName);
		if(flag){
			logger.info("cellInfo are saved in "+csvFullName);
			CsvDBUtil csvUtil=new CsvDBUtil(csvFullName);
			//logger.info(csvUtil.printDuplicatedCells());
			String log=csvUtil.printDuplicatedCells();
			if(StringUtils.isBlank(log)){logger.info(log);}
			String status=ExcelUtil.writeExport2ExcelResult(expectedExcelFullName,null,exportedExcelFullName,csvUtil);
			setExecutionStatus(status+System.getProperty("line.separator")+log);
			if(status.startsWith("pass")){
				flag=true;
			}else{
				flag=false;
			}
		}else{
			setExecutionStatus("fail on getNameInfo to csv");
		}
		
		return flag;
		
	}
	public String toString()
	{
		StringBuffer stringBuffer=new StringBuffer();
		Field[] fields=getClass().getDeclaredFields();
		for(Field field:fields)
		{
			int mod=field.getModifiers();
			if(Modifier.isFinal(mod) && Modifier.isStatic(mod) && field.getType().equals(Logger.class))continue;
			try {
				String value=null;
				Object obj=field.get(this);
				if(obj==null || StringUtils.isBlank(obj.toString()))
				{continue;}
				else value=field.get(this).toString();
				stringBuffer.append(field.getName()+"[" + value+"] ");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return stringBuffer.toString();
	}
}
