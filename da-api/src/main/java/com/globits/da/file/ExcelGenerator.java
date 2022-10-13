package com.globits.da.file;


import com.globits.da.Constants;
import com.globits.da.common.CreateCellIndexValue;
import com.globits.da.common.ErrorMessage;
import com.globits.da.common.GetCellValueIndex;
import com.globits.da.domain.Employee;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.service.impl.EmployeeServiceImpl;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.globits.da.common.ErrorMessage.SUCCESS;
import static com.globits.da.validate.ValidateEmployee.validateEmployee;

@Component
public class ExcelGenerator {
    private  List<EmployeeDto> employees;
    private  XSSFWorkbook workbook;
    private  XSSFSheet sheet;
    private final  DataFormatter formatter = new DataFormatter();
    private  EmployeeServiceImpl employeeService;

    @Autowired
    public ExcelGenerator(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    public ExcelGenerator(List<EmployeeDto> employees) {
        this.employees = employees;
        this.workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("list-employee-database");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(Font.SS_SUB);
        font.setFamily(10);
        font.setFontHeight(14);
        style.setFont(font);
        for (int i = 0; i < Constants.title.length; i++)
            createCell(row, i, Constants.title[i], style);
    }

    private void createCell(Row row, int columnIndex, Object cellValue, CellStyle style) {
        sheet.autoSizeColumn(columnIndex);
        Cell cell = row.createCell(columnIndex);
        if (cellValue instanceof Integer) {
            cell.setCellValue((Integer) cellValue);
        } else if (cellValue instanceof Long) {
            cell.setCellValue((Long) cellValue);
        } else if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean) cellValue);
        } else if (cellValue instanceof UUID) {
            cell.setCellValue(String.valueOf(cellValue));
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
        Collections.sort(employees);
        for (EmployeeDto dto : employees) {
            Row row = sheet.createRow(rowCount++);
            for (int i = 0; i < Constants.title.length; i++)
                createCellValue(row, i, dto, style);
        }
    }

    public void createCellValue(Row row, int columnIndex, EmployeeDto employeeDTO, CellStyle style) {
        CreateCellIndexValue createCellIndexValue = CreateCellIndexValue.values()[columnIndex];
        switch (createCellIndexValue) {
            case ID:
                createCell(row, columnIndex, employeeDTO.getId(), style);
                break;
            case CODE:
                createCell(row, columnIndex, employeeDTO.getCode(), style);
                break;
            case NAME:
                createCell(row, columnIndex, employeeDTO.getName(), style);
                break;
            case EMAIL:
                createCell(row, columnIndex, employeeDTO.getEmail(), style);
                break;
            case PHONE:
                createCell(row, columnIndex, employeeDTO.getPhone(), style);
                break;
            case AGE:
                createCell(row, columnIndex, employeeDTO.getAge(), style);
                break;
            case PROVINCE:
                createCell(row, columnIndex, employeeDTO.getProvinceDto().getId(), style);
                break;
            case DISTRICT:
                createCell(row, columnIndex, employeeDTO.getDistrictDto().getId(), style);
                break;
            case COMMUNE:
                createCell(row, columnIndex, employeeDTO.getCommuneDto().getId(), style);
                break;
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

    public List<String> importFileExcel(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        InputStream fileInputStream = file.getInputStream();
        employees = new ArrayList<>();
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheetAt(0);
        int rowCount;
        int cellIndex;
        boolean isValid;
        for (Row row : sheet) {
            rowCount = row.getRowNum();
            if (rowCount == 0) continue;
            EmployeeDto employeeDTO = new EmployeeDto();
            isValid = true;
            for (Cell cell : row) {
                cellIndex = cell.getColumnIndex();
                getCellValue(cellIndex, row, employeeDTO);
                ErrorMessage message = validateEmployee(employeeDTO, cellIndex);
                if (!SUCCESS.equals(message)) {
                    errors.add(message.getMessage() + " Hàng: " + rowCount + "  Cột: " + cellIndex);
                    isValid = false;
                }
            }
            if (isValid) {
                Employee employee = new Employee();
                employeeService.setEntity(employee, employeeDTO, Constants.INSERT);
                employeeService.save(employee);
            }
        }
        return errors;
    }

    public void getCellValue(int cellIndex, Row row, EmployeeDto employeeDTO) {
        GetCellValueIndex getCellValueIndex = GetCellValueIndex.values()[cellIndex];
        switch (getCellValueIndex) {
            case Code:
                employeeDTO.setCode(formatter.formatCellValue(row.getCell(cellIndex)));
                break;
            case Name:
                employeeDTO.setName(formatter.formatCellValue(row.getCell(cellIndex)));
                break;
            case Email:
                employeeDTO.setEmail(formatter.formatCellValue(row.getCell(cellIndex)));
                break;
            case Phone:
                employeeDTO.setPhone(formatter.formatCellValue(row.getCell(cellIndex)));
                break;
            case Age:
                employeeDTO.setAge(Integer.parseInt(formatter.formatCellValue(row.getCell(cellIndex))));
                break;
            case Province:
                ProvinceDto provinceDto = new ProvinceDto();
                provinceDto.setId(UUID.fromString(formatter.formatCellValue(row.getCell(cellIndex))));
                employeeDTO.setProvinceDto(provinceDto);
                break;
            case District:
                DistrictDto districtDto = new DistrictDto();
                districtDto.setId(UUID.fromString(formatter.formatCellValue(row.getCell(cellIndex))));
                employeeDTO.setDistrictDto(districtDto);
                break;
            case Commune:
                CommuneDto communeDto = new CommuneDto();
                communeDto.setId(UUID.fromString(formatter.formatCellValue(row.getCell(cellIndex))));
                employeeDTO.setCommuneDto(communeDto);
                break;
            default:
                break;
        }
    }
}
