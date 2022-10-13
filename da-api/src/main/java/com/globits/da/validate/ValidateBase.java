package com.globits.da.validate;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.Constants;
import com.globits.da.common.ErrorMessage;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import static com.globits.da.common.ErrorMessage.*;

@Component
public class ValidateBase extends BaseObjectDto {
    private static ProvinceRepository provinceRepository;
    private static DistrictRepository districtRepository;
    private static CommuneRepository communeRepository;
    private static CertificateRepository certificateRepository;

    @Autowired
    public ValidateBase(ProvinceRepository provinceRepository,
                        DistrictRepository districtRepository,
                        CommuneRepository communeRepository,
                        CertificateRepository certificateRepository) {
        ValidateBase.provinceRepository = provinceRepository;
        ValidateBase.districtRepository = districtRepository;
        ValidateBase.communeRepository = communeRepository;
        ValidateBase.certificateRepository = certificateRepository;
    }

    public ValidateBase() {
    }


    public static ErrorMessage isValidCode(String code) {
        if (StringUtils.isEmpty(code)) return CODE_IS_NULL;
        if (code.contains(Constants.SPACE)) return CHARACTER_CODE_INVALID;
        return SUCCESS;
    }

    public static ErrorMessage isValidName(String name) {
        if (StringUtils.isEmpty(name)) return NAME_IS_NULL;
        return SUCCESS;
    }

    public static ErrorMessage checkValidDto(Object obj, String action) {
        if (obj instanceof ProvinceDto) return checkValidProvinceDto(obj, action);
        if (obj instanceof DistrictDto) return checkValidDistrictDto(obj, action);
        if (obj instanceof CommuneDto) return checkValidCommuneDto(obj, action);
        if (obj instanceof CertificateDto) return checkValidCertificate(obj, action);
        return SUCCESS;
    }

    public static ErrorMessage checkValidProvinceDto(Object obj, String action) {
        if (obj instanceof ProvinceDto) {
            ProvinceDto provinceDto = ((ProvinceDto) obj);
            if (Constants.INSERT.equals(action)) {
                if (ObjectUtils.isEmpty(obj)) return PROVINCE_IS_NULL;
                ErrorMessage errorMessage = isValidCode(provinceDto.getCode());
                if (!SUCCESS.equals(errorMessage)) return errorMessage;
                if (Boolean.TRUE.equals(provinceRepository.existsProvinceByCode(provinceDto.getCode()))) return CODE_IS_EXIST;
                if (Boolean.TRUE.equals(provinceRepository.existsProvinceByName(provinceDto.getName()))) return NAME_IS_EXIST;
            }
            if (Constants.UPDATE.equals(action)) {
                if (ObjectUtils.isEmpty(obj)) return PROVINCE_IS_NULL;
                if (Boolean.TRUE.equals(provinceRepository.existsProvinceByCode(provinceDto.getCode()))) return CODE_IS_EXIST;
                if (Boolean.TRUE.equals(provinceRepository.existsProvinceById(provinceDto.getId()))) return ID_NOT_EXIST;
                ErrorMessage errorMessage = isValidCode(provinceDto.getCode());
                if (!SUCCESS.equals(errorMessage)) return errorMessage;
            }
        }
        return SUCCESS;
    }

    public static ErrorMessage checkValidDistrictDto(Object obj, String action) {
        if (obj instanceof DistrictDto) {
            DistrictDto districtDto = ((DistrictDto) obj);
            if (Constants.INSERT.equals(action)) {
                if (ObjectUtils.isEmpty(obj)) return DISTRICT_IS_NULL;
                ErrorMessage errorMessage = isValidCode(districtDto.getCode());
                if (!SUCCESS.equals(errorMessage)) return errorMessage;
                if (Boolean.TRUE.equals(districtRepository.existsDistrictByCode(districtDto.getCode()))) return CODE_IS_EXIST;
                if (Boolean.TRUE.equals(districtRepository.existsDistrictByName(districtDto.getName()))) return NAME_IS_EXIST;
                if (districtDto.getProvinceDto() != null
                        && districtDto.getProvinceDto().getId() != null
                        && districtDto.getId() != null
                        && districtRepository.findByIdAndProvinceId
                        (districtDto.getId(), districtDto.getProvinceDto().getId()) == null)
                        return DISTRICT_NOT_IN_PROVINCE;
                }
            if (Constants.UPDATE.equals(action)) {
                if (ObjectUtils.isEmpty(obj)) return DISTRICT_IS_NULL;
                if (Boolean.TRUE.equals(districtRepository.existsDistrictByCode(districtDto.getCode()))) return CODE_IS_EXIST;
                ErrorMessage errorMessage = isValidCode(districtDto.getCode());
                if (!SUCCESS.equals(errorMessage)) return errorMessage;
                if (districtDto.getProvinceDto() != null
                        && districtDto.getProvinceDto().getId() != null
                        && districtDto.getId() != null
                        && districtRepository.findByIdAndProvinceId
                        (districtDto.getId(), districtDto.getProvinceDto().getId()) == null)
                        return DISTRICT_NOT_IN_PROVINCE;
            }
        }
        return SUCCESS;
    }

