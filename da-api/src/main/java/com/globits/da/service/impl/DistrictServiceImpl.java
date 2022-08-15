package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.dto.search.ProvinceSearchDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.utils.PageUtil;
import com.globits.da.validate.ValidateProvince;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl extends GenericServiceImpl<District, UUID> implements DistrictService {
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<DistrictDto> getAll() {
        return repository.findAll()
                .stream()
                .map(district -> {
                    return modelMapper.map(district, DistrictDto.class);
                }).collect(Collectors.toList());
    }

    @Override
    public DistrictDto save(DistrictDto districtDto) {
        districtDto.getId();
        return modelMapper.map(repository.save(modelMapper.map(districtDto, District.class)), DistrictDto.class);
    }

    @Override
    public Boolean deleteById(UUID uuid) {
        if (districtRepository.existsDistrictById(uuid)) {
            districtRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

    @Override
    public ResponseRequest<DistrictDto> update(UUID id, DistrictDto districtDto) {
        if (districtRepository.existsDistrictById(id)) {
            String resultErrorMessage = checkValidCodeAndName(districtDto).getMessage();
            String updateSuccess = ErrorMessage.SUCCESS.getMessage();
            if (updateSuccess.equals(resultErrorMessage)) {
                return new ResponseRequest<>(
                        ErrorMessage.SUCCESS.getCode(),
                        ErrorMessage.SUCCESS.getMessage(),
                        modelMapper.map(repository.findById(id)
                                .map(district -> {
                                    district.setCode(districtDto.getCode());
                                    district.setName(districtDto.getName());
                                    Boolean hasProvinceId = provinceRepository.existsProvinceById(
                                            districtDto.getProvinceDto().getId());
                                    if (!hasProvinceId) {
                                        throw new IllegalArgumentException("Not exist the province with this ID");
                                    }
                                    district.setProvince(modelMapper.map(
                                            districtDto.getProvinceDto(),
                                            Province.class));
                                    return district;
                                }), DistrictDto.class));
            } else {
                return new ResponseRequest<>(
                        ValidateProvince.resultStatusCode(checkValidCodeAndName(districtDto)),
                        resultErrorMessage,
                        districtDto);
            }
        }
        return new ResponseRequest<>(
                ErrorMessage.ID_NOT_EXIST.getCode(),
                ErrorMessage.ID_NOT_EXIST.getMessage(),
                districtDto);
    }

    @Override
    public ResponseRequest<DistrictDto> insert(DistrictDto districtDto) {
        ProvinceDto provinceDto = districtDto.getProvinceDto();
        if (provinceRepository.existsProvinceById(provinceDto.getId())) {
            String resultErrorMessage = checkValidDistrict(districtDto).getMessage();
            String insertSuccess = ErrorMessage.SUCCESS.getMessage();
            if (insertSuccess.equals(resultErrorMessage)) {
                districtDto.setId(UUID.randomUUID());
                return new ResponseRequest<>(
                        ErrorMessage.SUCCESS.getCode(),
                        ErrorMessage.SUCCESS.getMessage(),
                        modelMapper.map(districtRepository.save(modelMapper.map(districtDto, District.class)), DistrictDto.class));
            } else {
                return new ResponseRequest<>(
                        ValidateProvince.resultStatusCode(checkValidDistrict(districtDto)),
                        resultErrorMessage,
                        districtDto);
            }
        }
        return new ResponseRequest<DistrictDto>(
                ErrorMessage.PROVINCE_ID_NOT_EXIST.getCode(),
                ErrorMessage.PROVINCE_ID_NOT_EXIST.getMessage(),
                districtDto);
    }

    @Override
    public Page<DistrictDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(PageUtil.validatePageIndex(pageIndex), PageUtil.validatePageSize(pageSize));
        return districtRepository.findAll(pageable)
                .map(district -> {
                    return modelMapper.map(district, DistrictDto.class);
                });
    }

    @Override
    public Page<DistrictDto> search(ProvinceSearchDto dto) {
        return null;
    }

    @Override
    public List<DistrictDto> findAllDistrictByProvinceId(UUID province_id) {
        return districtRepository.findAllDistrictByProvinceId(province_id)
                .stream()
                .map(district -> {
                    return modelMapper.map(district, DistrictDto.class);
                }).collect(Collectors.toList());
    }


    //kiểm tra xem tỉnh có hợp lệ không ,không hợp lệ trả ra errormessage tương ứng không có trả về  ErrorMessage.SUCCESS
    public ErrorMessage checkValidDistrict(DistrictDto districtDto) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateProvince.checkCodeIsNull(districtDto.getCode()).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (districtRepository.existsDistrictByCode(districtDto.getCode())) {
            return ErrorMessage.CODE_IS_EXIST;
        } else if (!ValidateProvince.checkNameIsNull(districtDto.getName()).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        } else if (districtRepository.existsDistrictByName(districtDto.getName())) {
            return ErrorMessage.NAME_IS_EXIST;
        }
        return ErrorMessage.SUCCESS;
    }


    //Kiểm tra code và name có rỗng hoặc null không
    public ErrorMessage checkValidCodeAndName(DistrictDto districtDto) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateProvince.checkCodeIsNull(districtDto.getCode()).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (!ValidateProvince.checkNameIsNull(districtDto.getName()).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        }
        return ErrorMessage.SUCCESS;
    }
}
