package org.example;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelReadWriteExample {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\HP\\Downloads\\Demo.xlsx";  // Excel file path

        try {
            // ✅ Read Excel
            FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            System.out.println("Reading Excel Data:");
            for (Row row : sheet) {
                for (Cell cell : row) {
                    System.out.print(cell.toString() + "\t");
                }
                System.out.println();
            }
            fis.close();

            // ✅ Write to Excel (Add new row)
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            Cell cell1 = newRow.createCell(0);
            cell1.setCellValue("New Course");
            Cell cell2 = newRow.createCell(1);
            cell2.setCellValue("Active");

            // Save changes
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();

            System.out.println("✅ Data written successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
