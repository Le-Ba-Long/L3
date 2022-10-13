package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.EmployeeCertificate;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.dto.ResponseData;

import java.util.List;
import java.util.UUID;

public interface EmployeeCertificateService extends GenericService<EmployeeCertificate, UUID> {
    List<EmployeeCertificateDto> getAll();

    boolean deleteById(UUID uuid);

    ResponseData<EmployeeCertificateDto> update(UUID id, EmployeeCertificateDto employeeCertificateDto);

    ResponseData<EmployeeCertificateDto> insert(EmployeeCertificateDto employeeCertificateDto);

}
