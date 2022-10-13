package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.District;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DistrictDto extends BaseObjectDto {
    private String code;

    private String name;

    private ProvinceDto provinceDto;

    private List<CommuneDto> communeDtoList;
    private static ModelMapper modelMapper = new ModelMapper();

    public static DistrictDto toDto(District entity) {
        DistrictDto dto = modelMapper.map(entity, DistrictDto.class);
        dto.setProvinceDto(modelMapper.map(entity.getProvince(), ProvinceDto.class));
        dto.setCommuneDtoList(entity.getCommunes().stream().map(commune -> modelMapper.map(commune, CommuneDto.class)).collect(Collectors.toList()));
        return dto;
    }

}
