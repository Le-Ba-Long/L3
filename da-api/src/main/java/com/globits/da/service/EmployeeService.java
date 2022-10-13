package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Employee;
import com.globits.da.dto.ContainQueryData;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.dto.search.EmployeeSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface EmployeeService extends GenericService<Employee, UUID> {
    List<EmployeeDto> getAll();

    ResponseData<EmployeeDto> insert(EmployeeDto employeeDTO);

    ResponseData<EmployeeDto> update(UUID id, EmployeeDto employeeDTO);

    List<EmployeeDto> findAllByEmployeeHaveMoreThanTwoCertificate();

    List<EmployeeSearchDTO> countEmployeeGroupByProvince(UUID id);
    List<ContainQueryData>calculatePercentageOfEmployeesWithDiplomas();

    boolean deleteById(UUID uuid);

    void saveAll(List<EmployeeDto> employeeDTOList);


    List<String> importExcel(MultipartFile multipartFile) throws IOException;

    List<EmployeeDto> exportExcel() throws IOException;

    Page<Employee> getPage(Pageable pageable);

}
