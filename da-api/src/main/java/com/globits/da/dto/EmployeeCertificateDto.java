package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmployeeCertificateDto extends BaseObjectDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;

    private EmployeeDto employeeDTO;

    private ProvinceDto provinceDto;

    private CertificateDto certificateDto;
}
