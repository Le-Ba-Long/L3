package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Constants;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ProvinceService;
import com.globits.da.utils.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.globits.da.common.ErrorMessage.*;
import static com.globits.da.validate.ValidateBase.checkValidDto;

@Service
public class ProvinceServiceImpl extends GenericServiceImpl<Province, UUID> implements ProvinceService {
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ProvinceDto> getAll() {
        return provinceRepository.findAll().stream()
                .map(province -> modelMapper.map(province, ProvinceDto.class)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(UUID uuid) {
        if (provinceRepository.existsProvinceById(uuid)) {
            provinceRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

    @Override
    public ResponseData<ProvinceDto> insert(ProvinceDto provinceDto) {
        ResponseData<Province> errorMessage = validateProvince(null, provinceDto, Constants.INSERT);
        if (SUCCESS.getMessage().equals(errorMessage.getMessageError()))
            return new ResponseData<>(ProvinceDto.toDto(errorMessage.getData()));
        return new ResponseData<>(errorMessage.getStatusCode(),errorMessage.getMessageError(), null);
    }

    @Override
    public ResponseData<ProvinceDto> update(UUID id, ProvinceDto provinceDto) {
        ResponseData<Province> errorMessage = validateProvince(id, provinceDto, Constants.UPDATE);
        if (SUCCESS.getMessage().equals(errorMessage.getMessageError()))
            return new ResponseData<>(ProvinceDto.toDto(errorMessage.getData()));
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), null);
    }

    @Override
    public Page<ProvinceDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(PageUtil.validatePageIndex(pageIndex), PageUtil.validatePageSize(pageSize));
        return provinceRepository.findAll(pageable).map(province -> modelMapper.map(province, ProvinceDto.class));
    }


    public List<District> initDistrict(List<DistrictDto> districtDtoList, Province entity) {
        if (!districtDtoList.isEmpty() && !ObjectUtils.isEmpty(entity)) {
            return districtDtoList.stream()
                    .map(districtDto -> {
                        District district = new District();
                        district.setCode(districtDto.getCode());
                        district.setName(districtDto.getName());
                        district.setProvince(entity);
                        if (!ObjectUtils.isEmpty(districtDto.getCommuneDtoList())) {
                            district.setCommunes(initCommune(districtDto.getCommuneDtoList(), district));
                        }
                        return district;
                    }).collect(Collectors.toList());
        }
        return null;
    }

    public List<Commune> initCommune(List<CommuneDto> communeDtoList, District entity) {
        if (!communeDtoList.isEmpty() && !ObjectUtils.isEmpty(entity)) {
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

    public ResponseData<Province> validateProvince(UUID id, ProvinceDto provinceDto, String action) {
        if (Constants.INSERT.equals(action)) {
            if (ObjectUtils.isEmpty(provinceDto)) return new ResponseData<>(PROVINCE_IS_NULL, null);
            ErrorMessage errorProvince = checkValidDto(provinceDto, action);
            if (!SUCCESS.equals(errorProvince)) return new ResponseData<>(errorProvince, null);
            Province entity = new Province();
            entity.setCode(provinceDto.getCode());
            entity.setName(provinceDto.getName());
            if (!ObjectUtils.isEmpty(provinceDto.getDistrictDtoList())) {
                for (DistrictDto districtDto : provinceDto.getDistrictDtoList()) {
                    ErrorMessage errDistrict = checkValidDto(districtDto, action);
                    if (!SUCCESS.equals(errDistrict))
                        return new ResponseData<>(errDistrict, null);
                    if (!ObjectUtils.isEmpty(districtDto.getCommuneDtoList())) {
                        for (CommuneDto communeDto : districtDto.getCommuneDtoList()) {
                            ErrorMessage errCommune = checkValidDto(communeDto, action);
                            if (!SUCCESS.equals(errCommune))
                                return new ResponseData<>(errCommune, null);
                        }
                    }
                }
                entity.setDistricts(initDistrict(provinceDto.getDistrictDtoList(), entity));
            }
            return new ResponseData<>(provinceRepository.save(entity));
        }
        if (Constants.UPDATE.equals(action)) {
            ErrorMessage errorMessage = checkValidDto(provinceDto, action);
            if (!SUCCESS.equals(errorMessage)) return new ResponseData<>(errorMessage, null);
            Province province = provinceRepository.getById(id);
            province.setName(provinceDto.getName());
            province.setCode(provinceDto.getCode());
            if (!ObjectUtils.isEmpty(provinceDto.getDistrictDtoList())) {
                for (DistrictDto districtDto : provinceDto.getDistrictDtoList()) {
                    ErrorMessage errorDistrict = checkValidDto(districtDto, action);
                    if (!SUCCESS.equals(errorDistrict)) return new ResponseData<>(errorDistrict, null);
                    if (!ObjectUtils.isEmpty(districtDto.getId())) {
                        District district = districtRepository.getById(districtDto.getId());
                        district.setName(districtDto.getName());
                        district.setCode(districtDto.getCode());
                        district.setProvince(province);
                        if (!ObjectUtils.isEmpty(districtDto.getCommuneDtoList())) {
                            for (CommuneDto communeDto : districtDto.getCommuneDtoList()) {
                                ErrorMessage errorCommune = checkValidDto(communeDto, action);
                                if (!SUCCESS.equals(errorCommune)) return new ResponseData<>(errorCommune, null);
                                if (!ObjectUtils.isEmpty(communeDto.getId())) {
                                    Commune commune = communeRepository.getById(communeDto.getId());
                                    commune.setName(communeDto.getName());
                                    commune.setCode(communeDto.getCode());
                                    commune.setDistrict(district);
                                }
                            }
                        }
                    }
                }
            }
            return new ResponseData<>(provinceRepository.save(province));
        }
        return new ResponseData<>(ACTION_NOT_EXIST, null);
    }
}

