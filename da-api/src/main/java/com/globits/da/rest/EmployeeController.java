package com.globits.da.rest;

import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Employee;
import com.globits.da.dto.*;
import com.globits.da.file.ExcelGenerator;
import com.globits.da.file.testReadFile;
import com.globits.da.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    EmployeeService employeeService;
    @Autowired
    ModelMapper modelMapper;
    private final Path root = Paths.get("E:\\BAI_TAP_LUYEN_TAP\\Thuc Tap Java OcenTech\\Bai Tap Lv 2\\L2_Backend\\uploads");

    @GetMapping("/list")
    public ResponseRequest<List<EmployeeDTO>> getAllEmployee() {
        List<EmployeeDTO> listEmployeeDto = employeeService.getAll()
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
        if (listEmployeeDto.isEmpty()) {
            return new ResponseRequest<>(
                    ErrorMessage.LIST_IS_EMPTY.getCode(),
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    listEmployeeDto);
        } else {
            return new ResponseRequest<>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    listEmployeeDto);
        }
    }

    @GetMapping("/lists")
    public ResponseEntity<List<Employee>> getAll(){
      return   ResponseEntity
              .status(HttpStatus.OK)
              .body(employeeService.getAllEmployee());
    }
    @PostMapping("/insert")
    public ResponseRequest<EmployeeDTO> insert(@RequestBody EmployeeDTO employeeDTO) {
        String resultErrorMessage = employeeService.insert(employeeDTO).getMessageError();
        int statusCode = employeeService.insert(employeeDTO).getStatusCode();
        if (resultErrorMessage.equals(ErrorMessage.SUCCESS.getMessage())) {
            return new ResponseRequest<>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    employeeDTO);
        }
        return new ResponseRequest<>(
                statusCode,
                resultErrorMessage,
                employeeDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseRequest<EmployeeDTO> update(@PathVariable("id") UUID id, @RequestBody EmployeeDTO employeeDTO) {
        String resultErrorMessage = employeeService.update(id, employeeDTO).getMessageError();
        int resultErrorCode = employeeService.update(id, employeeDTO).getStatusCode();
        if (resultErrorMessage.equals(ErrorMessage.SUCCESS.getMessage())) {
            return new ResponseRequest<EmployeeDTO>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    employeeDTO);
        }
        return new ResponseRequest<EmployeeDTO>(
                resultErrorCode,
                resultErrorMessage,
                employeeDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseRequest<EmployeeDTO> deleteEmployeeById(@PathVariable("id") UUID id) {
        Employee employee = employeeService.findById(id);
        if (employeeService.deleteById(id)) {
            LOGGER.info("Delete Success");
            return new ResponseRequest<>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    modelMapper.map(employee, EmployeeDTO.class));
        } else {
            LOGGER.info("Delete Not Success");
            return new ResponseRequest<>(ErrorMessage.ID_NOT_EXIST.getCode(),
                    ErrorMessage.ID_NOT_EXIST.getMessage(),
                    null);
        }
    }

    @GetMapping("/export-to-excel")
    public ResponseEntity<?> exportIntoExcelFile(HttpServletResponse response) throws IOException, IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employee" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        List<EmployeeDTO> listOfStudents = employeeService.getAll();
        ExcelGenerator generator = new ExcelGenerator(listOfStudents);
        generator.exportExcelFile(response);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseRequest(200, "Export Success", listOfStudents));

    }

    @PostMapping("/read-file-excel")
    public ResponseEntity<List<EmployeeDTO>> getAllTutorials(@RequestParam("file") MultipartFile file) {
        try {
            Path filepath = Paths.get(root.toString(), file.getOriginalFilename());
            OutputStream os = Files.newOutputStream(filepath);
            os.write(file.getBytes());
            List<EmployeeDTO> tutorials = testReadFile.excelToTutorials(filepath.toString());
            tutorials.forEach(x -> {
                System.out.println(x.getName());
            });
            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                employeeService.saveAll(tutorials);
                return new ResponseEntity<>(tutorials, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-list-employee-page")
    public Page<Employee> getListOfEmployeesByPage(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "2") Integer size) {
        Pageable pageable = PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "id"));
        return employeeService.getPage(pageable);
    }
}