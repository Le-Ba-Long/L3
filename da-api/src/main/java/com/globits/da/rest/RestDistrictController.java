package com.globits.da.rest;

import com.globits.da.common.ErrorMessage;
import com.globits.da.domain.District;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.service.DistrictService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/districts")
public class RestDistrictController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestDistrictController.class);
    @Autowired
    DistrictService districtService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseRequest<List<DistrictDto>> getAll() {
        List<DistrictDto> listDistrict = districtService.getAll();
        if (listDistrict.isEmpty()) {
            return new ResponseRequest<>(
                    ErrorMessage.LIST_IS_EMPTY.getCode(),
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    listDistrict);
        }
        return new ResponseRequest<>(
                ErrorMessage.SUCCESS.getCode(),
                ErrorMessage.SUCCESS.getMessage(),
                listDistrict);
    }

    @PostMapping("/insert")
    public ResponseRequest<?> insert(@RequestBody DistrictDto districtDto) {
        String resultErrorMessage = districtService.insert(districtDto).getMessageError();
        int resultErrorCode = districtService.insert(districtDto).getStatusCode();
        if (resultErrorMessage.equals(ErrorMessage.SUCCESS.getMessage())) {
            return new ResponseRequest<DistrictDto>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    districtDto);
        }
        return new ResponseRequest<DistrictDto>(
                resultErrorCode,
                resultErrorMessage,
                districtDto);

    }

    @PutMapping("/update/{id}")
    public ResponseRequest<DistrictDto> update(@PathVariable(name = "id") UUID id, @RequestBody DistrictDto districtDto) {
        String resultErrorMessage = districtService.update(id, districtDto).getMessageError();
        int resultErrorCode = districtService.update(id, districtDto).getStatusCode();
        if (resultErrorMessage.equals(ErrorMessage.SUCCESS.getMessage())) {
            return new ResponseRequest<DistrictDto>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(),
                    districtDto);
        }
        return new ResponseRequest<DistrictDto>(
                resultErrorCode,
                resultErrorMessage,
                districtDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseRequest<DistrictDto> delete(@PathVariable("id") UUID id) {
        District district = districtService.findById(id);
        if (districtService.deleteById(id)) {
            LOGGER.info("Delete Success");
            return new ResponseRequest<>(
                    ErrorMessage.SUCCESS.getCode(),
                    ErrorMessage.SUCCESS.getMessage(), modelMapper.map(district, DistrictDto.class));
        } else {
            LOGGER.info("Delete Not Success");
            return new ResponseRequest<>(ErrorMessage.ID_NOT_EXIST.getCode(),
                    ErrorMessage.ID_NOT_EXIST.getMessage(),
                    null);
        }
    }

    @GetMapping("/list/{id}")
    ResponseRequest<List<DistrictDto>> findAllDistrictByProvinceId(@PathVariable("id") UUID provinceId) {
        List<DistrictDto> districtDtoList = districtService.findAllDistrictByProvinceId(provinceId);
        if (districtDtoList.isEmpty()) {
            return new ResponseRequest<>(ErrorMessage.LIST_IS_EMPTY.getCode(),
                    ErrorMessage.LIST_IS_EMPTY.getMessage(),
                    districtDtoList);
        }
        return new ResponseRequest<>(ErrorMessage.SUCCESS.getCode(),
                ErrorMessage.SUCCESS.getMessage(),
                districtDtoList);
    }
}
