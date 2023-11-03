package com.furnitureWarehouse.repositories;

import com.furnitureWarehouse.entities.Furniture;
import org.springframework.data.repository.CrudRepository;


public interface FurnitureRepo extends CrudRepository<Furniture,String> {
}
