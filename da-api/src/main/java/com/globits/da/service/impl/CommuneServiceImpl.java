package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.Constants;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.service.CommuneService;
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

import static com.globits.da.common.ErrorMessage.SUCCESS;
import static com.globits.da.validate.ValidateBase.checkValidDto;

@Service
public class CommuneServiceImpl extends GenericServiceImpl<Commune, UUID> implements CommuneService {
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CommuneDto> getAll() {
        return communeRepository.findAll()
                .stream()
                .map(commune -> modelMapper.map(commune, CommuneDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(UUID uuid) {
        if (communeRepository.existsCommuneById(uuid)) {
            communeRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

    @Override
    public ResponseData<CommuneDto> update(UUID id, CommuneDto communeDto) {
        ErrorMessage errorMessage = checkValidDto(communeDto, Constants.UPDATE);
        if (!SUCCESS.equals(errorMessage)) return new ResponseData<>(errorMessage, null);
        Commune commune = communeRepository.getById(id);
        commune.setCode(communeDto.getCode());
        commune.setName(communeDto.getName());
        if (!ObjectUtils.isEmpty(communeDto.getDistrictDto()) &&
                districtRepository.existsDistrictById(communeDto.getDistrictDto().getId())) {
                District district = districtRepository.getById(communeDto.getDistrictDto().getId());
                commune.setDistrict(district);
        }
        return new ResponseData<>(CommuneDto.toDto(communeRepository.save(commune)));
    }

    @Override
    public ResponseData<CommuneDto> insert(CommuneDto communeDto) {
        ErrorMessage errorMessage = checkValidDto(communeDto, Constants.INSERT);
        if (!SUCCESS.equals(errorMessage)) return new ResponseData<>(errorMessage, null);
        Commune commune = new Commune();
        commune.setCode(communeDto.getCode());
        commune.setName(communeDto.getName());
        commune.setDistrict(modelMapper.map(communeDto.getDistrictDto(), District.class));
        return new ResponseData<>(CommuneDto.toDto(communeRepository.save(commune)));
    }

    @Override
    public Page<CommuneDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(PageUtil.validatePageIndex(pageIndex), PageUtil.validatePageSize(pageSize));
        return communeRepository.findAll(pageable).map(commune -> modelMapper.map(commune, CommuneDto.class));
    }
}
