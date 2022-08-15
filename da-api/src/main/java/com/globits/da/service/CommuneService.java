package com.globits.da.service;

import com.globits.core.service.GenericService;
import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.ResponseRequest;
import com.globits.da.dto.search.ProvinceSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CommuneService extends GenericService<Commune, UUID> {
    public List<CommuneDto> getAll();

    public CommuneDto save(CommuneDto communeDto);

    public Boolean deleteById(UUID uuid);

    public ResponseRequest<CommuneDto> update(UUID id, CommuneDto communeDto);

    public ResponseRequest<CommuneDto> insert(CommuneDto communeDto);

    public Page<CommuneDto> getPage(int pageIndex, int pageSize);

    Page<CommuneDto> search(ProvinceSearchDto dto);

    public CommuneDto getByID(UUID uuid);

    Boolean checkCodeHasExist(String code);

    public String resultErrorMessageId(UUID id);
}
