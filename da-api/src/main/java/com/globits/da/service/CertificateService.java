package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.ResponseData;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CertificateService extends GenericService<Certificate, UUID> {
    List<CertificateDto> getAll();

    boolean deleteById(UUID uuid);

    ResponseData<CertificateDto> update(UUID id, CertificateDto certificateDto);

    ResponseData<CertificateDto> insert(CertificateDto certificateDto);

    Page<CertificateDto> getPage(int pageIndex, int pageSize);

}
