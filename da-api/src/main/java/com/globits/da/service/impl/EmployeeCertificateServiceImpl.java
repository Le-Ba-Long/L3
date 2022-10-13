package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Constants;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Certificate;
import com.globits.da.domain.Employee;
import com.globits.da.domain.EmployeeCertificate;
import com.globits.da.domain.Province;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeCertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.EmployeeCertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.globits.da.common.ErrorMessage.SUCCESS;
import static com.globits.da.validate.ValidateEmployeeCertificate.isValidEmployeeCertificate;

@Service
public class EmployeeCertificateServiceImpl extends GenericServiceImpl<EmployeeCertificate, UUID> implements EmployeeCertificateService {
    @Autowired
    private EmployeeCertificateRepository employeeCertificateRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<EmployeeCertificateDto> getAll() {
        return employeeCertificateRepository.findAll()
                .stream().map(employeeCertificate -> modelMapper.map(employeeCertificate, EmployeeCertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(UUID id) {
        if (employeeCertificateRepository.existsById(id)) {
            employeeCertificateRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ResponseData<EmployeeCertificateDto> update(UUID id, EmployeeCertificateDto employeeCertificateDto) {
        ErrorMessage errorMessage = isValidEmployeeCertificate(employeeCertificateDto, id);
        if (!SUCCESS.equals(errorMessage)) return new ResponseData<>(errorMessage, null);
        EmployeeCertificate employeeCertificate = employeeCertificateRepository.getById(id);
        return new ResponseData<>(modelMapper.map(employeeCertificateRepository.save
                (setEntity(employeeCertificate, employeeCertificateDto, Constants.UPDATE)), EmployeeCertificateDto.class));
    }

    @Override
    public ResponseData<EmployeeCertificateDto> insert(EmployeeCertificateDto employeeCertificateDto) {
        ErrorMessage errorMessage = isValidEmployeeCertificate(employeeCertificateDto, null);
        if (!SUCCESS.equals(errorMessage))
            return new ResponseData<>(errorMessage, null);
        EmployeeCertificate employeeCertificate = new EmployeeCertificate();
        return new ResponseData<>(modelMapper.map(employeeCertificateRepository.save
                (setEntity(employeeCertificate, employeeCertificateDto, Constants.INSERT)), EmployeeCertificateDto.class));
    }

    public EmployeeCertificate setEntity(EmployeeCertificate entity, EmployeeCertificateDto dto, String action) {
        if (Constants.INSERT.equals(action)) {
            entity.setEmployee(modelMapper.map(dto.getEmployeeDTO(), Employee.class));
            entity.setProvince(modelMapper.map(dto.getProvinceDto(), Province.class));
            entity.setCertificate(modelMapper.map(dto.getCertificateDto(), Certificate.class));
            entity.setExpirationDate(dto.getExpirationDate());
            return entity;
        }
        if (Constants.UPDATE.equals(action)) {
            entity.setEmployee(employeeRepository.getById(dto.getEmployeeDTO().getId()));
            entity.setProvince(provinceRepository.getById(dto.getProvinceDto().getId()));
            entity.setCertificate(certificateRepository.getById(dto.getCertificateDto().getId()));
            entity.setExpirationDate(dto.getExpirationDate());
            return entity;
        }
        return entity;
    }
}
