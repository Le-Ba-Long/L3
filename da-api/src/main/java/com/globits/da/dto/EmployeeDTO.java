package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDto extends BaseObjectDto implements Comparable<EmployeeDto> {
    private String code;

    private String name;

    private String email;

    private String phone;

    private Integer age;

    private ProvinceDto provinceDto;

    private DistrictDto districtDto;

    private CommuneDto communeDto;
    private static ModelMapper modelMapper = new ModelMapper();

    public static EmployeeDto toDto(Employee entity) {
        EmployeeDto dto = modelMapper.map(entity, EmployeeDto.class);
        dto.setProvinceDto(modelMapper.map(entity.getProvince(), ProvinceDto.class));
        dto.setDistrictDto(modelMapper.map(entity.getDistrict(), DistrictDto.class));
        dto.setCommuneDto(modelMapper.map(entity.getCommune(), CommuneDto.class));
        return dto;
    }

    @Override
    public int compareTo(EmployeeDto e) {
        return this.getName().substring(this.getName().lastIndexOf(' '))
                .compareTo(e.getName().substring(e.getName().lastIndexOf(' ')));
    }
}
