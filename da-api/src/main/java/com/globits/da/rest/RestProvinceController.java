package com.globits.da.rest;

import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.ResponseData;
import com.globits.da.service.ProvinceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.globits.da.common.ErrorMessage.*;

@RestController
@RequestMapping("/api/provinces")
public class RestProvinceController {
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/list")
    public ResponseData<List<ProvinceDto>> getAll() {
        List<ProvinceDto> listProvince = provinceService.getAll();
        if (listProvince.isEmpty()) return new ResponseData<>(LIST_IS_EMPTY, listProvince);
        return new ResponseData<>(listProvince);
    }

    @PostMapping()
    public ResponseData<ProvinceDto> insert(@RequestBody ProvinceDto provinceDto) {
        ResponseData<ProvinceDto> errorMessage = provinceService.insert(provinceDto);
        if (errorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errorMessage.getData());
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), errorMessage.getData());
    }

    @PutMapping("/{id}")
    public ResponseData<ProvinceDto> update(@PathVariable(name = "id") UUID id, @RequestBody ProvinceDto provinceDto) {
        ResponseData<ProvinceDto> errorMessage = provinceService.update(id, provinceDto);
        if (errorMessage.getMessageError().equals(SUCCESS.getMessage()))
            return new ResponseData<>(errorMessage.getData());
        return new ResponseData<>(errorMessage.getStatusCode(), errorMessage.getMessageError(), errorMessage.getData());
    }

    @DeleteMapping("/{id}")
    public ResponseData<ProvinceDto> delete(@PathVariable("id") UUID id) {
        Province province = provinceService.findById(id);
        if (provinceService.deleteById(id))
            return new ResponseData<>(modelMapper.map(province, ProvinceDto.class));
        return new ResponseData<>(ID_NOT_EXIST, null);
    }
}
