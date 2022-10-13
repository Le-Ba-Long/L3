package com.globits.da.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchDto {
    private Integer pageIndex;

    private Integer pageSize;

    private String keyword;

    private Boolean voided;

    private UUID khoId;

    private String orderBy;

    private String text;

    private UUID productCategory;

    private Date fromDate;

    private Date toDate;

}
