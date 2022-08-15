package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.dto.search.ProvinceSearchDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ProvinceService;
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
public class ProvinceServiceImpl extends GenericServiceImpl<Province, UUID> implements ProvinceService {
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    CommuneRepository communeRepository;
    @Autowired
    ModelMapper modelMapper;
//    @Autowired
//    DistrictServiceImpl districtService;

    @Override
    public List<ProvinceDto> getAll() {
        return provinceRepository.findAll()
                .stream()
                .map(province -> {
                    return modelMapper.map(province, ProvinceDto.class);
                }).collect(Collectors.toList());
    }

    @Override
    public ProvinceDto save(ProvinceDto provinceDto) {
        // provinceDto.setId(UUID.randomUUID());
        // provinceDto.getId();
        return modelMapper.map(provinceRepository.save(modelMapper.map(provinceDto, Province.class)), ProvinceDto.class);
    }

    @Override
    public Boolean deleteById(UUID uuid) {
        if (provinceRepository.existsProvinceById(uuid)) {
            provinceRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

    @Override
    public ResponseRequest<ProvinceDto> update(UUID id, ProvinceDto provinceDto) {
        if (provinceRepository.existsProvinceById(id)) {
            String resultErrorMessage = checkValidCodeAndName(provinceDto).getMessage();
            String updateSuccess = ErrorMessage.SUCCESS.getMessage();
            if (updateSuccess.equals(resultErrorMessage)) {
                Province entity = provinceRepository.getOne(id);
                entity.setName(provinceDto.getName());
                entity.setCode(provinceDto.getCode());
                if (provinceDto.getDistrictDtoList() != null) {
                    for (DistrictDto districtDto : provinceDto.getDistrictDtoList()) {
                        if (districtDto.getId() != null) {
                            districtRepository.findById(districtDto.getId())
                                    .map(district -> {
                                        district.setName(districtDto.getName());
                                        district.setCode(districtDto.getCode());
                                        district.setProvince(entity);
                                        return district;
                                    });
                            for (CommuneDto communeDto : districtDto.getCommuneDtoList()) {
                                if (communeDto.getId() != null) {
                                    communeRepository.findById(communeDto.getId())
                                            .map(commune -> {
                                                commune.setName(communeDto.getName());
                                                commune.setCode(communeDto.getCode());
                                                //commune.setDistrict(district);
                                                return commune;
                                            });
                                }
                            }
                        }
                    }
                }
                return new ResponseRequest<>(
                        ErrorMessage.SUCCESS.getCode(),
                        ErrorMessage.SUCCESS.getMessage(),
                        modelMapper.map(provinceRepository.save(entity),
                                ProvinceDto.class));
            } else {
                return new ResponseRequest<>(
                        ValidateProvince.resultStatusCode(
                                checkValidCodeAndName(provinceDto)),
                        resultErrorMessage, provinceDto);
            }
        }
        return new ResponseRequest<>(ErrorMessage.ID_NOT_EXIST.getCode(), ErrorMessage.ID_NOT_EXIST.getMessage(), provinceDto);
    }

    @Override
    public ResponseRequest<ProvinceDto> insert(ProvinceDto provinceDto) {
        if (provinceDto != null) {
            String resultErrorMessage = checkValidProvince(provinceDto).getMessage();
            String provinceSuccess = ErrorMessage.SUCCESS.getMessage();
            if (provinceSuccess.equals(resultErrorMessage)) {
                Province entity = new Province();
                entity.setCode(provinceDto.getCode());
                entity.setName(provinceDto.getName());
                if (provinceDto.getDistrictDtoList() != null) {
                    entity.setDistricts(initDistrict(provinceDto.getDistrictDtoList(), entity));
                }
                return new ResponseRequest<>(
                        ErrorMessage.SUCCESS.getCode(),
                        ErrorMessage.SUCCESS.getMessage(),
                        modelMapper.map(provinceRepository.save(entity),
                                ProvinceDto.class));
            }
            return new ResponseRequest<>(
                    ValidateProvince.resultStatusCode
                            (checkValidProvince(provinceDto)),
                    resultErrorMessage, provinceDto);
        }
        return null;
    }


    @Override
    public Page<ProvinceDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(PageUtil.validatePageIndex(pageIndex), PageUtil.validatePageSize(pageSize));
        return provinceRepository.findAll(pageable)
                .map(province -> {
                    return modelMapper.map(province, ProvinceDto.class);
                });
    }

    @Override
    public Page<ProvinceDto> search(ProvinceSearchDto dto) {
        return null;
    }

    @Override
    public ProvinceDto getByID(UUID uuid) {
        return modelMapper.map(provinceRepository.findById(uuid), ProvinceDto.class);
    }

    //kiểm tra xem tỉnh có hợp lệ không ,không hợp lệ trả ra errormessage tương ứng không có trả về  ErrorMessage.SUCCESS
    public ErrorMessage checkValidProvince(ProvinceDto provinceDto) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateProvince.checkCodeIsNull(provinceDto.getCode()).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (provinceRepository.existsProvinceByCode(provinceDto.getCode())) {
            return ErrorMessage.CODE_IS_EXIST;
        } else if (!ValidateProvince.checkNameIsNull(provinceDto.getName()).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        } else if (provinceRepository.existsProvinceByName(provinceDto.getName())) {
            return ErrorMessage.NAME_IS_EXIST;
        }
        return ErrorMessage.SUCCESS;
    }

    public ErrorMessage checkValidCodeAndName(ProvinceDto provinceDto) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateProvince.checkCodeIsNull(provinceDto.getCode()).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (!ValidateProvince.checkNameIsNull(provinceDto.getName()).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        }
        return ErrorMessage.SUCCESS;
    }

    /* khởi tạo một list district từ list districtDto do người dùng nhập vào
     * sau đó map tương ứng các district dto rồi lưu  vào thành một list distric để
     * lưu vào Database
     * */
    public List<District> initDistrict(List<DistrictDto> districtDtoList, Province entity) {
        if (!districtDtoList.isEmpty() && entity != null) {
            return districtDtoList.stream()
                    .map(districtDto -> {
                        District district = new District();
                        district.setCode(districtDto.getCode());
                        district.setName(districtDto.getName());
                        district.setProvince(entity);
                        if (districtDto.getCommuneDtoList() != null) {
                            district.setCommunes(initCommune(districtDto.getCommuneDtoList(), district));
                        }
                        return district;
                    }).collect(Collectors.toList());
        }
        return null;
    }


    public List<Commune> initCommune(List<CommuneDto> communeDtoList, District entity) {
        if (!communeDtoList.isEmpty() && entity != null) {
            return communeDtoList.stream()
                    .map(communeDto -> {
                        Commune commune = new Commune();
                        commune.setCode(communeDto.getCode());
                        commune.setName(communeDto.getName());
                        commune.setDistrict(entity);
                        return commune;
                    }).collect(Collectors.toList());
        }
        return null;
    }
}
