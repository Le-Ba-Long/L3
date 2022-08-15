package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Certificate;
import com.globits.da.domain.Province;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.validate.ValidateCertificate;
import com.globits.da.validate.ValidateProvince;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl extends GenericServiceImpl<Certificate, UUID> implements CertificateService {
    @Autowired
    CertificateRepository certificateRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CertificateDto> getAll() {
        return certificateRepository.findAll()
                .stream()
                .map(certificate -> {
                    return modelMapper.map(certificate, CertificateDto.class);
                }).collect(Collectors.toList());
    }

    @Override
    public CertificateDto save(CertificateDto certificateDto) {
        return modelMapper.map(certificateRepository.save(modelMapper.map(certificateDto, Certificate.class)), CertificateDto.class);
    }

    @Override
    public Boolean deleteById(UUID id) {
        if (certificateRepository.existsCertificateById(id)) {
            certificateRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ResponseRequest<CertificateDto> update(UUID id, CertificateDto certificateDto) {
        if (certificateRepository.existsCertificateById(id)) {
            String resultErrorMessage = checkValidCodeAndName(certificateDto).getMessage();
            String updateSuccess = ErrorMessage.SUCCESS.getMessage();
            if (updateSuccess.equals(resultErrorMessage)) {
                return new ResponseRequest<>(
                        ErrorMessage.SUCCESS.getCode(),
                        ErrorMessage.SUCCESS.getMessage(),
                        modelMapper.map(certificateRepository.findById(id)
                                .map(certificate -> {
                                    certificate.setCode(certificateDto.getCode());
                                    certificate.setName(certificateDto.getName());
                                    certificate.setGrantedBy(certificateDto.getGrantedBy());
                                    certificate.setDateStartEffect(certificateDto.getDateStartEffect());
                                    certificate.setDateEndEffect(certificateDto.getDateEndEffect());
                                    certificate.setProvince(modelMapper.map(certificateDto.getProvince(), Province.class));
                                    return certificate;
                                }), CertificateDto.class));
            } else {
                return new ResponseRequest<>(
                        ValidateProvince.resultStatusCode(checkValidCodeAndName(certificateDto)),
                        resultErrorMessage,
                        certificateDto);
            }
        }
        return new ResponseRequest<>(
                ErrorMessage.ID_NOT_EXIST.getCode(),
                ErrorMessage.ID_NOT_EXIST.getMessage(),
                certificateDto);
    }

    @Override
    public ResponseRequest<CertificateDto> insert(CertificateDto certificateDto) {
        String resultErrorMessage = checkValidCertificate(certificateDto).getMessage();
        String insertSuccess = ErrorMessage.SUCCESS.getMessage();
        if (insertSuccess.equals(resultErrorMessage)) {
            // certificateDto.setId(UUID.randomUUID());
            Certificate entity = new Certificate();
            entity.setCode(certificateDto.getCode());
            entity.setName(certificateDto.getName());
            entity.setGrantedBy(certificateDto.getGrantedBy());
            entity.setDateStartEffect(certificateDto.getDateStartEffect());
            entity.setDateEndEffect(certificateDto.getDateEndEffect());
            entity.setProvince(modelMapper.map(certificateDto.getProvince(), Province.class));

            return new ResponseRequest<>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    modelMapper.map(certificateRepository.save(entity), CertificateDto.class));
        } else {
            return new ResponseRequest<>(
                    ValidateCertificate.resultStatusCode(checkValidCertificate(certificateDto)),
                    resultErrorMessage,
                    certificateDto);
        }
    }


    public ErrorMessage checkValidCertificate(CertificateDto certificateDto) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateCertificate.checkCodeIsNull(certificateDto.getCode()).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (certificateRepository.existsCertificateByCode(certificateDto.getCode())) {
            return ErrorMessage.CODE_IS_EXIST;
        } else if (!ValidateCertificate.checkNameIsNull(certificateDto.getName()).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        } else if (certificateRepository.existsCertificateByName(certificateDto.getName())) {
            return ErrorMessage.NAME_IS_EXIST;
        }
        return ErrorMessage.SUCCESS;
    }

    public ErrorMessage checkValidCodeAndName(CertificateDto certificateDto) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateCertificate.checkCodeIsNull(certificateDto.getCode()).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (!ValidateProvince.checkNameIsNull(certificateDto.getName()).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        }
        return ErrorMessage.SUCCESS;
    }


    @Override
    public Page<CertificateDto> getPage(int pageIndex, int pageSize) {
        return null;
    }

    @Override
    public Page<CertificateDto> search(CertificateDto certificateDto) {
        return null;
    }

    @Override
    public List<CertificateDto> findAllDistrictByCertificateId(UUID certificateId) {
        return null;
    }

}