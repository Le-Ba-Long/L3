package com.globits.da.rest;

import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.service.CertificateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.globits.da.common.ErrorMessage.*;

@RestController
@RequestMapping("api/certificates")
public class RestCertificateController {
    @Autowired
    private CertificateService certificateService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseData<List<CertificateDto>> getAll() {
        List<CertificateDto> listCertificate = certificateService.getAll();
        if (listCertificate.isEmpty()) return new ResponseData<>(LIST_IS_EMPTY, listCertificate);
        return new ResponseData<>(listCertificate);
    }

    @PostMapping()
    public ResponseData<CertificateDto> insert(@RequestBody CertificateDto certificateDto) {
        ResponseData<CertificateDto> errorMessage = certificateService.insert(certificateDto);
        if (errorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errorMessage.getData());
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), null);
    }

    @PutMapping("/{id}")
    public ResponseData<CertificateDto> update(@PathVariable(name = "id") UUID id, @RequestBody CertificateDto certificateDto) {
        ResponseData<CertificateDto> errorMessage = certificateService.update(id, certificateDto);
        if (errorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errorMessage.getData());
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseData<CertificateDto> delete(@PathVariable("id") UUID id) {
        Certificate certificate = certificateService.findById(id);
        if (certificateService.deleteById(id))
            return new ResponseData<>(modelMapper.map(certificate, CertificateDto.class));
        return new ResponseData<>(ID_NOT_EXIST, null);
    }
}

