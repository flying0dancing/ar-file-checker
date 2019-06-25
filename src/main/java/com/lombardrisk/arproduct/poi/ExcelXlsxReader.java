package com.lombardrisk.arproduct.poi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;  
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;  
import org.apache.poi.xssf.model.SharedStringsTable;  
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;  
import org.xml.sax.InputSource;  
import org.xml.sax.SAXException;  
import org.xml.sax.XMLReader;  
import org.xml.sax.helpers.DefaultHandler;  
import org.xml.sax.helpers.XMLReaderFactory;  

public class ExcelXlsxReader extends DefaultHandler{
	private static final Logger logger = LoggerFactory.getLogger(ExcelXlsxReader.class);
	enum CellDataType {BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL}
	private SharedStringsTable sst;
	private String lastContents;  
	private boolean nextIsString; 
	private String filePath = "";
	private int sheetIndex = -1;//工作表索引
	private String sheetName = "";
	private int totalRows=0;
	private List<List<String>> list=new ArrayList<List<String>>();
	private List<String> rowlist  = new ArrayList<String>(); // 一行内cell集合
	private boolean flag = false;//判断整行是否为空行的标记
	private int curRow = 0;//当前行
	private int curCol = 0;//当前col
	private boolean isTElement; //T元素标识
	//private String exceptionMessage;//异常信息，如果为空则表示没有异常
	private CellDataType nextDataType = CellDataType.SSTINDEX;//单元格数据类型，默认为字符串类型
	private final DataFormatter formatter = new DataFormatter();
	private short formatIndex;
	private String formatString;
	private String preRef = null, ref = null;//定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
	private String maxRef = null;// 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格 
	private StylesTable stylesTable; //单元格
	
	public String getSheetName(){
		return sheetName;
	}
	
    public int getTotalRows()
    {
    	return totalRows;
    }
	 public void process(String filename) throws Exception {
		 filePath = filename;
		 OPCPackage pkg = OPCPackage.open(filename,PackageAccess.READ);
		 XSSFReader xssfReader = new XSSFReader(pkg);
		 stylesTable = xssfReader.getStylesTable();
		 SharedStringsTable sst = xssfReader.getSharedStringsTable();
		 XMLReader parser = fetchSheetParser(sst);
		 
		 XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		 while (sheets.hasNext()) { //遍历sheet
		 	curRow =0; //标记初始行为第一行
		 	sheetIndex++;
		 	InputStream sheet = sheets.next(); //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
		 	sheetName = sheets.getSheetName();
		 	InputSource sheetSource = new InputSource(sheet);
		 	parser.parse(sheetSource); //解析excel的每条记录，在这个过程中startElement()、characters()、endElement()这三个函数会依次执行
		 	sheet.close();
		 }
		 pkg.clearRelationships();
		 pkg.revert();
	}
	 public void removeSheet(String filename,String sheetname) {
		 filePath = filename;
		 OPCPackage pkg=null;
		 XSSFReader xssfReader;
		try {
			pkg = OPCPackage.open(filename);
			xssfReader = new XSSFReader(pkg);
			stylesTable = xssfReader.getStylesTable();
			 SharedStringsTable sst = xssfReader.getSharedStringsTable();
			 //XMLReader parser = fetchSheetParser(sst);
			 XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
			 if(StringUtils.isBlank(sheetname) && sheets.hasNext()){
				 curRow =0; //标记初始行为第一行
				 sheetIndex++;
				 sheets.next(); //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
				 sheetName = sheets.getSheetName();
				 sheets.remove();
				
			 }else{
				 while (sheets.hasNext()) { //遍历sheet
					curRow =0; //标记初始行为第一行
					sheetIndex++;
					sheets.next(); //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
					sheetName = sheets.getSheetName();
					if(sheetName.equalsIgnoreCase(sheetname)){
						sheets.remove();
					}
				}
			 }
		} catch ( IOException | OpenXML4JException  e) {
			logger.error(e.getMessage(),e);
		}finally{
			pkg.clearRelationships();
			pkg.revert();
		}
		
	 }
	 
