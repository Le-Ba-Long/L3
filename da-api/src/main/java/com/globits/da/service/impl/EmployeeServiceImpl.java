package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Constants;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.ContainQueryData;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.dto.search.EmployeeSearchDTO;
import com.globits.da.file.ExcelGenerator;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.globits.da.common.ErrorMessage.*;
import static com.globits.da.validate.ValidateEmployee.validateEmployee;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, UUID> implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    @Lazy
    private ExcelGenerator excelGenerator;

    @Override
    public List<EmployeeDto> getAll() {
        return employeeRepository.findAll().stream().map(EmployeeDto::toDto).collect(Collectors.toList());
    }

    @Override
    public ResponseData<EmployeeDto> insert(EmployeeDto employeeDTO) {
        if (ObjectUtils.isEmpty(employeeDTO.getProvinceDto())
                || ObjectUtils.isEmpty(employeeDTO.getDistrictDto())
                || ObjectUtils.isEmpty(employeeDTO.getCommuneDto()))
            return new ResponseData<>(OBJECT_CANNOT_EMPTY, null);
        ErrorMessage resultError = validateEmployee(employeeDTO);
        if (!SUCCESS.equals(resultError)) return new ResponseData<>(resultError, null);
        Employee entity = new Employee();
        setEntity(entity, employeeDTO, Constants.INSERT);
        return new ResponseData<>(modelMapper.map(employeeRepository.save(entity), EmployeeDto.class));
    }

    @Override
    public ResponseData<EmployeeDto> update(UUID id, EmployeeDto employeeDTO) {
        if (!employeeRepository.existsEmployeeById(id)) return new ResponseData<>(ID_NOT_EXIST, null);
        if (ObjectUtils.isEmpty(employeeDTO.getDistrictDto())
                || ObjectUtils.isEmpty(employeeDTO.getProvinceDto())
                || ObjectUtils.isEmpty(employeeDTO.getCommuneDto()))
            return new ResponseData<>(OBJECT_CANNOT_EMPTY, null);
        ErrorMessage resultError = validateEmployee(employeeDTO);
        if (!SUCCESS.equals(resultError)) return new ResponseData<>(resultError, null);
        Employee entity = employeeRepository.getById(id);
        setEntity(entity, employeeDTO, Constants.UPDATE);
        return new ResponseData<>(modelMapper.map(employeeRepository.save(entity), EmployeeDto.class));
    }

    @Override
    public boolean deleteById(UUID uuid) {
        if (employeeRepository.existsEmployeeById(uuid)) {
            employeeRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

    @Override
    public List<EmployeeDto> findAllByEmployeeHaveMoreThanTwoCertificate() {
        return employeeRepository.findAllByEmployeeHaveMoreThanTwoCertificate().stream().map(employee -> modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeSearchDTO> countEmployeeGroupByProvince(UUID id) {
        List<Object[]> list = employeeRepository.countEmployeeGroupByProvince(id);
        return list.stream().map(obj -> {
            EmployeeSearchDTO employeeSearchDto = new EmployeeSearchDTO();
            employeeSearchDto.setCityId(UUID.fromString(obj[0].toString()));
            employeeSearchDto.setTotal(Integer.parseInt(obj[1].toString()));
            return employeeSearchDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ContainQueryData> calculatePercentageOfEmployeesWithDiplomas() {
        List<Object[]> dataList = employeeRepository.calculatePercentageOfEmployeesWithDiplomas();
        return dataList.stream().map(data -> {
            ContainQueryData containQueryData = new ContainQueryData();
            containQueryData.setName(data[0].toString());
            containQueryData.setPercent(Double.parseDouble(data[1].toString()));
            return containQueryData;
        }).collect(Collectors.toList());

    }

    public void saveAll(List<EmployeeDto> employeeDTOList) {
        List<Employee> list = employeeDTOList.stream().map(employeeDto -> {
            Employee employee = new Employee();
            setEntity(employee, employeeDto, Constants.INSERT);
            return employee;
        }).collect(Collectors.toList());
        if (list.isEmpty()) logger.info("List Employee is Empty");
        employeeRepository.saveAll(list);
    }

    @Override
    public Page<Employee> getPage(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public void setEntity(Employee employee, EmployeeDto employeeDTO, String action) {

        if (Constants.INSERT.equals(action)) {
            employee.setCode(employeeDTO.getCode());
            employee.setName(employeeDTO.getName());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());
            employee.setAge(employeeDTO.getAge());
            employee.setProvince(modelMapper.map(employeeDTO.getProvinceDto(), Province.class));
            employee.setDistrict(modelMapper.map(employeeDTO.getDistrictDto(), District.class));
            employee.setCommune(modelMapper.map(employeeDTO.getCommuneDto(), Commune.class));
        }
        if (Constants.UPDATE.equals(action)) {
            employee.setCode(employeeDTO.getCode());
            employee.setName(employeeDTO.getName());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());
            employee.setAge(employeeDTO.getAge());
            employee.setProvince(provinceRepository.getById(employeeDTO.getProvinceDto().getId()));
            employee.setDistrict(districtRepository.getById(employeeDTO.getDistrictDto().getId()));
            employee.setCommune(communeRepository.getById(employeeDTO.getCommuneDto().getId()));
        }

    }

    @Override
    public List<String> importExcel(MultipartFile multipartFile) throws IOException {
        return excelGenerator.importFileExcel(multipartFile);
    }

    @Override
    public List<EmployeeDto> exportExcel() {
        return getAll();
    }
}