package com.grocerystore.api.utils;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

public class ExcelDataReader {

    @DataProvider(name = "CategoriesData")
    public Object[][] getCategoriesData() {
        return getDataFromExcelSheet("src/test/resources/data/testdata.xlsx", "categories", true);
    }

    @DataProvider(name = "ProductsData")
    public Object[][] getProductsData() {
        return getDataFromExcelSheet("src/test/resources/data/testdata.xlsx",
                "products", true);
    }

    private Object[][] getDataFromExcelSheet(String file, String sheetName, boolean hasHeader) {

        Object[][] data = null;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {
                XSSFSheet sheet = workbook.getSheet(sheetName);
                int totalRows = sheet.getPhysicalNumberOfRows();
                System.out.println(totalRows);
                XSSFRow row = sheet.getRow(0);
                int totalColumns = row.getLastCellNum();
                System.out.println(totalColumns);
                Cell cell;
                int rows = totalRows - getStartRow(hasHeader);
                data = new Object[rows][totalColumns];
                int startRow = getStartRow(hasHeader);
                for (int i = startRow; i < totalRows; i++) {
                    for (int j = 0; j < totalColumns; j++) {
                        row = sheet.getRow(i);
                        cell = row.getCell(j);
                        switch (cell.getCellType()) {
                            case STRING:
                                data[i - startRow][j] = cell.getStringCellValue();
                                break;
                            case NUMERIC:
                                data[i - startRow][j] = cell.getNumericCellValue();
                                break;
                            case BOOLEAN:
                                data[i - startRow][j] = cell.getBooleanCellValue();
                                break;
                            default:
                                data[i - startRow][j] = cell.getStringCellValue();
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;

    }

    private int getStartRow(boolean hasHeader) {
        return hasHeader ? 1 : 0;
    }
}
