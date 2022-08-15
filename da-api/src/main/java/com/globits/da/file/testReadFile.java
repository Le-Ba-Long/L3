package com.globits.da.file;

import com.globits.da.dto.EmployeeDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class testReadFile {
    @Autowired
    public EntityManager entityManager;
    public static String TYPE = "application/octet-stream";
    static String[] HEADERs = {"ID", "Code", "Name", "Email", "Phone", "Age"};
    static String SHEET = "Tutorials";

    public static boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }
    public static List<EmployeeDTO> excelToTutorials(String file) {

        try {
            InputStream inputStream = Files.newInputStream(new File(file).toPath());
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("list-employee-database");
            Iterator<Row> rows = sheet.iterator();
            List<EmployeeDTO> tutorials = new ArrayList<EmployeeDTO>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();
                EmployeeDTO tutorial = new EmployeeDTO();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                      //      tutorial.setId((long) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            tutorial.setCode(currentCell.getStringCellValue());
                            break;
                        case 2:
                            tutorial.setName(currentCell.getStringCellValue());
                            break;
                        case 3:
                            tutorial.setEmail(currentCell.getStringCellValue());
                            break;
                        case 4:
                            tutorial.setPhone(currentCell.getStringCellValue());
                            break;
                        case 5:
                            tutorial.setAge((int) currentCell.getNumericCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                tutorials.add(tutorial);
            }
            workbook.close();
            return tutorials;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}