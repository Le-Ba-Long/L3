package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.ResponseData;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProvinceService extends GenericService<Province, UUID> {
    List<ProvinceDto> getAll();

    boolean deleteById(UUID uuid);

    ResponseData<ProvinceDto> update(UUID id, ProvinceDto provinceDto);

    ResponseData<ProvinceDto> insert(ProvinceDto provinceDto);

    Page<ProvinceDto> getPage(int pageIndex, int pageSize);

}
