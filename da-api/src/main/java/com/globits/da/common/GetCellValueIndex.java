package com.globits.da.common;

public enum GetCellValueIndex {
    Code(0),
    Name(1),
    Email(2),
    Phone(3),
    Age(4),

    Province(5),

    District(6),

    Commune(7);

    GetCellValueIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    private final int cellIndex;

    public int getCellIndex() {
        return cellIndex;
    }

}
