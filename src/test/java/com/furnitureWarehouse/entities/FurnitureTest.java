package com.furnitureWarehouse.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Clase que testea la entidad Furniture")
class FurnitureTest {

    @Test
    @DisplayName("Test getters/setters de la entidad Furniture")
    void FurnitureEntityTest() {
        Furniture furniture = new Furniture();

        furniture.setType("Piedra");
        String expected = "Piedra";
        String real = furniture.getType();

        Assertions.assertEquals(expected,real);
    }
}