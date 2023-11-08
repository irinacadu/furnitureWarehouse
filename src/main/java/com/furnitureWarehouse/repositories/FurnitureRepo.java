package com.furnitureWarehouse.repositories;

import com.furnitureWarehouse.entities.Furniture;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface FurnitureRepo extends CrudRepository<Furniture,Integer> {

    List<Furniture> findAll(Sort sort);

    @Query("SELECT f FROM Furniture f ORDER BY f.id ASC")
    List<Furniture> findAllOrderedByIdAsc();
}