    public static ErrorMessage checkValidCommuneDto(Object obj, String action) {
        if (obj instanceof CommuneDto) {
            CommuneDto communeDto = ((CommuneDto) obj);
            if (Constants.UPDATE.equals(action)) {
                if (ObjectUtils.isEmpty(obj)) return COMMUNE_IS_NULL;
                if (Boolean.FALSE.equals(communeRepository.existsCommuneById(communeDto.getId()))) return ID_NOT_EXIST;
                if (Boolean.TRUE.equals(communeRepository.existsCommuneByCode(communeDto.getCode()))) return CODE_IS_EXIST;
                ErrorMessage errorMessage = isValidCode(communeDto.getCode());
                if (!SUCCESS.equals(errorMessage)) return errorMessage;
                if (communeDto.getDistrictDto() != null
                        && communeDto.getDistrictDto().getId() != null
                        && communeDto.getId() != null
                        && communeRepository.findByIdAndDistrictId
                        (communeDto.getId(), communeDto.getDistrictDto().getId()) == null)
                        return COMMUNE_NOT_IN_DISTRICT;
            }
            if (action.equals(Constants.INSERT)) {
                if (ObjectUtils.isEmpty(obj)) return COMMUNE_IS_NULL;
                if (communeDto.getDistrictDto() != null
                        && communeDto.getDistrictDto().getId() != null
                        && Boolean.FALSE.equals
                        (districtRepository.existsDistrictById(communeDto.getDistrictDto().getId())))
                        return DISTRICT_NOT_FOUND;
                }
                ErrorMessage errorMessage = isValidCode(communeDto.getCode());
                if (!SUCCESS.equals(errorMessage)) return errorMessage;
                if (Boolean.TRUE.equals(communeRepository.existsCommuneByCode(communeDto.getCode()))) return CODE_IS_EXIST;
                if (Boolean.TRUE.equals(communeRepository.existsCommuneByName(communeDto.getName()))) return NAME_IS_EXIST;
                if (communeDto.getDistrictDto() != null
                        && communeDto.getDistrictDto().getId() != null
                        && communeDto.getId() != null
                        && communeRepository.findByIdAndDistrictId(communeDto.getId()
                        , communeDto.getDistrictDto().getId()) == null)
                        return COMMUNE_NOT_IN_DISTRICT;
        }
        return SUCCESS;
    }

    public static ErrorMessage checkValidCertificate(Object obj, String action) {
        if (obj instanceof CertificateDto) {
            CertificateDto certificateDto = ((CertificateDto) obj);
            if (action.equals(Constants.INSERT)) {
                if (ObjectUtils.isEmpty(obj)) return CERTIFICATE_IS_NULL;
                ErrorMessage errorMessage = isValidCode(certificateDto.getCode());
                if (!SUCCESS.equals(errorMessage)) return errorMessage;
                if (certificateRepository.existsCertificateByCode(certificateDto.getCode())) return CODE_IS_EXIST;
                if (certificateRepository.existsCertificateByName(certificateDto.getName())) return NAME_IS_EXIST;
            }
            if (Constants.UPDATE.equals(action)) {
                if (ObjectUtils.isEmpty(obj)) return CERTIFICATE_IS_NULL;
                if (Boolean.FALSE.equals(certificateRepository.existsCertificateById(certificateDto.getId()))) return ID_NOT_EXIST;
                if (certificateRepository.existsCertificateByCode(certificateDto.getCode())) return CODE_IS_EXIST;
                ErrorMessage errorMessage = isValidCode(certificateDto.getCode());
                if (!SUCCESS.equals(errorMessage)) return errorMessage;
            }
        }
        return SUCCESS;
    }
}
