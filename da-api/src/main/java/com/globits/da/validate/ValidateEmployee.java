package com.globits.da.validate;

import com.globits.da.Constants;
import com.globits.da.common.GetCellValueIndex;
import com.globits.da.common.ErrorMessage;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.regex.Matcher;

import static com.globits.da.common.ErrorMessage.*;
import static com.globits.da.validate.ValidateEmployeeCertificate.getErrorMessage;

@Component
public class ValidateEmployee extends ValidateBase {
    public static EmployeeRepository employeeRepository;
    private static ProvinceRepository provinceRepository;
    public static DistrictRepository districtRepository;
    public static CommuneRepository communeRepository;


    @Autowired
    public ValidateEmployee(EmployeeRepository employeeRepository, ProvinceRepository provinceRepository, DistrictRepository districtRepository, CommuneRepository communeRepository) {
        ValidateEmployee.employeeRepository = employeeRepository;
        ValidateEmployee.provinceRepository = provinceRepository;
        ValidateEmployee.districtRepository = districtRepository;
        ValidateEmployee.communeRepository = communeRepository;
    }

    public static ErrorMessage checkValidCode(String code) {
        if (StringUtils.isEmpty(code)) return CODE_IS_NULL;
        ErrorMessage err = isValidCode(code);
        if (!SUCCESS.equals(err)) return err;
        if ((code.length() < Constants.MIN_LENGTH_CODE || code.length() > Constants.MAX_LENGTH_CODE))
            return LENGTH_CODE_INVALID;
        if (employeeRepository.existsEmployeeByCode(code)) return CODE_IS_EXIST;
        return SUCCESS;
    }

    public static ErrorMessage isValidEmail(String email) {
        if (StringUtils.isEmpty(email)) return EMAIL_IS_NULL;
        if (employeeRepository.existsEmployeeByEmail(email)) return EMAIL_IS_EXIST;
        Matcher matcher = Constants.VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (matcher.find()) return SUCCESS;
        return EMAIL_INVALID;
    }

    public static ErrorMessage isValidPhone(String phone) {
        if (StringUtils.isEmpty(phone)) return PHONE_IS_NULL;
        if ((phone.length() == Constants.MAX_LENGTH_PHONE)) return LENGTH_PHONE_INVALID;
        if (Constants.REGEX_VALID_PHONE.matches(phone)) return CHARACTER_PHONE_INVALID;
        return SUCCESS;
    }

    public static ErrorMessage isValidAge(Integer age) {
        if (ObjectUtils.isEmpty(age)) return AGE_IS_NULL;
        if (!StringUtils.isEmpty(age) && age < 0) return AGE_INVALID;
        return SUCCESS;
    }

    public static ErrorMessage validateCommune(EmployeeDto employeeDTO) {
        if (ObjectUtils.isEmpty(employeeDTO.getDistrictDto())) return DISTRICT_IS_NULL;
        if ((ObjectUtils.isEmpty(employeeDTO.getCommuneDto()))) return COMMUNE_IS_NULL;
        UUID communeID = employeeDTO.getCommuneDto().getId();
        UUID districtID = employeeDTO.getDistrictDto().getId();
        if (StringUtils.isEmpty(districtID)) return ID_IS_NULL;
        if (StringUtils.isEmpty(communeID)) return ID_IS_NULL;
        if (!communeRepository.existsCommuneById(communeID)) return COMMUNE_NOT_FOUND;
        if (communeRepository.findByIdAndDistrictId(communeID, districtID) == null) return COMMUNE_NOT_IN_DISTRICT;
        return SUCCESS;
    }

