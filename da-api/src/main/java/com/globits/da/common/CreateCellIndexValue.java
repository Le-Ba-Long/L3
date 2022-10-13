package com.globits.da.common;

public enum CreateCellIndexValue {
    ID(0),
    CODE(1),
    NAME(2),
    EMAIL(3),
    PHONE(4),
    AGE(5),

    PROVINCE(6),

    DISTRICT(7),

    COMMUNE(8);

    CreateCellIndexValue(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    private final int cellIndex;

    public int getCellIndex() {
        return cellIndex;
    }
}


