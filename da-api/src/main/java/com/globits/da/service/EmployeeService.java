package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDTO;
import com.globits.da.dto.ResponseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface EmployeeService extends GenericService<Employee, UUID> {
    public List<EmployeeDTO> getAll();

    public ResponseRequest<EmployeeDTO> insert(EmployeeDTO employeeDTO);

    public ResponseRequest<EmployeeDTO> update(UUID id,EmployeeDTO employeeDTO);

    public Boolean deleteById(UUID uuid);

    public void saveAll(List<EmployeeDTO> employeeDTOList);

    public List<EmployeeDTO> findByAllKeyword(String keyword);

    public Optional<EmployeeDTO> findEmployeeById(UUID id);

    Page<Employee> getPage(Pageable pageable);
    public List<Employee> getAllEmployee();
}
