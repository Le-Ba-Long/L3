package com.globits.da.rest;

import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.EmployeeCertificate;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.service.EmployeeCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.globits.da.common.ErrorMessage.*;


@RestController
@RequestMapping(value = "/api/employee-certificates")
public class RestEmployeeCertificateController {

    @Autowired
    private EmployeeCertificateService employeeCertificateService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseData<List<EmployeeCertificateDto>> getAll() {
        List<EmployeeCertificateDto> employeeCertificateDtoList = employeeCertificateService.getAll();
        if (employeeCertificateDtoList.isEmpty())
            return new ResponseData<>(LIST_IS_EMPTY, employeeCertificateDtoList);
        return new ResponseData<>(employeeCertificateDtoList);
    }

    @PostMapping()
    public ResponseData<EmployeeCertificateDto> insert(@RequestBody EmployeeCertificateDto employeeCertificateDto) {
        ResponseData<EmployeeCertificateDto> errorMessage = employeeCertificateService.insert(employeeCertificateDto);
        if (errorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errorMessage.getData());
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), null);
    }

    @PutMapping("/{id}")
    public ResponseData<EmployeeCertificateDto> update(@PathVariable(name = "id") UUID id, @RequestBody EmployeeCertificateDto employeeCertificateDto) {
        ResponseData<EmployeeCertificateDto> errorMessage = employeeCertificateService.update(id, employeeCertificateDto);
        if (errorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errorMessage.getData());
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseData<EmployeeCertificateDto> delete(@PathVariable("id") UUID id) {
        EmployeeCertificate employeeCertificate = employeeCertificateService.findById(id);
        if (employeeCertificateService.deleteById(id))
            return new ResponseData<>(modelMapper.map(employeeCertificate, EmployeeCertificateDto.class));
        return new ResponseData<>(ID_NOT_EXIST, null);
    }
}
