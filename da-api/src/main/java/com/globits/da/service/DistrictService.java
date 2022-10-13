package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.District;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ResponseData;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface DistrictService extends GenericService<District, UUID> {
    List<DistrictDto> getAll();

    boolean deleteById(UUID uuid);

    ResponseData<DistrictDto> update(UUID id, DistrictDto districtDto);

    ResponseData<DistrictDto> insert(DistrictDto districtDto);

    Page<DistrictDto> getPage(int pageIndex, int pageSize);

    List<DistrictDto> findAllDistrictByProvinceId(UUID province_id);

}
