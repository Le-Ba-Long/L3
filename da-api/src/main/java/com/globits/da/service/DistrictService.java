package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.District;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.dto.search.ProvinceSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DistrictService extends GenericService<District, UUID> {
    public List<DistrictDto> getAll();

    public DistrictDto save(DistrictDto districtDto);

    public Boolean deleteById(UUID uuid);

    public ResponseRequest<DistrictDto> update(UUID id, DistrictDto districtDto);

    public ResponseRequest<DistrictDto> insert(DistrictDto districtDto);

    public Page<DistrictDto> getPage(int pageIndex, int pageSize);

    Page<DistrictDto> search(ProvinceSearchDto dto);

    List<DistrictDto> findAllDistrictByProvinceId(UUID province_id);

}
