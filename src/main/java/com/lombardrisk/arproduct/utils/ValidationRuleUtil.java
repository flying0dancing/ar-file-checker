package com.lombardrisk.arproduct.utils;

import com.lombardrisk.arproduct.poi.ExcelXlsxReader;
import com.lombardrisk.arproduct.pojo.ExportToVal;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ValidationRuleUtil {
    private final static Logger logger = LoggerFactory.getLogger(ValidationRuleUtil.class);

    public static String fileNotExists(String... fileFullName){
        String flagStr="";
        for(int i=0;i<fileFullName.length;i++){
            if(!FileUtil.exists(fileFullName[i])){
                flagStr="error: File Not Found "+fileFullName[i];
                break;
            }
        }
        return flagStr;
    }

    public static String writeValidationRulesResult(String logPrefix, String fileFullName_expected, String sheetName_expected,String fileFullName_exported)
    {
        String flagStr="pass";
        Workbook wb_expected;
        String ewTestLog="log";
        List<List<String>> list_exported;
        List<String> row_exported;
        ExportToVal row_exportedObj;
        try
        {
            File file_expected=new File(fileFullName_expected);
            //File file_exported=new File(fileFullName_exported);
            wb_expected=ExcelUtil.openWorkbook(file_expected);

            ExcelXlsxReader excelXlsxReader=new ExcelXlsxReader();
            list_exported=excelXlsxReader.processOneSheet(fileFullName_exported, null);
            List<ExportToVal> objs_exported=transferToObject(list_exported);


            String sheet_exported=excelXlsxReader.getSheetName();

            logger.info("exported file(need to be checked):"+fileFullName_exported);
            logger.info("expected file:"+fileFullName_expected);
            //delete sheet named log
            ExcelUtil.deleteSheet(wb_expected,ewTestLog);
            Sheet sheet_expected = null;
            Row row_expected=null;
            int amt_expected,amt_exported;

            amt_expected = ExcelUtil.getLastRowNum(wb_expected,sheetName_expected);

            amt_exported=objs_exported.size();
            sheet_expected = ExcelUtil.getSheet(wb_expected,sheetName_expected);
            //sheet_exported=wb_exported.getSheetAt(0);
            if(sheet_expected==null){
                flagStr="error: cannot get first sheet ";
                return flagStr;
            }

            String check_expected=null, ruleType_expected=null, ruleNo_expected=null,instance_expected = null, rowIdStr_expected = null, status_expected = null, ruleMsg_expected = null;
            int comparedRowCount=0;
            List<ExportToVal> addresses;
            Boolean search;
            String status_E = null, msg_G = null, rowID = null,rst=null, instance_G;//in exported file
            logger.info("Verify row:1 skip head row (expectedValue vs actualValue)");
            for(long i=1;i<=amt_expected;i++){
                //initial running
                search=true;
                rst=null;
                //part1 set expected info
                row_expected=sheet_expected.getRow((int) i);
                if(row_expected==null)continue;
                check_expected=ExcelUtil.getCellValue_expected(row_expected,1);//column B
                if(!check_expected.equalsIgnoreCase("y")){continue;}

                //get expected info
                ruleNo_expected=ExcelUtil.getCellValue_expected(row_expected,3);//column D
                ruleType_expected=ExcelUtil.getCellValue_expected(row_expected,2);//column C

                instance_expected=ExcelUtil.getCellValue_expected(row_expected,4);//column E
                rowIdStr_expected=ExcelUtil.getCellValue_expected(row_expected,5);//column F
                status_expected=ExcelUtil.getCellValue_expected(row_expected,6);//column G
                ruleMsg_expected=ExcelUtil.getCellValue_expected(row_expected,8);//column I
                //find rule from exported excel
                addresses=findCell(objs_exported,ruleType_expected,ruleNo_expected);
                if(addresses==null || addresses.size()<=0){
                    logger.error(logPrefix+" Verify row:"+(i+1)+" fail to find");
                    row_expected.createCell(7).setCellValue("fail to find");//column H
                    row_expected.createCell(12).setCellValue("fail to find");//column M
                    flagStr="fail";
                    continue;
                }
                int index=0;
                while( index<addresses.size() && search ){
                    search = false;//reset search for false means no need to search again.

                    row_exportedObj=addresses.get(index);

                    rst=null; //clear info
                    instance_G=row_exportedObj.getInstances();
                    status_E=row_exportedObj.getStatus();
                    msg_G=row_exportedObj.getMessage();
                    rowID=row_exportedObj.getExtendGridId();


                    if(instance_expected.equals("") || instance_expected.equalsIgnoreCase("All Instance") || instance_G.equals("")){
                        search=compareDifference(rowID,rowIdStr_expected);
                    }else{
                        if(msg_G.toLowerCase().startsWith("[pageinstance")){
                            if(instance_expected.equalsIgnoreCase("Each Instance")){
                                if(instance_G.equalsIgnoreCase(instance_expected)){
                                    search=compareDifference(msg_G,ruleMsg_expected);
                                }
                            }else{
                                if(instance_expected.equalsIgnoreCase(instance_G)){
                                    if(StringUtils.isNoneBlank(rowIdStr_expected,rowID)){
                                        search=compareDifference(rowID,rowIdStr_expected);
                                    }else{
                                        if((StringUtils.isBlank(rowIdStr_expected) && StringUtils.isNotBlank(rowID)) || (StringUtils.isNotBlank(rowIdStr_expected) && StringUtils.isBlank(rowID))){
                                            search = true;
                                            break;//break for fail
                                        }
                                    }
                                }else{search = true;}
                            }
                        }
                    }

                    if(!search && ( StringUtils.isBlank(rowIdStr_expected) ||  StringUtils.equals(rowID, rowIdStr_expected) )){
                        row_expected.createCell(7).setCellValue(status_E);//column H
                        row_expected.createCell(9).setCellValue(msg_G);//column J
                        //add flag at column U for checked row
                        addCheckStatus(list_exported, row_exportedObj);

                        rst=setValRuleCompareRst(status_expected,status_E,ruleMsg_expected,msg_G);
                        //logger.info(logPrefix+" Verify row:"+(i+1)+" "+rst);
                        row_expected.createCell(12).setCellValue(rst);//column M
                        if(rst.equals("fail")){
                            flagStr="fail";
                            logger.info(logPrefix+" Verify row:"+(i+1)+" fail");
                        }
                        comparedRowCount++;
                        break;
                    }
                    index++;
                }
                if(search || rst==null){
                    flagStr="fail";
                    logger.error(logPrefix+" Verify row:"+(i+1)+" fail");
                    row_expected.createCell(7).setCellValue(flagStr);//column H
                    row_expected.createCell(12).setCellValue(flagStr);//column M
                    comparedRowCount++;
                }

            }

            flagStr=writeExpectedLogSheet(wb_expected,ewTestLog,list_exported,objs_exported, comparedRowCount,flagStr);
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

    /***
     * compare same return false, compare different return true
     * @param a
     * @param b
     * @return
     */
    private static Boolean compareDifference(String a,String b){
        Boolean search=false;
        if(StringUtils.isBlank(a)){
            a="";
        }
        if(StringUtils.isBlank(b)){
            b="";
        }
        if(!a.equalsIgnoreCase(b)){
            search = true;
        }
        return search;
    }

    protected static String writeExpectedLogSheet(final Workbook wb_expected,final String ewTestLog,final List<List<String>> list_exported,final List<ExportToVal> objs_exported,final int comparedRowCount,String flagStr){

        Row row_expected=null;
        List<String> row_exported;
        ExportToVal row_exportedObj;

        int amt_exported=objs_exported.size();
        Sheet log_expected = null;
        int uncheckederrNo=0;
        if(comparedRowCount<amt_exported){
            for(int i=0;i<objs_exported.size();i++){
                row_exportedObj=objs_exported.get(i);
                if(StringUtils.isBlank(row_exportedObj.getCheckStatus()) && !row_exportedObj.getStatus().equalsIgnoreCase("pass")){
                    if(uncheckederrNo==0){
                        //create log sheet
                        flagStr="fail";
                        log_expected = wb_expected.createSheet(ewTestLog);
                        row_expected=log_expected.createRow(uncheckederrNo);
                        //set head row
                        String[] columnNames=new String[]{"CaseID(QC)","Check","RuleType","RuleID","Instance","RowID(For extendgrid)","Expected Status","Acctual Status","Expected Error","Acctual Error","Expected Cell Counts","Acctual Cell Counts","Test Result"};
                        for(int c=0;c<=12;c++){
                            row_expected.createCell(c).setCellValue(columnNames[c]);
                        }
                        log_expected.setAutoFilter(CellRangeAddress.valueOf("A1:M1"));
                    }
                    uncheckederrNo++;
                    row_expected=log_expected.createRow(uncheckederrNo);
                    row_expected.createCell(1).setCellValue("Y");//Check
                    row_expected.createCell(2).setCellValue(row_exportedObj.getType());//RuleType
                    row_expected.createCell(3).setCellValue(row_exportedObj.getId());//RuleID
                    row_expected.createCell(4).setCellValue(row_exportedObj.getInstances());//Instance
                    row_expected.createCell(5).setCellValue(row_exportedObj.getExtendGridId());//RowID
                    row_expected.createCell(6).setCellValue(row_exportedObj.getStatus());//Expected Status
                    row_expected.createCell(8).setCellValue(row_exportedObj.getMessage());//Expected Error

                    //add flag at column U for checked row
                    addCheckStatus(list_exported, row_exportedObj);

                }
            }
        }
        return flagStr;
    }

    protected static void addCheckStatus(final List<List<String>> list_exported,final ExportToVal row_exportedObj){
        List<String> row_exported;
        //add flag at column U for checked row
        row_exportedObj.setCheckStatus("checkedForValidation");//add flag at column U for checked row
        row_exported=list_exported.get(row_exportedObj.getRowIndex());
        for(int colIndex=row_exported.size();colIndex<20;colIndex++){
            row_exported.add("");
        }
        row_exported.add("checkedForValidation");
        list_exported.set(row_exportedObj.getRowIndex(),row_exported);//add flag at column U for checked row
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
     * get short rule type(Val,XVal,UVal,UXVal), return null if no matches. used in arVersion:16.1
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


    private static int getIndexOfColumn(List<String> headRow, String headStr){
        int i=0;
        String content;
        for(;i<headRow.size();i++){
            content=headRow.get(i);
            if(content.equalsIgnoreCase(headStr) ){
                break;
            }
        }
        return i;
    }

    /**
     * transfer excel list to object list
     * @param list_exported
     * @return
     */
    private static List<ExportToVal> transferToObject(List<List<String>> list_exported){
        List<ExportToVal> obj_exported=new ArrayList<ExportToVal>();
        List<String> headRow;
        int ruleType_colIndex,id_colIndex,level_colIndex,Status_colIndex,message_colIndex;
        int startRow=3;
        String exportedFileV="19.3";
        //validation rules' export file are updated. started from agile reporter v19.3
        headRow=list_exported.get(2);//have a empty row in index 3, ignore it
        ruleType_colIndex=getIndexOfColumn(headRow,"Rule Type");//column E
        id_colIndex=getIndexOfColumn(headRow,"id");//column D
        level_colIndex=getIndexOfColumn(headRow,"level");//column B
        Status_colIndex=getIndexOfColumn(headRow,"status");//column A
        message_colIndex=getIndexOfColumn(headRow,"details");//column C
        if(list_exported.get(0).get(0).equalsIgnoreCase("No")){
            exportedFileV="16.1";
            startRow=1;
            headRow=list_exported.get(0);
            id_colIndex=getIndexOfColumn(headRow, "No"); //column A
            level_colIndex=getIndexOfColumn(headRow,"level");//column B
            Status_colIndex=getIndexOfColumn(headRow,"status");//column E
            message_colIndex=getIndexOfColumn(headRow,"message");;//column G
        }
        if(list_exported.get(0).get(0).equalsIgnoreCase("Rule Type")){
            //validation rules' export file are updated. started from agile reporter v1.16.2
            exportedFileV="16.2";
            startRow=1;
            headRow=list_exported.get(0);
            ruleType_colIndex=getIndexOfColumn(headRow,"Rule Type");//column A
            id_colIndex=getIndexOfColumn(headRow,"id");//column B
            level_colIndex=getIndexOfColumn(headRow,"level");//column C
            Status_colIndex=getIndexOfColumn(headRow,"status");//column F
            message_colIndex=getIndexOfColumn(headRow,"message");;//column H
        }

        ExportToVal exportToValRule =null;
        List<String> row_exported;
        String shortRuleType,ruleId;
        String msg_G ="", rowID = "",instance_G= "All Instance";//in exported file, rowID means extendgridId
        for(int i=startRow;i<list_exported.size();i++){
            row_exported=list_exported.get(i);
            if(StringUtils.isBlank(row_exported.get(0))){
                continue;
            }
            exportToValRule =new ExportToVal();
            exportToValRule.setRowIndex(i);
            exportToValRule.setArVersion(exportedFileV);
            exportToValRule.setLevel(row_exported.get(level_colIndex));
            exportToValRule.setStatus(row_exported.get(Status_colIndex));
            msg_G=row_exported.get(message_colIndex);

            if(exportedFileV.equals("16.1")){
                shortRuleType=getShortRuleType(row_exported.get(id_colIndex)); //RuleType
                ruleId=row_exported.get(id_colIndex).replaceAll(".*?(\\d+)", "$1"); //RuleID
            }else{
                shortRuleType=row_exported.get(ruleType_colIndex).replaceAll("Reg ", ""); //RuleType
                ruleId=row_exported.get(id_colIndex);
            }
            exportToValRule.setType(shortRuleType);
            exportToValRule.setId(ruleId);

            if(StringUtils.isNotBlank(msg_G)){
                if(msg_G.contains("Message: N/A")){
                    msg_G="";
                }
                if(msg_G.startsWith("Message: ")){
                    msg_G=msg_G.replaceFirst("Message: ","");
                }
                if(msg_G.contains("Row:")){
                    rowID=msg_G.replace("\n", "").replaceAll(".*\\[Row:(.+?)\\].*", "$1");
                }
                if( msg_G.contains("PageInstance:")){
                    instance_G=msg_G.replace("\n", "").replaceAll("\\[PageInstance:(.+?)\\].*", "$1");
                }
            }
            exportToValRule.setMessage(msg_G);
            exportToValRule.setInstances(instance_G);
            exportToValRule.setExtendGridId(rowID);
            obj_exported.add(exportToValRule);
        }
       return obj_exported;
    }


    /**
     * find cell and return its row id (0-based) list
     * @param list_exported
     * @param searchRuleType
     * @param searchId
     * @return
     */
    public static List<ExportToVal> findCell(List<ExportToVal> list_exported,String searchRuleType,String searchId){
        List<ExportToVal> result=null;
        if(list_exported!=null && list_exported.size()>0){
            result=new ArrayList<ExportToVal>();
            ExportToVal obj;
            for(int i=0;i<list_exported.size();i++){
                obj=list_exported.get(i);
                if(obj.getType().equalsIgnoreCase(searchRuleType) && obj.getId().equalsIgnoreCase(searchId)){
                    result.add(obj);
                }
            }
        }
        return result;
    }


}
