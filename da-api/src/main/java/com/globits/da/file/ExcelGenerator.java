package com.globits.da.file;


import com.globits.da.dto.EmployeeDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.util.*;

public class ExcelGenerator {
    private static final Path root = Paths.get("E:\\BAI_TAP_LUYEN_TAP\\Thuc Tap Java OcenTech\\Bai Tap Lv 2\\L2_Backend\\uploads");
    private List<EmployeeDTO> listEmployee;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator(List<EmployeeDTO> listEmployee) {
        this.listEmployee = listEmployee;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() throws FileNotFoundException {
        sheet = workbook.createSheet("list-employee-database");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        //font.setBold(true);
        font.setColor(XSSFFont.SS_SUB);
        font.setFamily(10);
        font.setFontHeight(14);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Code", style);
        createCell(row, 2, "Name", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Phone", style);
        createCell(row, 5, "Age", style);

    }

    private void createCell(Row row, int columnCount, Object cellValue, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (cellValue instanceof Integer) {
            cell.setCellValue((Integer) cellValue);
        } else if (cellValue instanceof Long) {
            cell.setCellValue((Long) cellValue);
        } else if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else {
            cell.setCellValue((Boolean) cellValue);
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setColor(XSSFFont.DEFAULT_FONT_COLOR);
        font.setFontHeight(12);
        style.setFont(font);

        Collections.sort(listEmployee, new Comparator<EmployeeDTO>() {
            @Override
            public int compare(EmployeeDTO o1, EmployeeDTO o2) {
                return o1.getName().substring(o1.getName().lastIndexOf(' '))
                        .compareTo(o2.getName().substring(o2.getName().lastIndexOf(' ')));
            }
        });
        for (EmployeeDTO record : listEmployee) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getId(), style);
            createCell(row, columnCount++, record.getCode(), style);
            createCell(row, columnCount++, record.getName(), style);
            createCell(row, columnCount++, record.getEmail(), style);
            createCell(row, columnCount++, record.getPhone(), style);
            createCell(row, columnCount++, record.getAge(), style);

        }
        //ghi vào file trong lap top
        try (OutputStream os = Files.newOutputStream(Paths.get("E:\\BAI_TAP_LUYEN_TAP\\Thuc Tap Java OcenTech\\Bai Tap Lv 2\\test.xlsx"))) {
            workbook.write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }


    private static Object getCellValue(Cell cell, FormulaEvaluator evaluator) {
        CellValue cellValue = evaluator.evaluate(cell);
        switch (cellValue.getCellTypeEnum()) {
            case BOOLEAN:
                return cellValue.getBooleanValue();
            case NUMERIC:
                return cellValue.getNumberValue();
            case STRING:
                return cellValue.getStringValue();
            case BLANK:
                return "";
            case ERROR:
                return cellValue.getError(cell.getErrorCellValue()).getStringValue();
            // CELL_TYPE_FORMULA will never happen
            case FORMULA:
            default:
                return null;
        }
    }

    public static Object readFileExcel(@RequestParam("file") MultipartFile file) throws IOException {
        Path filepath = Paths.get(root.toString(), file.getOriginalFilename());
        OutputStream os = Files.newOutputStream(filepath);
        os.write(file.getBytes());
        Object resul = "";
        List<EmployeeDTO> listEmployeeDto = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(filepath.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
        Iterator<Row> rowIterator = sheet.rowIterator();
        int count = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (count == 0) {
                ++count;
                continue;
            } else {
                // Now let's iterate over the columns of the current row
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    resul += String.valueOf(getCellValue(cell, evaluator) + "-");
                }
                EmployeeDTO employeeDTO = new EmployeeDTO();
              //  listEmployeeDto.add(employeeDTO.formatTheLineToObject((String) resul));
                resul="";
                System.out.println(resul);
            }
        }
        return listEmployeeDto;
    }
}
