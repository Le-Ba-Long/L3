package com.globits.da.rest;

import com.globits.da.domain.District;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.service.DistrictService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.globits.da.common.ErrorMessage.*;

@RestController
@RequestMapping("/api/districts")
public class RestDistrictController {

    @Autowired
    private DistrictService districtService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseData<List<DistrictDto>> getAll() {
        List<DistrictDto> listDistrict = districtService.getAll();
        if (listDistrict.isEmpty()) return new ResponseData<>(LIST_IS_EMPTY, listDistrict);
        return new ResponseData<>(listDistrict);
    }

    @PostMapping()
    public ResponseData<DistrictDto> insert(@RequestBody DistrictDto districtDto) {
        ResponseData<DistrictDto> resultErrorMessage = districtService.insert(districtDto);
        if (resultErrorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(resultErrorMessage.getData());
        return new ResponseData<>(resultErrorMessage.getStatusCode(), resultErrorMessage.getMessageError(), null);
    }

    @PutMapping("/{id}")
    public ResponseData<DistrictDto> update(@PathVariable UUID id, @RequestBody DistrictDto districtDto) {
        ResponseData<DistrictDto> resultErrorMessage = districtService.update(id, districtDto);
        if (resultErrorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(resultErrorMessage.getData());
        return new ResponseData<>(resultErrorMessage.getStatusCode(), resultErrorMessage.getMessageError(), null);
    }

    @DeleteMapping("/{id}")
    public ResponseData<DistrictDto> delete(@PathVariable("id") UUID id) {
        District district = districtService.findById(id);
        if (districtService.deleteById(id)) return new ResponseData<>(modelMapper.map(district, DistrictDto.class));
        return new ResponseData<>(ID_NOT_EXIST, null);
    }

    @GetMapping("/list/{id}")
    ResponseData<List<DistrictDto>> findAllDistrictByProvinceId(@PathVariable("id") UUID provinceId) {
        List<DistrictDto> districtDtoList = districtService.findAllDistrictByProvinceId(provinceId);
        if (districtDtoList.isEmpty()) return new ResponseData<>(LIST_IS_EMPTY, districtDtoList);
        return new ResponseData<>(districtDtoList);
    }

}
