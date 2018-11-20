package com.lombardrisk.arproduct.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lombardrisk.arproduct.pojo.DatabaseServer;


/**
 * create a new instance of this class, its argument is csv file full name which must follow format is "CellName","SheetName","Instance","RowRef","ColumnRef"<br>
 * we can use {@link ExcelUtil}.getNameInfosToCsv(String excelFullName,String csvFullName) to get the csv
 * @author kun shen
 *
 */
public class CsvDBUtil {
	private final static Logger logger = LoggerFactory.getLogger(CsvDBUtil.class);
	private String tableName=null;
	private String csvPath=null;
	private DatabaseServer csvdb=null;
	private DBHelper dbHelper=null;
	public CsvDBUtil(String csvFullName){
		tableName=Helper.getFileNameWithoutSuffix(csvFullName);
		csvPath=Helper.getParentPath(csvFullName);
		csvdb=new DatabaseServer("csv",null, csvPath,null,null);
		dbHelper=new DBHelper(csvdb);
	}
	/***
	 * print duplicated cells, print empty string if no duplicated cells.
	 * @param dbHelper
	 * @param tableName
	 * @param logFullName
	 * @return
	 */
	public String printDuplicatedCells(){
		String duplicatedCellsLog="";
		/*String tableName=Helper.getFileNameWithoutSuffix(csvFullName);
		String csvPath=Helper.getParentPath(csvFullName);
		DatabaseServer csvdb=new DatabaseServer("csv",null, csvPath,null,null);
		DBHelper dbHelper=new DBHelper(csvdb);*/
		//String sql="select A.\"SheetName\",A.\"Instance\",A.\"RowRef\",A.\"ColumnRef\",A.\"CellName\" from "+tableName+" A right join ( select \"SheetName\",\"Instance\",\"RowRef\",\"ColumnRef\" ,COUNT(*) as Count from "+tableName+"  group by \"SheetName\",\"Instance\",\"RowRef\",\"ColumnRef\" having COUNT(*)>1) B on A.\"SheetName\"=B.\"SheetName\" and   A.\"RowRef\"=B.\"RowRef\" and A.\"ColumnRef\"=B.\"ColumnRef\" and A.\"Instance\" is null --and A.\"Instance\"=B.\"Instance\" where A.\"SheetName\" is not null ";
		String sql="select \"SheetName\",\"Instance\",\"RowRef\",\"ColumnRef\" from \""+tableName+"\"  group by \"SheetName\",\"Instance\",\"RowRef\",\"ColumnRef\" having COUNT(*)>1";
		if(dbHelper.connect()){
			List<List<String>> results= dbHelper.queryRecordset(sql);
			if(results!=null && results.size()>0){
				for(int i=0;i<results.size();i++){
					duplicatedCellsLog=duplicatedCellsLog+" fail:  duplicated 'refers to': '"+results.get(i).get(0)+(StringUtils.isNotBlank(results.get(i).get(1))?"|"+results.get(i).get(1):"")+"'!$"+results.get(i).get(3)+"$"+results.get(i).get(2);
					String instance="";
					if(StringUtils.isNotBlank(results.get(i).get(1))){
						instance=" and \"Instance\"='"+results.get(i).get(1)+"'";
					}
					sql="select \"CellName\" from \""+tableName+"\" where \"SheetName\"='"+results.get(i).get(0)+"'"+instance+" and \"RowRef\"='"+results.get(i).get(2)+"' and \"ColumnRef\"='"+results.get(i).get(3)+"'";
					List<String> duplicatedCells=dbHelper.queryRecords(sql);
					if(duplicatedCells!=null && duplicatedCells.size()>0){
						duplicatedCellsLog=duplicatedCellsLog+", which names are "+String.join(",", duplicatedCells)+";\n";
					}
				}
				logger.info("\n"+duplicatedCellsLog);
			}
			dbHelper.close();
		}
		
		return duplicatedCellsLog;
	}
	
	
	public List<String> getCellInfo(String cellName_expected, String instance_expected){
		List<String> cellInfo=null;
		List<List<String>> cellInfoList=null;
		//TODO
		Boolean allInstanceNull=false;
		List<String> instances=null;
		String sql="select distinct \"Instance\" from \""+tableName+"\" where \"CellName\" like '%"+cellName_expected+"'";;
		if(dbHelper.connect()){
			instances=dbHelper.queryRecords(sql);
			/*if(instances!=null && instances.size()>0){
				int instanceAllNull_i = 0;
				for(String instance:instances){
					instanceAllNull_i++;
					if(StringUtils.isBlank(instance)){allInstanceNull=true;}
				}
				if(instanceAllNull_i>1){allInstanceNull=false;}
			}else{allInstanceNull=true;}*/
			
			if(instances==null || instances.size()==0 || (instances.size()==1 && StringUtils.isBlank(instances.get(0))))
			{
				allInstanceNull=true;
			}
			if(allInstanceNull || StringUtils.isBlank(instance_expected)){
				sql="select \"CellName\",\"SheetName\",\"Instance\",\"RowRef\",\"ColumnRef\" from \""+tableName+ "\" where \"CellName\" like '%"+cellName_expected+"'";
			}else{
				sql="select \"CellName\",\"SheetName\",\"Instance\",\"RowRef\",\"ColumnRef\" from \""+tableName+ "\" where \"CellName\" like '%"+cellName_expected+"' and \"Instance\"='"+instance_expected+"'";
			}
			cellInfoList=dbHelper.queryRecordset(sql);
			if(cellInfoList!=null && cellInfoList.size()>0){
				String cellID_of_cellInfo=null;
				for(List<String> nameInfo:cellInfoList){
					cellID_of_cellInfo=nameInfo.get(0);
					cellID_of_cellInfo=cellID_of_cellInfo.replaceAll("^_{1,}(.*)", "$1");
					if(cellName_expected.equalsIgnoreCase(cellID_of_cellInfo)){
						cellInfo=nameInfo;
						break;
					}
				}
			}
			dbHelper.close();
		}
		return cellInfo;
	}


	public String getCsvPath() {
		return csvPath;
	}


	public void setCsvPath(String csvPath) {
		this.csvPath = csvPath;
	}


	public DatabaseServer getCsvdb() {
		return csvdb;
	}


	public void setCsvdb(DatabaseServer csvdb) {
		this.csvdb = csvdb;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
