package com.globits.da.validate;

import com.globits.da.common.ErrorMessage;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeCertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

import static com.globits.da.common.ErrorMessage.*;

@Component
public class ValidateEmployeeCertificate extends ValidateBase {

    private static EmployeeCertificateRepository employeeCertificateRepository;
    private static CertificateRepository certificateRepository;
    private static EmployeeRepository employeeRepository;
    private static ProvinceRepository provinceRepository;

    @Autowired
    public ValidateEmployeeCertificate(EmployeeCertificateRepository employeeCertificateRepository, CertificateRepository certificateRepository, EmployeeRepository employeeRepository, ProvinceRepository provinceRepository) {
        ValidateEmployeeCertificate.employeeCertificateRepository = employeeCertificateRepository;
        ValidateEmployeeCertificate.certificateRepository = certificateRepository;
        ValidateEmployeeCertificate.employeeRepository = employeeRepository;
        ValidateEmployeeCertificate.provinceRepository = provinceRepository;

    }


    public static ErrorMessage isValidEmployeeCertificate(EmployeeCertificateDto employeeCertificateDto, UUID id) {
        ErrorMessage errorEmployee = checkExistsEmployee(employeeCertificateDto);
        ErrorMessage errorCertificate = checkExistsCertificate(employeeCertificateDto);
        ErrorMessage errorProvince = checkExistsProvince(employeeCertificateDto);
        ErrorMessage errorEmployeeCertificate = isValidCountEmployeeCertificate(employeeCertificateDto, id);
        return getErrorMessage(errorEmployee, errorCertificate, errorProvince, errorEmployeeCertificate);
    }

    static ErrorMessage getErrorMessage(ErrorMessage errorEmployee, ErrorMessage errorCertificate, ErrorMessage errorProvince, ErrorMessage errorEmployeeCertificate) {
        if (!SUCCESS.equals(errorEmployee)) return errorEmployee;
        if (!SUCCESS.equals(errorCertificate)) return errorCertificate;
        if (!SUCCESS.equals(errorProvince)) return errorProvince;
        if (!SUCCESS.equals(errorEmployeeCertificate)) return errorEmployeeCertificate;
        return SUCCESS;
    }

    public static ErrorMessage checkExistsCertificate(EmployeeCertificateDto employeeCertificateDto) {
        if (ObjectUtils.isEmpty(employeeCertificateDto.getCertificateDto().getId())) return ID_IS_NULL;
        if (!certificateRepository.existsCertificateById(employeeCertificateDto.getCertificateDto().getId()))
            return ID_NOT_EXIST;
        return SUCCESS;
    }

    public static ErrorMessage checkExistsProvince(EmployeeCertificateDto employeeCertificateDto) {
        if (ObjectUtils.isEmpty(employeeCertificateDto.getProvinceDto().getId())) return ID_IS_NULL;
        if (!provinceRepository.existsProvinceById(employeeCertificateDto.getProvinceDto().getId()))
            return ID_NOT_EXIST;
        return SUCCESS;
    }

    public static ErrorMessage checkExistsEmployee(EmployeeCertificateDto employeeCertificateDto) {
        UUID employeeId = employeeCertificateDto.getEmployeeDTO().getId();
        if (ObjectUtils.isEmpty(employeeId)) return ID_IS_NULL;
        if (!employeeRepository.existsEmployeeById(employeeId)) return ID_NOT_EXIST;
        return SUCCESS;
    }

    public static ErrorMessage isValidCountEmployeeCertificate(EmployeeCertificateDto employeeCertificateDto, UUID id) {
        String employeeId = String.valueOf(employeeCertificateDto.getEmployeeDTO().getId());
        String certificateId = String.valueOf(employeeCertificateDto.getCertificateDto().getId());
        String provinceId = String.valueOf(employeeCertificateDto.getProvinceDto().getId());
        String employeeCertificateID = String.valueOf(id);
        if (StringUtils.isEmpty(employeeCertificateID)) {
            if (employeeCertificateRepository.countExpireCertificateOfTheProvince(employeeId, certificateId, provinceId) >= 1)
                return CERTIFICATE_HAS_EFFECT;
            if (employeeCertificateRepository.countMoreThan3ValidCertificatesHaveTheSameType(employeeId, certificateId) >= 3)
                return NUMBER_CERTIFICATE_EXCEED;
        } else {
            if (employeeCertificateRepository.countMoreThan3ValidCertificatesHaveTheSameTypeExcept(employeeCertificateID, employeeId, certificateId) >= 3)
                return NUMBER_CERTIFICATE_EXCEED;
            if (employeeCertificateRepository.countExpireCertificateOfTheProvinceExcept(employeeCertificateID, employeeId, certificateId, provinceId) >= 1)
                return CERTIFICATE_HAS_EFFECT;
        }
        return SUCCESS;
    }
}
