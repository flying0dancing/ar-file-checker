package com.lombardrisk.arproduct.poi;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.InvalidOperationException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.ooxml.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/**
 * A rudimentary XLSX -> CSV processor modeled on the
 * POI sample program XLS2CSVmra from the package
 * org.apache.poi.hssf.eventusermodel.examples.
 * As with the HSSF version, this tries to spot missing
 *  rows and cells, and output empty entries for them.
 * <p>
 * Data sheets are read using a SAX parser to keep the
 * memory footprint relatively small, so this should be
 * able to read enormous workbooks.  The styles table and
 * the shared-string table must be kept in memory.  The
 * standard POI styles table class is used, but a custom
 * (read-only) class is used for the shared string table
 * because the standard POI SharedStringsTable grows very
 * quickly with the number of unique strings.
 * <p>
 * For a more advanced implementation of SAX event parsing
 * of XLSX files, see {@link XSSFEventBasedExcelExtractor}
 * and {@link XSSFSheetXMLHandler}. Note that for many cases,
 * it may be possible to simply use those with a custom 
 * {@link SheetContentsHandler} and no SAX code needed of
 * your own!
 */
public class XLSX2CSV {
	private static final Logger logger = LoggerFactory.getLogger(XLSX2CSV.class);
	/**
     * Uses the XSSF Event SAX helpers to do most of the work
     *  of parsing the Sheet XML, and outputs the contents
     *  as a (basic) CSV.
     */
    private class SheetToCSV implements SheetContentsHandler {
        private boolean firstCellOfRow;
        private int currentRow = -1;
        private int currentCol = -1;
        private List<String> row;
        private List<List<String>> content=new ArrayList<List<String>>();
        
        public List<List<String>> getSheetContent(){
        	return content;
        }
        private void outputMissingRows(int number) {
            for (int i=0; i<number; i++) {
                for (int j=0; j<minColumns; j++) {
                    output.append(',');
                }
                output.append('\n');
            }
        }

        @Override
        public void startRow(int rowNum) {
            // If there were gaps, output the missing rows
          //  outputMissingRows(rowNum-currentRow-1);
            // Prepare for this row
            firstCellOfRow = true;
            currentRow = rowNum;
            currentCol = -1;
            
        }

        @Override
        public void endRow(int rowNum) {
            // Ensure the minimum number of columns
            for (int i=currentCol; i<minColumns; i++) {
               // output.append(',');
                row.add("");
            }
            //output.append('\n');
            content.add(row);
        }

        @Override
        public void cell(String cellReference, String formattedValue,
                XSSFComment comment) {
            if (firstCellOfRow) {
                firstCellOfRow = false;
                row=new ArrayList<String>();
            } else {
                //output.append(',');
            }

            // gracefully handle missing CellRef here in a similar way as XSSFCell does
            if(cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }

            // Did we miss any cells?
            int thisCol = (new CellReference(cellReference)).getCol();
            int missedCols = thisCol - currentCol - 1;
            for (int i=0; i<missedCols; i++) {
                //output.append(',');
                row.add("");
            }
            currentCol = thisCol;
            
            // Number or string?
          /*  try {
                //noinspection ResultOfMethodCallIgnored
                Double.parseDouble(formattedValue);
                output.append(formattedValue);
            } catch (NumberFormatException e) {
                output.append('"');
                output.append(formattedValue);
                output.append('"');
            }*/
            row.add(formattedValue);
        }
    }


    ///////////////////////////////////////

    private final OPCPackage xlsxPackage;

    /**
     * Number of columns to read starting with leftmost
     */
    private final int minColumns;

    /**
     * Destination for data
     */
    private final PrintStream output;
    
    //private final List<List<String>> content;
    private final Map<String,List<List<String>>> allContent;

