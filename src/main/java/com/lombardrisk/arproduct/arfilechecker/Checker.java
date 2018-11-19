package com.lombardrisk.arproduct.arfilechecker;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lombardrisk.arproduct.utils.ExcelUtil;
import com.lombardrisk.arproduct.utils.FileUtil;
import com.lombardrisk.arproduct.utils.Helper;

/***
 * hello world!
 * @author kun shen
 *
 */
public class Checker implements IComFolder
{
	private final static Logger logger = LoggerFactory.getLogger(Checker.class);
    public static void main( String[] args )
    {
        long begin=System.currentTimeMillis();
    	long end=begin;
    	//read args from command-line
    	if(args.length>0){
			for(String s:args){
				if(s.contains("=")){
					String[] argKeyValue=s.split("=");
					argKeyValue[0]=argKeyValue[0].replaceAll("^\\-D?(.*)$", "$1");
					System.setProperty(argKeyValue[0], argKeyValue[1]);
				}else{
					s=s.replaceAll("^\\-D?(.*)$", "$1");
					System.setProperty(s,"true");
				}
				
			}
			
		}
    	String funcType=System.getProperty(CMDL_FUNC);
    	String downloadFile=Helper.reviseFilePath(System.getProperty(CMDL_DOWNLOADFILE));
    	String expectedFile=Helper.reviseFilePath(System.getProperty(CMDL_EXPECTATION));
    	String allInOneFile=Helper.reviseFilePath(System.getProperty(CMDL_ALLINONE));
    	String allInOneSheet=Helper.reviseFilePath(System.getProperty(CMDL_SHEET));
    	String logPath=Helper.reviseFilePath(System.getProperty(CMDL_LOGPATH));
    	FileUtil.createNew(logPath);
    	if(StringUtils.isBlank(funcType)){
			logger.error("please set argument -Dfunc, details see readme.");
			Helper.readme("readme.md");
			return;
    	}
    	if(FileUtil.exists(downloadFile,expectedFile)){
    		IFuncChecker schecker;
    		String logFullName=Helper.reviseFilePath(logPath+System.getProperty("file.separator"));
    		if(funcType.toLowerCase().startsWith(FUNC_EXCEL)){
    			logger.info("=====================check export to excel =====================");
    			schecker=new ExcelChecker(downloadFile,expectedFile);
    			schecker.checker();
    			FileUtil.writeContentToEmptyFile(logFullName+"excelChecker.log", schecker.getExecutionStatus());
    		}else if(funcType.toLowerCase().startsWith(FUNC_VALIDATION)){
    			logger.info("=====================check validation rule =====================");
    			schecker=new ExcelChecker(downloadFile,expectedFile);
    			schecker.checker();
    			FileUtil.writeContentToEmptyFile(logFullName+"valRuleChecker.log", schecker.getExecutionStatus());
    		}else{
        		logger.error("value of argument -Dfunc are not supported, details see readme.");
        		Helper.readme("readme.md");
        	}
		}
		if(FileUtil.exists(allInOneFile)){
			List<Map<Integer,String>> checkers=ExcelUtil.getObjects(allInOneFile,allInOneSheet,false);
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
	    		if(funcType.toLowerCase().startsWith(FUNC_EXCEL)){
	    			logger.info("=====================check export to excel =====================");
	    			for(int i=1;i<checkers.size();i++){
		        		map=checkers.get(i);
		        		achecker=new ExcelChecker(map.get(exec_DownloadFile_Index),map.get(exec_ExpectationFile_Index));
		        		achecker.checker();
		        		map.put(executionStatus_Index, achecker.getExecutionStatus());
		        		logger.info(achecker.toString());
		        	}
	    			ExcelUtil.writeObjectsToExcel(checkers, allInOneFile, allInOneSheet, false);
	    		}else if(funcType.toLowerCase().startsWith(FUNC_VALIDATION)){
	    			logger.info("=====================check validation rule =====================");
	    			for(int i=1;i<checkers.size();i++){
		        		map=checkers.get(i);
		        		achecker=new ValidationRuleChecker(map.get(exec_DownloadFile_Index),map.get(exec_ExpectationFile_Index));
		        		achecker.checker();
		        		map.put(executionStatus_Index, achecker.getExecutionStatus());
		        		logger.info(achecker.toString());
		        	}
	    			ExcelUtil.writeObjectsToExcel(checkers, allInOneFile, allInOneSheet, false);
	    		}else{
	        		logger.error("value of argument -Dfunc are not supported, details see readme.");
	        		Helper.readme("readme.md");
	        	}
	    	}
    	}
		end=System.currentTimeMillis();
		logger.info("total time(sec):"+(end-begin)/1000.00F);
    }
}
