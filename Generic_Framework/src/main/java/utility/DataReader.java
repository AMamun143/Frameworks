package utility;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class DataReader {

    XSSFWorkbook wb;
    XSSFSheet sheet;

    File src = new File("path");

    public  DataReader(){
        try {
            FileInputStream fis = new FileInputStream(src);
            wb = new XSSFWorkbook(fis);

        }catch (Exception e){
            System.out.println(e.getMessage());

        }
    }


    public String getData(int sheetNumber, int row, int column) {

        sheet = wb.getSheetAt(sheetNumber);
        String data = sheet.getRow(row).getCell(column).getStringCellValue();

        return data;
    }

    public void writeBack(int sheetNumber, int row, int column, String cellValue) throws IOException {
        sheet = wb.getSheetAt(sheetNumber);

        sheet.getRow(row).createCell(column).setCellValue(cellValue);

        FileOutputStream fout = new FileOutputStream(src);
        wb.write(fout);

        fout.close();
    }

}