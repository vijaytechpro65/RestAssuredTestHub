package api.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
    private static Workbook workbook;
    private static Sheet sheet;

    // Method to initialize the Excel file and sheet
    public static void initExcel(String sheetName) {
        String filePath = ".\\src\\test\\resource\\testdata\\Api Data.xlsx"; // Your provided Excel file path
        try (FileInputStream fis = new FileInputStream(filePath)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' does not exist in the Excel file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load the Excel file. " + e.getMessage());
        }
    }

    // Method to get the number of rows
    public static int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }

    // Method to get the number of columns
    public static int getColumnCount() {
        return sheet.getRow(0).getPhysicalNumberOfCells();
    }

    // Method to get data as a 2D array
    public static String[][] getDataAsArray() {
        int rowCount = getRowCount();
        int colCount = getColumnCount();
        String[][] data = new String[rowCount - 1][colCount];

        for (int i = 1; i < rowCount; i++) { // Start from 1 to skip the header row
            Row row = sheet.getRow(i);
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j);
                data[i - 1][j] = cellToString(cell);
            }
        }
        return data;
    }

    // Method to get data as a List of Maps (each map represents a row with column names as keys)
    public static List<Map<String, String>> getDataAsList() {
        List<Map<String, String>> dataList = new ArrayList<>();
        Row headerRow = sheet.getRow(0);
        int rowCount = getRowCount();
        int colCount = getColumnCount();

        for (int i = 1; i < rowCount; i++) { // Start from 1 to skip the header row
            Row row = sheet.getRow(i);
            Map<String, String> dataMap = new HashMap<>();
            for (int j = 0; j < colCount; j++) {
                String key = headerRow.getCell(j).getStringCellValue();
                String value = cellToString(row.getCell(j));
                dataMap.put(key, value);
            }
            dataList.add(dataMap);
        }
        return dataList;
    }

    // Utility method to convert cell values to String
    private static String cellToString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // Return the date as a string
                } else {
                    // Handle the numeric value as string
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (int) numericValue) {
                        return String.valueOf((int) numericValue); // Remove decimal for whole numbers
                    } else {
                        return String.valueOf(numericValue); // Keep decimal for non-whole numbers
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    // Method to close the workbook
    public static void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
