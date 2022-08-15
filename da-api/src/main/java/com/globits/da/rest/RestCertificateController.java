package com.globits.da.rest;

import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.service.CertificateService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("api/certificates")
public class RestCertificateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestCertificateController.class);
    @Autowired
    CertificateService certificateService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseRequest<List<CertificateDto>> getAll() {
        List<CertificateDto> listCertificate = certificateService.getAll();
        if (listCertificate.isEmpty()) {
            return new ResponseRequest<>(
                    ErrorMessage.LIST_IS_EMPTY.getCode(),
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    listCertificate);
        }
        return new ResponseRequest<>(
                ErrorMessage.SUCCESS.getCode(),
                ErrorMessage.SUCCESS.getMessage(),
                listCertificate);
    }

    @PostMapping("/insert")
    public ResponseRequest<CertificateDto> insert(@RequestBody CertificateDto certificateDto) {
        String resultErrorMessage = certificateService.insert(certificateDto).getMessageError();
        int resultErrorCode = certificateService.insert(certificateDto).getStatusCode();
        if (resultErrorMessage.equals(ErrorMessage.SUCCESS.getMessage())) {
            return new ResponseRequest<CertificateDto>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    certificateDto);
        }
        return new ResponseRequest<CertificateDto>(
                resultErrorCode,
                resultErrorMessage,
                certificateDto);
    }

    @PutMapping("/update/{id}")
    public ResponseRequest<CertificateDto> update(@PathVariable(name = "id") UUID id, @RequestBody CertificateDto certificateDto) {
        String resultErrorMessage = certificateService.update(id, certificateDto).getMessageError();
        int resultErrorCode = certificateService.update(id, certificateDto).getStatusCode();
        if (resultErrorMessage.equals(ErrorMessage.SUCCESS.getMessage())) {
            return new ResponseRequest<CertificateDto>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    certificateDto);
        }
        return new ResponseRequest<CertificateDto>(
                resultErrorCode,
                resultErrorMessage,
                certificateDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseRequest<CertificateDto> delete(@PathVariable("id") UUID id) {
        Certificate certificate = certificateService.findById(id);
        if (certificateService.deleteById(id)) {
            LOGGER.info("Delete Success");
            return new ResponseRequest<CertificateDto>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    modelMapper.map(certificate, CertificateDto.class));
        } else {
            LOGGER.info("Delete Not Success");
            return new ResponseRequest<>(ErrorMessage.ID_NOT_EXIST.getCode(),
                    ErrorMessage.ID_NOT_EXIST.getMessage(),
                    null);
        }
    }
}

