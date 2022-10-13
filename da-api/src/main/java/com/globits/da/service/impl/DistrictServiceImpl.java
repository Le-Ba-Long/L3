package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Constants;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.utils.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.globits.da.common.ErrorMessage.*;
import static com.globits.da.validate.ValidateBase.checkValidDto;

@Service
public class DistrictServiceImpl extends GenericServiceImpl<District, UUID> implements DistrictService {
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override

    public List<DistrictDto> getAll() {
        return districtRepository.findAll().stream().map(DistrictDto::toDto).collect(Collectors.toList());
    }

    @Override
    public ResponseData<DistrictDto> insert(DistrictDto districtDto) {
        ResponseData<District> resultErrorMessage = validateDistrict(null, districtDto, Constants.INSERT);
        if (SUCCESS.getMessage().equals(resultErrorMessage.getMessageError()))
            return new ResponseData<>(DistrictDto.toDto(resultErrorMessage.getData()));
        return new ResponseData<>(resultErrorMessage.getStatusCode(), resultErrorMessage.getMessageError(), null);
    }

    @Override
    public ResponseData<DistrictDto> update(UUID id, DistrictDto districtDto) {
        ResponseData<District> resultErrorMessage = validateDistrict(id, districtDto, Constants.UPDATE);
        if (SUCCESS.getMessage().equals(resultErrorMessage.getMessageError()))
            return new ResponseData<>(DistrictDto.toDto(resultErrorMessage.getData()));
        return new ResponseData<>(resultErrorMessage.getStatusCode(), resultErrorMessage.getMessageError(), null);
    }

    @Override
    public boolean deleteById(UUID uuid) {
        if (Boolean.TRUE.equals(districtRepository.existsDistrictById(uuid))) {
            districtRepository.deleteById(uuid);
            return true;
        }
        return false;
    }


    @Override
    public Page<DistrictDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(PageUtil.validatePageIndex(pageIndex), PageUtil.validatePageSize(pageSize));
        return districtRepository.findAll(pageable).map(district -> modelMapper.map(district, DistrictDto.class));
    }

    @Override
    public List<DistrictDto> findAllDistrictByProvinceId(UUID province_id) {
        return districtRepository.findAllDistrictByProvinceId(province_id)
                .stream().map(district -> modelMapper.map(district, DistrictDto.class)).collect(Collectors.toList());
    }

    public ResponseData<District> validateDistrict(UUID id, DistrictDto districtDto, String action) {
        if (Constants.INSERT.equals(action)) {
            if (ObjectUtils.isEmpty(districtDto.getProvinceDto())) return new ResponseData<>(PROVINCE_IS_NULL, null);
            if (Boolean.FALSE.equals(provinceRepository.existsProvinceById(districtDto.getProvinceDto().getId())))
                return new ResponseData<>(PROVINCE_ID_NOT_EXIST, null);
            ErrorMessage resultErrorMessage = checkValidDto(districtDto, action);
            if (!SUCCESS.equals(resultErrorMessage)) return new ResponseData<>(resultErrorMessage, null);
            District district = new District();
            district.setCode(districtDto.getCode());
            district.setName(districtDto.getName());
            district.setProvince(modelMapper.map(districtDto.getProvinceDto(), Province.class));
            if (!CollectionUtils.isEmpty(districtDto.getCommuneDtoList())) {
                List<Commune> communes = new ArrayList<>();
                for (CommuneDto communeDto : districtDto.getCommuneDtoList()) {
                    ErrorMessage errCommune = checkValidDto(communeDto, action);
                    if (!SUCCESS.equals(errCommune)) return new ResponseData<>(errCommune, null);
                    Commune commune = new Commune();
                    commune.setCode(communeDto.getCode());
                    commune.setName(communeDto.getName());
                    commune.setDistrict(district);
                    communes.add(commune);
                }
                if (!CollectionUtils.isEmpty(communes)) district.setCommunes(communes);
            }
            return new ResponseData<>(districtRepository.save(district));
        }
        if (Constants.UPDATE.equals(action)) {
            if (Boolean.FALSE.equals(districtRepository.existsDistrictById(id))) return new ResponseData<>(ID_NOT_EXIST, null);
            if (ObjectUtils.isEmpty(districtDto.getProvinceDto())) return new ResponseData<>(PROVINCE_IS_NULL, null);
            if (Boolean.FALSE.equals(provinceRepository.existsProvinceById(districtDto.getProvinceDto().getId()))) return new ResponseData<>(PROVINCE_ID_NOT_EXIST, null);
            District district = districtRepository.getById(id);
            ErrorMessage errorMessage = checkValidDto(districtDto, action);
            if (!SUCCESS.equals(errorMessage)) return new ResponseData<>(errorMessage, null);
            if (!StringUtils.isEmpty(districtDto.getProvinceDto())) {
                if (Boolean.FALSE.equals(provinceRepository.existsProvinceById(districtDto.getProvinceDto().getId())))
                    return new ResponseData<>(PROVINCE_ID_NOT_EXIST, null);
                Province province = provinceRepository.getById(districtDto.getProvinceDto().getId());
                district.setProvince(province);
            }
            district.setCode(districtDto.getCode());
            district.setName(districtDto.getName());
            List<Commune> communeList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(districtDto.getCommuneDtoList())) {
                for (CommuneDto communeDto : districtDto.getCommuneDtoList()) {
                    ErrorMessage errCommune = checkValidDto(communeDto, action);
                    if (!SUCCESS.equals(errCommune)) return new ResponseData<>(errCommune, null);
                    Commune commune = communeRepository.getById(communeDto.getId());
                    commune.setCode(communeDto.getCode());
                    commune.setName(communeDto.getName());
                    communeList.add(commune);
                }
            }
            if (!communeList.isEmpty()) district.setCommunes(communeList);
            return new ResponseData<>(districtRepository.save(district));
        }
        return new ResponseData<>(ACTION_NOT_EXIST, null);
    }
}
