package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DistrictDto extends BaseObjectDto {
    private String code;
    private String name;
    private ProvinceDto provinceDto;
    List<CommuneDto> communeDtoList;
}
