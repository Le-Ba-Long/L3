package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.Employee;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeDTO extends BaseObjectDto {
    private String code;
    private String name;
    private String email;
    private String phone;
    private int age;
    private ProvinceDto provinceDto;
    private DistrictDto districtDto;
    private CommuneDto communeDto;

//    public EmployeeDTO(String code, String name, String email, String phone, int age) {
//        this.code = code;
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//        this.age = age;
//    }

//    public EmployeeDTO formatTheLineToObject(String line) {
//        String[] param = line.split("-");
//        System.out.println(param);
//        return new EmployeeDTO((long) Float.parseFloat(param[0]), param[1], param[2], param[3], param[4], (int) Float.parseFloat(param[5]));
//    }
}
