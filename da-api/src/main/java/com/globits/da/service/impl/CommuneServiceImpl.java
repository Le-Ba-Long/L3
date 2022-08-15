package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.dto.search.ProvinceSearchDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.service.CommuneService;
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
public class CommuneServiceImpl extends GenericServiceImpl<Commune, UUID> implements CommuneService {
    @Autowired
    CommuneRepository communeRepository;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CommuneDto> getAll() {
        return communeRepository.findAll()
                .stream()
                .map(commune -> {
                    return modelMapper.map(commune, CommuneDto.class);
                }).collect(Collectors.toList());
    }

    @Override
    public CommuneDto save(CommuneDto communeDto) {
        communeDto.getId();
        return modelMapper.map(communeRepository.save(modelMapper.map(communeDto, Commune.class)), CommuneDto.class);
    }

    @Override
    public Boolean deleteById(UUID uuid) {
        if (communeRepository.existsCommuneById(uuid)) {
            communeRepository.deleteById(uuid);
            return true;
        }
        return false;
    }

    @Override
    public ResponseRequest<CommuneDto> update(UUID id, CommuneDto communeDto) {
        if (communeRepository.existsCommuneById(id)) {
            if (districtRepository.existsDistrictById(communeDto.getDistrictDto().getId())) {
                String resultErrorMessage = checkValidCodeAndName(communeDto).getMessage();
                String updateSuccess = ErrorMessage.SUCCESS.getMessage();
                if (updateSuccess.equals(resultErrorMessage)) {
                    return new ResponseRequest<>(
                            ErrorMessage.SUCCESS.getCode(),
                            ErrorMessage.SUCCESS.getMessage(),
                            modelMapper.map(communeRepository.findById(id)
                                    .map(commune -> {
                                        commune.setCode(communeDto.getCode());
                                        commune.setName(communeDto.getName());
                                        return commune;
                                    }), CommuneDto.class));
                } else {
                    return new ResponseRequest<>(
                            ValidateProvince.resultStatusCode(checkValidCodeAndName(communeDto)),
                            resultErrorMessage,
                            communeDto);
                }
            } else {
                return new ResponseRequest<>(
                        ErrorMessage.DISTRICT_NOT_FOUND.getCode(),
                        ErrorMessage.DISTRICT_NOT_FOUND.getMessage(),
                        communeDto);
            }
        }
        return new ResponseRequest<>(
                ErrorMessage.ID_NOT_EXIST.getCode(),
                ErrorMessage.ID_NOT_EXIST.getMessage(),
                communeDto);
    }

    @Override
    public ResponseRequest<CommuneDto> insert(CommuneDto communeDto) {
        if (districtRepository.existsDistrictById(communeDto.getDistrictDto().getId())) {
            String resultErrorMessage = checkValidCommune(communeDto).getMessage();
            String insertSuccess = ErrorMessage.SUCCESS.getMessage();
            if (insertSuccess.equals(resultErrorMessage)) {
                communeDto.setId(UUID.randomUUID());
                return new ResponseRequest<>(
                        ErrorMessage.SUCCESS.getCode(),
                        ErrorMessage.SUCCESS.getMessage(),
                        modelMapper.map(communeRepository.save(modelMapper.map(communeDto, Commune.class)), CommuneDto.class));
            }
            return new ResponseRequest<>(
                    ValidateProvince.resultStatusCode(checkValidCommune(communeDto)),
                    resultErrorMessage,
                    communeDto);
        }
        return new ResponseRequest<>(ErrorMessage.DISTRICT_NOT_FOUND.getCode(),
                ErrorMessage.DISTRICT_NOT_FOUND.getMessage(), communeDto);
    }

    @Override
    public Page<CommuneDto> getPage(int pageIndex, int pageSize) {
        Pageable pageable = PageRequest.of(PageUtil.validatePageIndex(pageIndex), PageUtil.validatePageSize(pageSize));
        return communeRepository.findAll(pageable)
                .map(commune -> {
                    return modelMapper.map(commune, CommuneDto.class);
                });
    }

    @Override
    public Page<CommuneDto> search(ProvinceSearchDto dto) {
        return null;
    }

    @Override
    public CommuneDto getByID(UUID uuid) {
        return modelMapper.map(communeRepository.findById(uuid), CommuneDto.class);
    }

    @Override
    public Boolean checkCodeHasExist(String code) {
        return communeRepository.findByCode(code) == null;
    }

    @Override
    public String resultErrorMessageId(UUID id) {
        if (communeRepository.existsCommuneById(id))
            return ErrorMessage.ID_IS_EXIST.getMessage();
        return null;
    }

    //kiểm tra xem xã có hợp lệ không ,không hợp lệ trả ra errormessage tương ứng không có trả về  ErrorMessage.SUCCESS
    public ErrorMessage checkValidCommune(CommuneDto communeDto) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateProvince.checkCodeIsNull(communeDto.getCode()).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (communeRepository.existsCommuneByCode(communeDto.getCode())) {
            return ErrorMessage.CODE_IS_EXIST;
        } else if (!ValidateProvince.checkNameIsNull(communeDto.getName()).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        } else if (communeRepository.existsCommuneByName(communeDto.getName())) {
            return ErrorMessage.NAME_IS_EXIST;
        }
        return ErrorMessage.SUCCESS;
    }

    public ErrorMessage checkValidCodeAndName(CommuneDto communeDto) {
        ErrorMessage success = ErrorMessage.SUCCESS;
        if (!ValidateProvince.checkCodeIsNull(communeDto.getCode()).equals(success)) {
            return ErrorMessage.CODE_IS_NULL;
        } else if (!ValidateProvince.checkNameIsNull(communeDto.getName()).equals(success)) {
            return ErrorMessage.NAME_IS_NULL;
        }
        return ErrorMessage.SUCCESS;
    }
}
