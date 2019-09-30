package com.lombardrisk.arproduct.arfilechecker;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.lombardrisk.arproduct.utils.ValidationRuleUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lombardrisk.arproduct.utils.ExcelUtil;
import com.lombardrisk.arproduct.utils.Helper;


public class ValidationRuleChecker implements IFuncChecker{
	private final static Logger logger = LoggerFactory.getLogger(ValidationRuleChecker.class);
	private String formInfo;
	private String exec_DownloadFile;
	private String exec_ExpectationFile;
	private String executionStatus;
	public ValidationRuleChecker(){}
	public ValidationRuleChecker(String downloadFile,String expectation){
		setFormInfo("");
		setExec_DownloadFile(downloadFile);
		setExec_ExpectationFile(expectation);

	}
	public ValidationRuleChecker(String formInfo, String downloadFile,String expectation){
		setFormInfo(formInfo);
		setExec_DownloadFile(downloadFile);
		setExec_ExpectationFile(expectation);

	}
	public String getExec_DownloadFile() {
		return exec_DownloadFile;
	}
	public void setExec_DownloadFile(String exec_DownloadFile) {
		this.exec_DownloadFile = Helper.reviseFilePath(exec_DownloadFile);
		//this.exec_DownloadFile = exec_DownloadFile;
	}
	public String getExec_ExpectationFile() {
		return exec_ExpectationFile;
	}
	public void setExec_ExpectationFile(String exec_ExpectationFile) {
		this.exec_ExpectationFile =Helper.reviseFilePath(exec_ExpectationFile);
	}
	public String getExecutionStatus() {
		return executionStatus;
	}
	public void setExecutionStatus(String executionStatus) {
		this.executionStatus = executionStatus;
	}	
	
	public Boolean checker(){
		return checker(this.getFormInfo(), this.getExec_DownloadFile(),this.getExec_ExpectationFile());
	}

	public Boolean checker(String exportedExcelFullName,String expectedExcelFullName){
		return checker(this.getFormInfo(), exportedExcelFullName, expectedExcelFullName);
	}

	public Boolean checker(String formInfo, String exportedExcelFullName,String expectedExcelFullName){
		logger.info("start checking validation rule");
		if(!new File(exportedExcelFullName).isFile()){
			setExecutionStatus("error: File Not Found "+exportedExcelFullName);
			return false;
		}
		String status=ValidationRuleUtil.fileNotExists(expectedExcelFullName,exportedExcelFullName);
		if(status.equals("")){
			status=ValidationRuleUtil.writeValidationRulesResult(formInfo, expectedExcelFullName,null,exportedExcelFullName);
		}
		setExecutionStatus(status);
		Boolean flag=false;
		if(status.startsWith("pass")){
			flag=true;
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

	public String getFormInfo() {
		return formInfo;
	}

	public void setFormInfo(final String formInfo) {
		this.formInfo = formInfo;
	}
}
