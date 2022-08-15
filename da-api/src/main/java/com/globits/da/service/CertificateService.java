package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ResponseRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public interface CertificateService extends GenericService<Certificate, UUID> {
    public List<CertificateDto> getAll();

    public CertificateDto save(CertificateDto certificateDto);

    public Boolean deleteById(UUID uuid);

    public ResponseRequest<CertificateDto> update(UUID id, CertificateDto certificateDto);

    public ResponseRequest<CertificateDto> insert(CertificateDto certificateDto);

    public Page<CertificateDto> getPage(int pageIndex, int pageSize);

    public Page<CertificateDto> search(CertificateDto certificateDto);

    public List<CertificateDto> findAllDistrictByCertificateId(UUID certificateId);
}
