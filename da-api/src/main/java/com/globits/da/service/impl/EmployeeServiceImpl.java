package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Employee;
import com.globits.da.domain.Province;
import com.globits.da.dto.*;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.validate.ValidateBase;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, UUID> implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    CommuneRepository communeRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<EmployeeDTO> getAll() {
        List<EmployeeDTO> employeeDTOList =
                employeeRepository.findAll()
                        .stream()
                        .map(employee -> {
                            return modelMapper.map(employee, EmployeeDTO.class);
                        }).collect(Collectors.toList());
        List<EmployeeDTO> list = new ArrayList<>();
        employeeDTOList
                .stream()
                .map(employeeDTO -> {
                    EmployeeDTO employeeDTO1 = new EmployeeDTO();
                    employeeDTO1.setCode(employeeDTO.getCode());
                    employeeDTO1.setName(employeeDTO.getName());
                    employeeDTO1.setEmail(employeeDTO.getEmail());
                    employeeDTO1.setPhone(employeeDTO.getPhone());
                    employeeDTO1.setAge(employeeDTO.getAge());
                    employeeDTO1.setProvinceDto(modelMapper.map(employeeDTO.getProvinceDto(), ProvinceDto.class));
                    employeeDTO1.setDistrictDto(modelMapper.map(employeeDTO.getDistrictDto(), DistrictDto.class));
                    employeeDTO1.setCommuneDto(modelMapper.map(employeeDTO.getCommuneDto(), CommuneDto.class));
                     list.add(employeeDTO1);
                    return employeeDTO1;
                }).collect(Collectors.toList());
        return list;
    }

    @Override
    public ResponseRequest<EmployeeDTO> insert(EmployeeDTO employeeDTO) {

        if (employeeDTO.getProvinceDto() != null
                && employeeDTO.getDistrictDto() != null
                && employeeDTO.getCommuneDto() != null) {
            ErrorMessage success = ErrorMessage.SUCCESS;
            ErrorMessage resultError = validateEmployee(employeeDTO);
            if (resultError.equals(success)) {
                Employee entity = new Employee();
                entity.setCode(employeeDTO.getCode());
                entity.setName(employeeDTO.getName());
                entity.setEmail(employeeDTO.getEmail());
                entity.setPhone(employeeDTO.getPhone());
                entity.setAge(employeeDTO.getAge());
                entity.setProvince(modelMapper.map(employeeDTO.getProvinceDto(), Province.class));
                entity.setDistrict(modelMapper.map(employeeDTO.getDistrictDto(), District.class));
                entity.setCommune(modelMapper.map(employeeDTO.getCommuneDto(), Commune.class));
                return new ResponseRequest<>(
                        ValidateBase.resultStatusCode(resultError),
                        resultError.getMessage(),
                        modelMapper.map(employeeRepository.save(entity), EmployeeDTO.class));

            }
            return new ResponseRequest<>(
                    ValidateBase.resultStatusCode(resultError),
                    resultError.getMessage(), employeeDTO);
        } else {
            return new ResponseRequest<>(
                    ErrorMessage.NOT_SUCCESS.getCode(),
                    ErrorMessage.NOT_SUCCESS.getMessage(),
                    employeeDTO);
        }


    }

    @Override
    public ResponseRequest<EmployeeDTO> update(UUID id, EmployeeDTO employeeDTO) {
        if (employeeRepository.existsEmployeeById(id)) {
            if (employeeDTO.getProvinceDto() != null
                    && employeeDTO.getDistrictDto() != null
                    && employeeDTO.getCommuneDto() != null) {
                ErrorMessage success = ErrorMessage.SUCCESS;
                ErrorMessage resultError = validateEmployee(employeeDTO);
                if (resultError.equals(success)) {
                    Employee entity = employeeRepository.getOne(id);
                    entity.setCode(employeeDTO.getCode());
                    entity.setName(employeeDTO.getName());
                    entity.setEmail(employeeDTO.getEmail());
                    entity.setPhone(employeeDTO.getPhone());
                    entity.setAge(employeeDTO.getAge());
                    entity.setProvince(modelMapper.map(employeeDTO.getProvinceDto(), Province.class));
                    entity.setDistrict(modelMapper.map(employeeDTO.getDistrictDto(), District.class));
                    entity.setCommune(modelMapper.map(employeeDTO.getCommuneDto(), Commune.class));
                    return new ResponseRequest<>(
                            ValidateBase.resultStatusCode(resultError),
                            resultError.getMessage(),
                            modelMapper.map(employeeRepository.save(entity), EmployeeDTO.class));
                }
                return new ResponseRequest<>(
                        ValidateBase.resultStatusCode(resultError),
                        resultError.getMessage(),
                        employeeDTO);
            } else {
                return new ResponseRequest<>(
                        ErrorMessage.NOT_SUCCESS.getCode(),
                        ErrorMessage.NOT_SUCCESS.getMessage(),
                        employeeDTO);
            }
        } else {
            return new ResponseRequest<>(
                    ErrorMessage.ID_NOT_EXIST.getCode(),
                    ErrorMessage.ID_NOT_EXIST.getMessage(),
                    employeeDTO);
        }
    }

    //  @Override
