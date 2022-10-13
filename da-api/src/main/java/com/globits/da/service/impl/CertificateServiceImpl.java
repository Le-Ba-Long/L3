package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Constants;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.utils.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.globits.da.common.ErrorMessage.SUCCESS;
import static com.globits.da.validate.ValidateBase.checkValidDto;

@Service
public class CertificateServiceImpl extends GenericServiceImpl<Certificate, UUID> implements CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CertificateDto> getAll() {
        return certificateRepository.findAll()
                .stream()
                .map(certificate -> modelMapper.map(certificate, CertificateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(UUID id) {
        if (certificateRepository.existsCertificateById(id)) {
            certificateRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ResponseData<CertificateDto> update(UUID id, CertificateDto certificateDto) {
        ErrorMessage errorMessage = checkValidDto(certificateDto, Constants.UPDATE);
        if (!SUCCESS.equals(errorMessage))
            return new ResponseData<>(errorMessage.getCode(), errorMessage.getMessage(), null);
        Certificate certificate = certificateRepository.getById(id);
        certificate.setName(certificateDto.getName());
        certificate.setCode(certificateDto.getCode());
        certificate.setGrantedBy(certificateDto.getGrantedBy());
        certificate.setDateStartEffect(certificateDto.getDateStartEffect());
        certificate.setDateEndEffect(certificateDto.getDateEndEffect());
        return new ResponseData<>(modelMapper.map(certificateRepository.save(certificate), CertificateDto.class));
    }

    @Override
    public ResponseData<CertificateDto> insert(CertificateDto certificateDto) {
        ErrorMessage errorMessage = checkValidDto(certificateDto, Constants.INSERT);
        if (!SUCCESS.equals(errorMessage)) return new ResponseData<>(errorMessage, null);
        Certificate entity = new Certificate();
        entity.setCode(certificateDto.getCode());
        entity.setName(certificateDto.getName());
        entity.setGrantedBy(certificateDto.getGrantedBy());
        entity.setDateStartEffect(certificateDto.getDateStartEffect());
        entity.setDateEndEffect(certificateDto.getDateEndEffect());
        return new ResponseData<>(modelMapper.map(certificateRepository.save(entity), CertificateDto.class));
    }

    @Override
    public Page<CertificateDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(PageUtil.validatePageIndex(pageIndex), PageUtil.validatePageSize(pageSize));
        return certificateRepository.findAll(pageable).map(certificate -> modelMapper.map(certificate, CertificateDto.class));
    }
}