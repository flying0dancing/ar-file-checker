package com.lombardrisk.arproduct.utils;

import com.lombardrisk.arproduct.poi.ExcelXlsxReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ValidationRuleUtil {
    private final static Logger logger = LoggerFactory.getLogger(ValidationRuleUtil.class);


    public static String writeValidationRulesResult(String logPrefix, String fileFullName_expected, String sheetName_expected,String fileFullName_exported)
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
            wb_expected=ExcelUtil.openWorkbook(file_expected);

            ExcelXlsxReader excelXlsxReader=new ExcelXlsxReader();
            list_exported=excelXlsxReader.processOneSheet(fileFullName_exported, null);
            String exportedFileV="1.16.1";
            if(list_exported.get(0).get(0).equalsIgnoreCase("Rule Type")){
                //validation rules' export file are updated. started from agile reporter v1.16.2
                exportedFileV="1.16.2";
            }
            if(list_exported.get(0).get(0).equalsIgnoreCase("FILTER CRITERIA")){
                //validation rules' export file are updated. started from agile reporter v19.3
                exportedFileV="19.3";
            }
            String sheet_exported=excelXlsxReader.getSheetName();

            logger.info("exported file(need to be checked):"+fileFullName_exported);
            logger.info("expected file:"+fileFullName_expected);
            //delete sheet named log
            ExcelUtil.deleteSheet(wb_expected,ewTestLog);
            Sheet sheet_expected = null;
            Row row_expected=null;
            int amt_expected = 0,amt_exported=0;

            amt_expected = ExcelUtil.getLastRowNum(wb_expected,sheetName_expected);
            //amt_exported = getLastRowNum(wb_exported,null);
            amt_exported=list_exported.size();
            sheet_expected = ExcelUtil.getSheet(wb_expected,sheetName_expected);
            //sheet_exported=wb_exported.getSheetAt(0);
            if(sheet_expected==null){
                flagStr="error: cannot get first sheet ";
                return flagStr;
            }

            String check_expected=null, ruleType_expected=null, ruleNo_expected=null,ruleTypeNo_expected=null, instance_expected = null, rowIdStr_expected = null, status_expected = null, ruleMsg_expected = null;
            Map<Integer,List<String>> addresses;
            Iterator<Map.Entry<Integer, List<String>>> entries;
            Map.Entry<Integer, List<String>> map;
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
                check_expected=ExcelUtil.getCellValue_expected(row_expected,1);//column B
                if(!check_expected.equalsIgnoreCase("y")){continue;}

                //get expected info
                ruleNo_expected=ExcelUtil.getCellValue_expected(row_expected,3);//column D
                ruleType_expected=ExcelUtil.getCellValue_expected(row_expected,2);//column C
                ruleTypeNo_expected=getFullRuleNo(ruleType_expected,ruleNo_expected);
                if(ruleTypeNo_expected==null && exportedFileV.equals("1.16.1")){
                    logger.error(logPrefix+" Verify row:"+(i+1)+" fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal, Cross-Val");
                    row_expected.createCell(7).setCellValue("fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal, Cross-Val");//column H
                    row_expected.createCell(12).setCellValue("fail to find this Rule Type, should be any of Val, XVal, UVal, UXVal, Cross-Val");//column M
                    flagStr="fail";
                    continue;
                }

                instance_expected=ExcelUtil.getCellValue_expected(row_expected,4);//column E
                rowIdStr_expected=ExcelUtil.getCellValue_expected(row_expected,5);//column F
                status_expected=ExcelUtil.getCellValue_expected(row_expected,6);//column G
                ruleMsg_expected=ExcelUtil.getCellValue_expected(row_expected,8);//column I
                //find rule from exported excel
                if(exportedFileV.equals("1.16.1")){
                    addresses=ExcelUtil.findCell(list_exported,ruleTypeNo_expected);
                }else if(exportedFileV.equals("1.16.2")){
                    addresses=ExcelUtil.findCell(list_exported,ruleType_expected,0,ruleNo_expected,1);
                }else{
                    addresses=ExcelUtil.findCell(list_exported,ruleType_expected,4,ruleNo_expected,3);
                }

                entries = addresses.entrySet().iterator();
                if(addresses==null || addresses.size()<=0){
                    logger.error(logPrefix+" Verify row:"+(i+1)+" fail to find");
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
                    }else if(exportedFileV.equals("1.16.2")){
                        instance_D=row_exported.get(4);//column E
                        status_E=row_exported.get(5);//column F
                        msg_G=row_exported.get(7);//column H
                    }else{
                        //TODO
                        instance_D="";//use value of instance_G(generate by msg_G)
                        status_E=row_exported.get(0);//column A
                        msg_G=row_exported.get(2);//column H
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
                        //logger.info(logPrefix+" Verify row:"+(i+1)+" "+rst);
                        row_expected.createCell(12).setCellValue(rst);//column M
                        if(rst.equals("fail")){
                            flagStr="fail";
                            logger.info(logPrefix+" Verify row:"+(i+1)+" "+rst);
                        }
                        break;
                    }

                }
                if(search || rst==null){
                    flagStr="fail";
                    logger.error(logPrefix+" Verify row:"+(i+1)+" "+flagStr);
                    row_expected.createCell(7).setCellValue(flagStr);//column H
                    row_expected.createCell(12).setCellValue(flagStr);//column M
                }

            }


            Sheet log_expected = null;
            int uncheckederrNo=0;
            String shortRuleType=null;
            int startIndex=1;
            if(exportedFileV.equals("19.3")){
                startIndex=3;
            }
            for(int i=startIndex;i<amt_exported;i++){//skip head row
                row_exported=list_exported.get(i);
                if(StringUtils.isBlank(row_exported.get(0))){
                    continue;
                }
                no_A=null;checked_T=null;status_E = null; //clear info
                no_A=row_exported.get(0);//column A
                checked_T=row_exported.get(row_exported.size()-1);//column U
                if(exportedFileV.equals("1.16.1")){
                    status_E=row_exported.get(4);//column E
                }else if(exportedFileV.equals("1.16.2")){
                    status_E=row_exported.get(5);//column F
                }else{
                    status_E=row_exported.get(0);//column A
                    no_A=row_exported.get(4);//column E
                }


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
                    }else if(exportedFileV.equals("1.16.2")){
                        msg_G=row_exported.get(7);//column H
                        row_expected.createCell(2).setCellValue(no_A.replace("Reg ", ""));//RuleType
                        row_expected.createCell(3).setCellValue(row_exported.get(1));//RuleID column B
                    }else{
                        msg_G=row_exported.get(2);//column C
                        row_expected.createCell(2).setCellValue(no_A.replace("Reg ", ""));//RuleType
                        row_expected.createCell(3).setCellValue(row_exported.get(3));//RuleID column D
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

    /***
     * get instance from exported excel's Message column
     * @param message
     * @return "All Instance" if no PageInstance in Message column
     */
    protected static String getInstanceFromExportedExcel(String message){
        String str="All Instance";
        if(StringUtils.isNotBlank(message) && message.contains("[PageInstance")){
            str=message.replace("\n", "").replaceAll("\\[PageInstance:(.+?)\\].*","$1");
        }
        return str;
    }
    /**
     * get rowID from exported excgetShortRuleTypeel's Message column
     * @param message
     * @return return "" if no Row ID
     */
    protected static String getRowIDFromExportedExcel(String message){
        String str="";
        if(StringUtils.isNotBlank(message) && message.contains("[Row")){
            str=message.replace("\n", "").replaceAll(".*?\\[Row:(.+?)\\].*","$1");
        }
        return str;
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
                    rowId_tmp=ExcelUtil.getDisplayCellValue(c);
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
}
