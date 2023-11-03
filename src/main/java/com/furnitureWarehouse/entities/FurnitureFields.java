package com.furnitureWarehouse.entities;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum FurnitureFields {

    ID(0,"id"),
    OWNER(1,"owner"),
    TYPE_FURNITURE(2,"type"),
    MATERIAL(3,"material"),
    YEARS_IN_STORAGE (4,"yearsInStorage"),
    WAREHOUSE_OUTPUT(5,"warehouseOutput");


    private final int index;
    private final String name;


}
