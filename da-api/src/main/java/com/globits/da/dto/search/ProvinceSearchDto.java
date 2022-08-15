package com.globits.da.dto.search;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProvinceSearchDto extends BaseObjectDto {
    private String keyword;
    private String orderBy;
    private Integer pageIndex;
    private Integer pageSize;
}
