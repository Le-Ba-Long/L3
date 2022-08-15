package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Province;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.dto.search.ProvinceSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProvinceService extends GenericService<Province, UUID> {
    public List<ProvinceDto> getAll();

    public ProvinceDto save(ProvinceDto provinceDto);

    public Boolean deleteById(UUID uuid);

    public ResponseRequest<ProvinceDto> update(UUID id, ProvinceDto provinceDto);

    public ResponseRequest<ProvinceDto> insert(ProvinceDto provinceDto);

    public Page<ProvinceDto> getPage(int pageIndex, int pageSize);

    Page<ProvinceDto> search(ProvinceSearchDto dto);

    public ProvinceDto getByID(UUID uuid);

}
