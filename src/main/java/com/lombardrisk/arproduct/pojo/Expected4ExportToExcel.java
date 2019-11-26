package com.lombardrisk.arproduct.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class Expected4ExportToExcel {
	private String cellName;
	private String rowID;
	private String instance;
	private String editValue;
	private String expectedValue;
	private String acctualValue;
	private String expectedValue_DB;
	private String acctualValue_DB;
	private String testResult;
	private String notes;
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getRowID() {
		return rowID;
	}
	public void setRowID(String rowID) {
		this.rowID = rowID;
	}
	public String getInstance() {
		return instance;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public String getEditValue() {
		return editValue;
	}
	public void setEditValue(String editValue) {
		this.editValue = editValue;
	}
	public String getExpectedValue() {
		return expectedValue;
	}
	public void setExpectedValue(String expectedValue) {
		this.expectedValue = expectedValue;
	}
	public String getAcctualValue() {
		return acctualValue;
	}
	public void setAcctualValue(String acctualValue) {
		this.acctualValue = acctualValue;
	}
	public String getExpectedValue_DB() {
		return expectedValue_DB;
	}
	public void setExpectedValue_DB(String expectedValue_DB) {
		this.expectedValue_DB = expectedValue_DB;
	}
	public String getAcctualValue_DB() {
		return acctualValue_DB;
	}
	public void setAcctualValue_DB(String acctualValue_DB) {
		this.acctualValue_DB = acctualValue_DB;
	}
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
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
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		if(StringUtils.isNotBlank(notes)){
			if(StringUtils.isBlank(this.notes)){
				this.notes=notes;
			}else{
				this.notes = this.notes.concat(notes);
			}

		}
	}

	public void clearExistedResult(){
		this.notes=null;
		this.acctualValue = null;
		this.testResult = null;
	}
	
}
