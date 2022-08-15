package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommuneDto extends BaseObjectDto {
    private String code;
    private String name;
    private DistrictDto districtDto;

}
