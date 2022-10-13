package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProvinceDto extends BaseObjectDto {
    private String code;

    private String name;

    private List<DistrictDto> districtDtoList;

    public static ProvinceDto toDto(Province entity) {
        ProvinceDto dto = new ModelMapper().map(entity, ProvinceDto.class);
        //  return new ModelMapper().map(district, DistrictDto.class);
        dto.setDistrictDtoList(entity.getDistricts().stream().map(DistrictDto::toDto).collect(Collectors.toList()));
        return dto;
    }
}
