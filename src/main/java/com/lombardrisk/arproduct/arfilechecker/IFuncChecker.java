package com.lombardrisk.arproduct.arfilechecker;

public interface IFuncChecker {
	public String getExec_DownloadFile();
	public void setExec_DownloadFile(String exec_downloadFile);
	public String getExec_ExpectationFile();
	public void setExec_ExpectationFile(String exec_ExpectationFile);
	public String getExecutionStatus();
	public void setExecutionStatus(String executionStatus);
	public Boolean checker();
	public Boolean checker(String exportedExcelFullName,String expectedExcelFullName);
}
