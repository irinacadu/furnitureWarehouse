package com.furnitureWarehouse.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Table
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Furniture {

    @Id
    private String id;
    private String owner;
    private String type;
    private String material;
    private int yearsInStorage;
    private boolean warehouseOutput;

}
