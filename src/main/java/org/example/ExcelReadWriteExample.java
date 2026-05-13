package org.example;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReadWriteExample {

    public static ExcelResponse readExcel(String filePath) {
        List<List<String>> excelData = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(cell.toString());
                }
                excelData.add(rowData);
            }

            return new ExcelResponse(true, "Data read successfully", excelData);

        } catch (IOException e) {
            return new ExcelResponse(false, "Error reading Excel: " + e.getMessage(), null);
        }
    }

    public static ExcelResponse writeExcel(String filePath, String col1, String col2) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            newRow.createCell(0).setCellValue(col1);
            newRow.createCell(1).setCellValue(col2);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            return new ExcelResponse(true, "Data written successfully", col1 + " | " + col2);

        } catch (IOException e) {
            return new ExcelResponse(false, "Error writing Excel: " + e.getMessage(), null);
        }
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\HP\\IdeaProjects\\Excelproject\\src\\main\\resources\\Demo.xlsx";

        // ✅ Read
        ExcelResponse readResponse = readExcel(filePath);
        System.out.println(readResponse);

        // ✅ Write
        ExcelResponse writeResponse = writeExcel(filePath, "New Course", "Active");
        System.out.println(writeResponse);
    }
}

