package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.ResponseData;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CommuneService extends GenericService<Commune, UUID> {
    List<CommuneDto> getAll();

    boolean deleteById(UUID uuid);

    ResponseData<CommuneDto> update(UUID id, CommuneDto communeDto);

    ResponseData<CommuneDto> insert(CommuneDto communeDto);

    Page<CommuneDto> getPage(int pageIndex, int pageSize);


}