//    public List<EmployeeDTO> findByAllKeyword(String keyword) {
//        return employeeRepository.findByAllKeyword(keyword)
//                .stream()
//                .map(employee -> {
//                    return modelMapper.map(employee, EmployeeDTO.class);
//                })
//                .collect(Collectors.toList());
//
//    }

    @Override
    public Boolean deleteById(UUID uuid) {
        if (employeeRepository.existsEmployeeById(uuid)) {
            employeeRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

    public void saveAll(List<EmployeeDTO> employeeDTOList) {
        List<Employee> list = employeeDTOList
                .stream()
                .map(employee -> {
                    return modelMapper.map(employee, Employee.class);
                }).collect(Collectors.toList());
        if (list.isEmpty()) {
            logger.info("List Employee is Empty");
        } else {
            employeeRepository.saveAll(list);
        }
    }

    @Override
    public List<EmployeeDTO> findByAllKeyword(String keyword) {
        return null;
    }

    @Override
    public Optional<EmployeeDTO> findEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class));

    }

    @Override
    public Page<Employee> getPage(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public ErrorMessage validateEmployee(EmployeeDTO employeeDTO) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        ErrorMessage isValidCode = validateCode(employeeDTO.getCode());
        ErrorMessage isValidName = validateName(employeeDTO.getName());
        ErrorMessage isValidEmail = validateEmail(employeeDTO.getEmail());
        ErrorMessage isValidAge = validateAge(employeeDTO.getAge());
        ErrorMessage isValidPhone = validatePhone(employeeDTO.getPhone());
        ErrorMessage isValidProvince = validateProvince(employeeDTO);
        ErrorMessage isValidDistrict = validateDistrict(employeeDTO);
        ErrorMessage isValidCommune = validateCommune(employeeDTO);
        if (!isValidCode.equals(success)) {
            return isValidCode;
        } else if (!isValidName.equals(success)) {
            return isValidName;
        } else if (!isValidEmail.equals(success)) {
            return isValidEmail;
        } else if (!isValidPhone.equals(success)) {
            return isValidPhone;
        } else if (!isValidAge.equals(success)) {
            return isValidAge;
        } else if (!isValidProvince.equals(success)) {
            return isValidProvince;
        } else if (!isValidDistrict.equals(success)) {
            return isValidDistrict;
        } else if (!isValidCommune.equals(success)) {
            return isValidCommune;
        }
        return success;
    }

    public ErrorMessage validateProvince(EmployeeDTO employeeDTO) {
        UUID provinceID = employeeDTO.getProvinceDto().getId();
        if (provinceRepository.existsProvinceById(provinceID)) {
            return ErrorMessage.SUCCESS;
        } else {
            return ErrorMessage.PROVINCE_ID_NOT_EXIST;
        }
    }

    public ErrorMessage validateDistrict(EmployeeDTO employeeDTO) {
        UUID provinceID = employeeDTO.getProvinceDto().getId();
        UUID districtID = employeeDTO.getDistrictDto().getId();
        if (districtRepository.existsDistrictById(districtID)) {
            if (districtRepository.findDistinctInProvince(provinceID, districtID) == null) {
                return ErrorMessage.DISTRICT_NOT_IN_PROVINCE;
            }
            return ErrorMessage.SUCCESS;
        }
        return ErrorMessage.DISTRICT_NOT_FOUND;


    }

    public ErrorMessage validateCommune(EmployeeDTO employeeDTO) {
        UUID communeID = employeeDTO.getCommuneDto().getId();
        UUID districtID = employeeDTO.getDistrictDto().getId();
        if (communeRepository.existsCommuneById(communeID)) {
            if (communeRepository.findCommuneInDistrict(districtID, communeID) == null) {
                return ErrorMessage.COMMUNE_NOT_IN_DISTRICT;
            }
            return ErrorMessage.SUCCESS;
        }
        return ErrorMessage.COMMUNE_NOT_FOUND;
    }

    public ErrorMessage validateCode(String code) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateBase.checkValidCodeContainSpace(code).equals(success)) {
            return ErrorMessage.CHARACTER_CODE_INVALID;
        } else if (!ValidateBase.checkCodeIsNull(code).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (!ValidateBase.checkValidLengthOfCode(code).equals(success)) {
            return ErrorMessage.LENGTH_CODE_INVALID;
        } else if (employeeRepository.existsEmployeeByCode(code)) {
            return ErrorMessage.CODE_IS_EXIST;
        }
        return success;
    }

    public ErrorMessage validateName(String name) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateBase.checkNameIsNull(name).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        }
        return success;
    }

    public ErrorMessage validateEmail(String email) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateBase.checkEmailIsNull(email).equals(success)) {
            return ErrorMessage.EMAIL_IS_NULL;
        } else if (ValidateBase.checkValidEmail(email).equals(success)) {
            return ErrorMessage.EMAIL_INVALID;
        } else if (employeeRepository.existsEmployeeByEmail(email)) {
            return ErrorMessage.EMAIL_IS_EXIST;
        }
        return success;
    }

    public ErrorMessage validatePhone(String phone) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateBase.checkPhoneIsNull(phone).equals(success)) {
            return ErrorMessage.PHONE_IS_NULL;
        } else if (ValidateBase.checkValidLengthOfPhone(phone).equals(success)) {
            return ErrorMessage.LENGTH_PHONE_INVALID;
        } else if (ValidateBase.checkValidPhoneIsNumber(phone).equals(success)) {
            return ErrorMessage.CHARACTER_PHONE_INVALID;
        }
        return success;
    }

    public ErrorMessage validateAge(int age) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (ValidateBase.checkValidAge(age).equals(success)) {
            return ErrorMessage.AGE_INVALID;
        }
        return success;
    }
}