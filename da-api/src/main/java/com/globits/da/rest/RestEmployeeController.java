package com.globits.da.rest;

import com.globits.da.domain.Employee;
import com.globits.da.dto.ContainQueryData;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.dto.search.EmployeeSearchDTO;
import com.globits.da.file.ExcelGenerator;
import com.globits.da.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.globits.da.common.ErrorMessage.*;

@RestController
@RequestMapping("/api/employees")
public class RestEmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseData<List<EmployeeDto>> getAllEmployee() {
        List<EmployeeDto> listEmployeeDto = employeeService.getAll();
        if (listEmployeeDto.isEmpty()) return new ResponseData<>(LIST_IS_EMPTY, listEmployeeDto);
        return new ResponseData<>(listEmployeeDto);
    }

    @PostMapping()
    public ResponseData<EmployeeDto> insert(@RequestBody EmployeeDto employeeDTO) {
        ResponseData<EmployeeDto> errorMessage = employeeService.insert(employeeDTO);
        if (errorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errorMessage.getData());
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), null);
    }

    @PutMapping("/{id}")
    public ResponseData<EmployeeDto> update(@PathVariable("id") UUID id, @RequestBody EmployeeDto employeeDTO) {
        ResponseData<EmployeeDto> errorMessage = employeeService.update(id, employeeDTO);
        if (errorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errorMessage.getData());
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseData<EmployeeDto> deleteEmployeeById(@PathVariable("id") UUID id) {
        Employee employee = employeeService.findById(id);
        if (employeeService.deleteById(id)) return new ResponseData<>(modelMapper.map(employee, EmployeeDto.class));
        return new ResponseData<>(ID_NOT_EXIST, null);
    }

    @GetMapping(value = "/export-to-excel")
    public void exportIntoExcelFile(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=employee" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
            List<EmployeeDto> employeeDTOs = employeeService.getAll();
            ExcelGenerator generator = new ExcelGenerator(employeeDTOs);
            generator.exportExcelFile(response);
        } catch (IOException e) {
            throw new IllegalArgumentException("export file thất bại");
        }

    }

    @PostMapping("/import-excel")
    public ResponseEntity<List<String>> importEmployee(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.importExcel(file));
    }

    @GetMapping("/get-list-employee-page")
    public Page<Employee> getListOfEmployeesByPage(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, @RequestParam(value = "size", required = false, defaultValue = "2") Integer size) {
        Pageable pageable = PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "id"));
        return employeeService.getPage(pageable);
    }

    @GetMapping(value = "list-employee-more-than-two-certificate")
    public ResponseData<List<EmployeeDto>> findAllByEmployeeHaveMoreThanTwoCertificate() {
        return new ResponseData<>(employeeService.findAllByEmployeeHaveMoreThanTwoCertificate());
    }

    @GetMapping(value = "count-employee-group-by-province/{id}")
    public ResponseData<List<EmployeeSearchDTO>> countEmployeeGroupByProvince(@PathVariable("id") UUID id) {
        return new ResponseData<>(employeeService.countEmployeeGroupByProvince(id));

    }
    @GetMapping(value = "calculate-percentage-of-employees-with-diplomas")
    public ResponseData<List<ContainQueryData>>calculatePercentageOfEmployeesWithDiplomas(){
        return new ResponseData<>(employeeService.calculatePercentageOfEmployeesWithDiplomas());
    }
}