    public static ErrorMessage validateDistrict(EmployeeDto employeeDTO) {
        if (ObjectUtils.isEmpty(employeeDTO.getProvinceDto())) return PROVINCE_IS_NULL;
        if (ObjectUtils.isEmpty(employeeDTO.getDistrictDto())) return DISTRICT_IS_NULL;
        UUID provinceID = employeeDTO.getProvinceDto().getId();
        UUID districtID = employeeDTO.getDistrictDto().getId();
        if (StringUtils.isEmpty(provinceID)) return ID_IS_NULL;
        if (StringUtils.isEmpty(districtID)) return ID_IS_NULL;
        if (!districtRepository.existsDistrictById(districtID)) return DISTRICT_NOT_FOUND;
        if (districtRepository.findByIdAndProvinceId(districtID, provinceID) == null) return DISTRICT_NOT_IN_PROVINCE;
        return SUCCESS;
    }

    public static ErrorMessage validateProvince(EmployeeDto employeeDTO) {
        if (ObjectUtils.isEmpty(employeeDTO.getProvinceDto())) return PROVINCE_IS_NULL;
        UUID provinceID = employeeDTO.getProvinceDto().getId();
        if (StringUtils.isEmpty(provinceID)) return ID_IS_NULL;
        if (!provinceRepository.existsProvinceById(provinceID)) return PROVINCE_ID_NOT_EXIST;
        return SUCCESS;
    }

    public static ErrorMessage validateEmployee(EmployeeDto employeeDTO) {
        ErrorMessage isValidCode = checkValidCode(employeeDTO.getCode());
        ErrorMessage isValidName = isValidName(employeeDTO.getName());
        ErrorMessage isValidEmail = isValidEmail(employeeDTO.getEmail());
        ErrorMessage isValidAge = isValidAge(employeeDTO.getAge());
        ErrorMessage isValidPhone = isValidPhone(employeeDTO.getPhone());
        ErrorMessage isValidProvince = validateProvince(employeeDTO);
        ErrorMessage isValidDistrict = validateDistrict(employeeDTO);
        ErrorMessage isValidCommune = validateCommune(employeeDTO);
        if (!SUCCESS.equals(isValidCommune)) return isValidCommune;
        if (!SUCCESS.equals(isValidDistrict)) return isValidDistrict;
        if (!SUCCESS.equals(isValidName)) return isValidName;
        if (!SUCCESS.equals(isValidProvince)) return isValidProvince;
        return getErrorMessage(isValidCode, isValidAge, isValidPhone, isValidEmail);
    }

    public static ErrorMessage validateEmployee(EmployeeDto employeeDTO, int index) {
        GetCellValueIndex getCellValueIndex = GetCellValueIndex.values()[index];
        switch (getCellValueIndex) {
            case Code:
                ErrorMessage isValidCode = checkValidCode(employeeDTO.getCode());
                if (!SUCCESS.equals(isValidCode)) return isValidCode;
                break;
            case Name:
                ErrorMessage isValidName = isValidName(employeeDTO.getName());
                if (!SUCCESS.equals(isValidName)) return isValidName;
                break;
            case Email:
                ErrorMessage isValidEmail = isValidEmail(employeeDTO.getEmail());
                if (!SUCCESS.equals(isValidEmail)) return isValidEmail;
                break;
            case Phone:
                ErrorMessage isValidPhone = isValidPhone(employeeDTO.getPhone());
                if (!SUCCESS.equals(isValidPhone)) return isValidPhone;
                break;
            case Age:
                ErrorMessage isValidAge = isValidAge(employeeDTO.getAge());
                if (!SUCCESS.equals(isValidAge)) return isValidAge;
                break;
            case Province:
                ErrorMessage isValidProvince = validateProvince(employeeDTO);
                if (!SUCCESS.equals(isValidProvince)) return isValidProvince;
                break;
            case District:
                ErrorMessage isValidDistrict = validateDistrict(employeeDTO);
                if (!SUCCESS.equals(isValidDistrict)) return isValidDistrict;
                break;
            case Commune:
                ErrorMessage isValidCommune = validateCommune(employeeDTO);
                if (!SUCCESS.equals(isValidCommune)) return isValidCommune;
                break;
        }
        return SUCCESS;
    }
}
