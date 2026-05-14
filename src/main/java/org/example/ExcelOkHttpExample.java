package org.example;
import com.google.gson.Gson;
import okhttp3.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelOkHttpExample {

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

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

    public static ExcelResponse sendDataToApi(String url, Object data) {
        try {
            String json = gson.toJson(data);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder().url(url).post(body).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    return new ExcelResponse(true, "Data sent successfully", response.body().string());
                } else {
                    return new ExcelResponse(false, "API error: " + response.code(), null);
                }
            }
        } catch (IOException e) {
            return new ExcelResponse(false, "Error sending data: " + e.getMessage(), null);
        }
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\HP\\IdeaProjects\\Excelproject\\src\\main\\resources\\Demo.xlsx";

        // ✅ Read Excel
        ExcelResponse readResponse = readExcel(filePath);
        System.out.println(readResponse);

        // ✅ Write Excel
        ExcelResponse writeResponse = writeExcel(filePath, "New Course", "Active");
        System.out.println(writeResponse);

        // ✅ Send Data via OkHttp
        ExcelResponse apiResponse = sendDataToApi("https://httpbin.org/post", readResponse.getData());
        System.out.println(apiResponse);
    }
}