	 public List<List<String>> processOneSheet(String filename,String sheetname){
		 filePath = filename;
		 OPCPackage pkg=null;
		 XSSFReader xssfReader;
		try {
			pkg = OPCPackage.open(filename);
			xssfReader = new XSSFReader(pkg);
			stylesTable = xssfReader.getStylesTable();
			 SharedStringsTable sst = xssfReader.getSharedStringsTable();
			 XMLReader parser = fetchSheetParser(sst);
			 XSSFReader.SheetIterator sheets = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
			 if(StringUtils.isBlank(sheetname) && sheets.hasNext()){
				 curRow =0; //标记初始行为第一行
				 sheetIndex++;
				 InputStream sheet = sheets.next(); //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
				 sheetName = sheets.getSheetName();
				 InputSource sheetSource = new InputSource(sheet);
				 parser.parse(sheetSource); //解析excel的每条记录，在这个过程中startElement()、characters()、endElement()这三个函数会依次执行
				 sheet.close();
			 }else{
				 while (sheets.hasNext()) { //遍历sheet
					curRow =0; //标记初始行为第一行
					sheetIndex++;
					InputStream sheet = sheets.next(); //sheets.next()和sheets.getSheetName()不能换位置，否则sheetName报错
					sheetName = sheets.getSheetName();
					if(sheetName.equalsIgnoreCase(sheetname)){
						InputSource sheetSource = new InputSource(sheet);
						parser.parse(sheetSource); //解析excel的每条记录，在这个过程中startElement()、characters()、endElement()这三个函数会依次执行
						sheet.close();
					}
				}
			 }
			 
		} catch ( IOException | OpenXML4JException | SAXException e) {
			logger.error(e.getMessage(),e);
		}finally{
			pkg.clearRelationships();
			pkg.revert();
			System.gc();
		}
		
		return list;
		 
	}
	 public XMLReader fetchSheetParser(SharedStringsTable sst)  
	            throws SAXException {  
	        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");  
	        this.sst = sst;  
	        parser.setContentHandler(this);  
	        return parser;  
	}
	 @Override
	 public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		 // c => 单元格  
	        if ("c".equals(name)) {  
	            // 前一个单元格的位置  
	            if (preRef == null) {  
	                preRef =getFirstCell(attributes.getValue("r")) ;  
	            } else {  
	                preRef = ref;  
	            }  
	            // 当前单元格的位置  
	            ref = attributes.getValue("r");  
	            //set first null cells
	            int len = countNullCell(ref, preRef);
				if(rowlist.size()==0){
					for (int i = 0; i <= len; i++) {
						rowlist.add(curCol, "");
						curCol++;
					}
				}
				
	            // 设定单元格类型  
	            this.setNextDataType(attributes);  
	            // Figure out if the value is an index in the SST  
	            String cellType = attributes.getValue("t");  
	            if (cellType != null && cellType.equals("s")) {  
	                nextIsString = true;  
	            } else {  
	                nextIsString = false;  
	            }  
	        }  
	  
	        // 当元素为t时  
	        if ("t".equals(name)) {  
	            isTElement = true;  
	        } else {  
	            isTElement = false;  
	        }  
	  
	        // 置空  
	        lastContents = "";  
	 }


	private void setNextDataType(Attributes attributes) {
		nextDataType = CellDataType.NUMBER;  
        formatIndex = -1;  
        formatString = null;  
        String cellType = attributes.getValue("t");  
        String cellStyleStr = attributes.getValue("s");  
        String columData = attributes.getValue("r");  
  
        if ("b".equals(cellType)) {  
            nextDataType = CellDataType.BOOL;  
        } else if ("e".equals(cellType)) {  
            nextDataType = CellDataType.ERROR;  
        } else if ("inlineStr".equals(cellType)) {  
            nextDataType = CellDataType.INLINESTR;  
        } else if ("s".equals(cellType)) {  
            nextDataType = CellDataType.SSTINDEX;  
        } else if ("str".equals(cellType)) {  
            nextDataType = CellDataType.FORMULA;  
        }  
  
        if (cellStyleStr != null) {  
            int styleIndex = Integer.parseInt(cellStyleStr);  
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);  
            formatIndex = style.getDataFormat();  
            formatString = style.getDataFormatString();  
  
            if ("m/d/yy" == formatString) {  
                nextDataType = CellDataType.DATE;  
                formatString = "yyyy-MM-dd hh:mm:ss.SSS";  
            }  
  
            if (formatString == null) {  
                nextDataType = CellDataType.NULL;  
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);  
            }  
        }  
		
	}
	/***
	 * 得到单元格对应的索引值或是内容值
	 * 如果单元格类型是字符串、INLINESTR、数字、日期，lastIndex则是索引值
	 * 如果单元格类型是布尔值、错误、公式，lastIndex则是内容值
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		lastContents  += new String(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
	
		if (isTElement) {
			//将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
			String value = lastContents.trim();
			rowlist.add(curCol, value);
			curCol++;
			isTElement = false;
			//如果里面某个单元格含有值，则标识该行不为空行
			if (value != null && !"".equals(value)) {
				flag = true;
			}
		}else if ("v".equals(name)) {
			//v => 单元格的值，如果单元格是字符串，则v标签的值为该字符串在SST中的索引
			String value = this.getDataValue(lastContents.trim(), "");//根据索引值获取对应的单元格值
			if (!ref.equals(preRef)) {
				int len = countNullCell(ref, preRef);
				for (int i = 0; i < len; i++) {
					rowlist.add(curCol, "");
					curCol++;
				}
			}
			rowlist.add(curCol, value);
			curCol++;
			if (value != null && !"".equals(value)) {
				flag = true;
			}
		}else{
			//如果标签名称为row，这说明已到行尾，调用optRows()方法
			if ("row".equals(name)) {
				/*if (maxRef == null) {
					maxRef = ref;
				}else{
					if(ref.compareTo(maxRef)>0){
						maxRef=ref;
					}
				}
				//补全一行尾部可能缺失的单元格
				if (maxRef != null) {
					int len = countNullCell(maxRef, ref);
					for (int i = 0; i <= len; i++) {
						rowlist.add(curCol, "");
						curCol++;
					}
				}*/
				if (flag){ //该行不为空行且该行不是第一行，则发送（第一行为列名，需要）
					list.add(rowlist);
					totalRows++;
				}
				//setting new
				rowlist=new ArrayList<String>();
				curRow++;
				curCol = 0;
				preRef = null;
				ref = null;
				flag=false;
			}
			
			
		}
	}
	
