package com.globits.da.utils;

import com.globits.da.Constants;

public class PageUtil {
    public static Integer validatePageIndex(Integer pageIndex) {
        if (pageIndex > 0) {
            return --pageIndex;
        }
        return 0;
    }

    public static Integer validatePageSize(Integer pageSize) {
        if (pageSize < 1) {
            return Constants.DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }
}