    /**
     * Creates a new XLSX -> CSV examples
     *
     * @param pkg        The XLSX package to process
     * @param output     The PrintStream to output the CSV to
     * @param minColumns The minimum number of columns to output, or -1 for no minimum
     */
    public XLSX2CSV(OPCPackage pkg, PrintStream output,Map<String,List<List<String>>> allContent, int minColumns) {
        this.xlsxPackage = pkg;
        this.output = output;
        this.minColumns = minColumns;
        this.allContent=allContent;
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param styles The table of styles that may be referenced by cells in the sheet
     * @param strings The table of strings that may be referenced by cells in the sheet
     * @param sheetInputStream The stream to read the sheet-data from.

     * @exception java.io.IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     * @throws SAXException if parsing the XML data fails.
     */
    public void processSheet(Styles styles, SharedStrings strings,  SheetContentsHandler sheetHandler,  InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                  styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
         } catch(ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
         }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException If reading the data from the package fails.
     * @throws SAXException if parsing the XML data fails.
     */
    public void process() throws IOException, OpenXML4JException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            try (InputStream stream = iter.next()) {
                String sheetName = iter.getSheetName();
                
                this.output.println();
                this.output.println(sheetName + " [index=" + index + "]:");
                SheetToCSV stc=new SheetToCSV();
                processSheet(styles, strings, stc, stream);
                this.allContent.put(sheetName, stc.getSheetContent());
            }
            ++index;
        }
    }
    public void processOneSheet(String sheetname) throws IOException, OpenXML4JException, SAXException {
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        if(sheetname==null || sheetname.equals("")){
        	if(iter.hasNext()){
        		try (InputStream stream = iter.next()) {
                    String sheetName = iter.getSheetName();
                    this.output.println(sheetName + " [index=0]:");
                    SheetToCSV stc=new SheetToCSV();
                    processSheet(styles, strings, stc, stream);
                    this.allContent.put(sheetName, stc.getSheetContent());
                }
        	}
        }else{
        	int index = 0;
            while (iter.hasNext()) {
                try (InputStream stream = iter.next()) {
                    String sheetName = iter.getSheetName();
                    this.output.println(sheetName + " [index=" + index + "]:");
                    if(sheetName.equalsIgnoreCase(sheetname)){
                    	SheetToCSV stc=new SheetToCSV();
                        processSheet(styles, strings, stc, stream);
                        this.allContent.put(sheetName, stc.getSheetContent());
                    }
                }
                ++index;
            }
        }
        
    }
    
    public static Map<String,List<List<String>>> process(String fileFullName){
    	OPCPackage p ;
    	Map<String,List<List<String>>> all=new HashMap<String,List<List<String>>>();
    	try{
    		p = OPCPackage.open(fileFullName, PackageAccess.READ);
    		XLSX2CSV xlsx2csv = new XLSX2CSV(p, System.out,all, -1);
    		xlsx2csv.process();
    		p.clearRelationships();
            p.close();
    	}catch (InvalidOperationException e) {
			logger.error(e.getMessage(),e);
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} catch (OpenXML4JException e) {
			logger.error(e.getMessage(),e);
		} catch (SAXException e) {
			logger.error(e.getMessage(),e);
		}
    	return all;
    }

    
    public static Map<String,List<List<String>>> processOneSheet(String fileFullName,String sheetName) {
    	OPCPackage p ;
    	Map<String,List<List<String>>> all=new HashMap<String,List<List<String>>>();
    	try {
    		p = OPCPackage.open(fileFullName, PackageAccess.READ);
            XLSX2CSV xlsx2csv = new XLSX2CSV(p, System.out,all, -1);
            xlsx2csv.processOneSheet(sheetName);
            p.clearRelationships();
            p.close();
        } catch (InvalidOperationException e) {
			logger.error(e.getMessage(),e);
		} catch (InvalidFormatException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} catch (OpenXML4JException e) {
			logger.error(e.getMessage(),e);
		} catch (SAXException e) {
			logger.error(e.getMessage(),e);
		}
    	return all;
    }
    public static void main(String[] args) throws Exception {
        

        File xlsxFile = new File("Z:\\ProductLine\\FED\\TestResults\\FED_1.14.2\\Auto\\1.14.2_AR1.16.0b75\\download\\US FED Reserve(ExportValidation)_arfileck\\FFIEC009_v2_2999_09302016_validations_checked.xlsx");
        if (!xlsxFile.exists()) {
            System.err.println("Not found or not a file: " + xlsxFile.getPath());
            return;
        }

        int minColumns = -1;
        if (args.length >= 2)
            minColumns = Integer.parseInt(args[1]);

        //XLSX2CSV.processOneSheet(xlsxFile.getPath(), null);
        XLSX2CSV.process(xlsxFile.getPath());
    }
}
