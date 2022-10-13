package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.Commune;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommuneDto extends BaseObjectDto {
    private String code;

    private String name;

    private DistrictDto districtDto;

    public static CommuneDto toDto(Commune entity) {
        CommuneDto dto = new ModelMapper().map(entity, CommuneDto.class);
        dto.setDistrictDto(DistrictDto.toDto(entity.getDistrict()));
        return dto;
    }

}
