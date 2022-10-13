package com.globits.da.dto.search;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployeeSearchDTO {
    private UUID employeeId;

    private UUID cityId;

    private UUID districtId;

    private String createDate;

    private Integer total;

    private UUID id;

    private String keyword;

    private String orderBy;

    private Integer pageSize;

    private Integer pageIndex;

}