private String getFirstCell(String ref){
	String a=ref.replaceAll(".*(\\d+)", "$1");
	a="A"+a;
	return a;
}
/***
 * 计算两个单元格之间的单元格数目(同一行) 
 * @param ref2
 * @param preRef2
 * @return
 */
	private int countNullCell(String ref2, String preRef2) {
		//excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
		String xfd = ref2.replaceAll("\\d+", "");  
        String xfd_1 = preRef2.replaceAll("\\d+", "");  
  
        xfd = fillChar(xfd, 3, '@', true);  
        xfd_1 = fillChar(xfd_1, 3, '@', true);  
  
        char[] letter = xfd.toCharArray();  
        char[] letter_1 = xfd_1.toCharArray();  
        int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);  
        return res - 1;  
	}

/***
 * 
 * @param value 单元格的值（这时候是一串数字） 
 * @param thisStr 一个空字符串
 * @return
 */
	@SuppressWarnings("deprecation")
	private String getDataValue(String value, String thisStr) {
		switch (nextDataType) {  
        // 这几个的顺序不能随便交换，交换了很可能会导致数据错误  
        case BOOL:  
            char first = value.charAt(0);  
            thisStr = first == '0' ? "FALSE" : "TRUE";  
            break;  
        case ERROR:  
            thisStr = "\"ERROR:" + value.toString() + '"';  
            break;  
        case FORMULA:  
            thisStr = '"' + value.toString() + '"';  
            break;  
        case INLINESTR:  
            XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());  
  
            thisStr = rtsi.toString();  
            rtsi = null;  
            break;  
        case SSTINDEX:  
            String sstIndex = value.toString();  
            try {  
                int idx = Integer.parseInt(sstIndex);  
                XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));  
                thisStr = rtss.toString();  
                rtss = null;  
            } catch (NumberFormatException ex) {  
                thisStr = value.toString();  
            }  
            break;  
        case NUMBER:  
            if (formatString != null) {  
                thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();  
            } else {  
                thisStr = value;  
            }  
  
            thisStr = thisStr.replace("_", "").trim();  
            break;  
        case DATE:  
            thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);  
  
            // 对日期字符串作特殊处理  
            thisStr = thisStr.replace(" ", "T");  
            break;  
        default:  
            thisStr = " ";  
  
            break;  
        }  
  
        return thisStr;  
	}
	public String fillChar(String str, int len, char let, boolean isPre) {
		int len_1 = str.length();
		if (len_1 < len) {
			if (isPre) {
				for (int i = 0; i < (len - len_1); i++) {
					str = let + str;
				}
			} else {
				for (int i = 0; i < (len - len_1); i++) {
					str = str + let;
				}
			}
		}
		return str;   
	}
	//public String getExceptionMessage() {
	//	return exceptionMessage;
	//}
	//public abstract void getRows(String filePath,String sheetName,int sheetIndex, int curRow, List<String> rowList);
	

	  /** 
     * 测试方法 
     */  
    public static void main(String[] args) throws Exception {  
  
    	//String file = "Z:\\ProductLine\\FED\\TestResults\\FED_1.14.2\\Auto\\1.14.2_AR1.16.0b75\\download\\US FED Reserve(ExportValidation)_arfileck\\FFIEC009_v1_2999_12312015_validations.xlsx";
		String file="Z:\\ProductLine\\HKMA\\autoResults\\HKMA5.29.0_NewReturns-2\\download\\Hong Kong Monetary Authority(ExportToExcelApplyScale)\\HKMA_0001_T10BLGE_v1_20180330.xlsx";
    	ExcelXlsxReader reader = new ExcelXlsxReader();  
    	
    	List<List<String>> alist=reader.processOneSheet(file,null);
    	List<String> row=null;
    	for(int i=0;i<alist.size();i++){
    		row=alist.get(i);
    		for(int j=0;j<row.size();j++){
    			System.out.println("row:"+i+",column:"+j+", data:"+row.get(j));
    		}
    	}
    	
    }
	
}
