package com.furnitureWarehouse.processes;

import com.furnitureWarehouse.entities.Furniture;
import com.furnitureWarehouse.repositories.FurnitureRepo;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class FurnitureProcessor implements ItemProcessor<Furniture,Furniture> {

    @Autowired
    FurnitureRepo furnitureRepo;
    @Override
    public Furniture process(Furniture furniture) throws Exception {
        String type = furniture.getType().trim();
        String material = furniture.getMaterial().trim();
        Furniture existingFurniture = furnitureRepo.findById(furniture.getId()).orElse(null);
        int yearsInWarehouse = furniture.getYearsInStorage();

        if(existingFurniture!= null || existingFurniture == null){

            if (type != null && material != null) {
                furniture.setId(furniture.getId());
                furniture.setType(type.toUpperCase().trim());
                furniture.setType(type.toUpperCase().trim());
            }

            if(yearsInWarehouse >= 10){
                furniture.setWarehouseOutput(false);
            }
        }




        return furniture;
    }
}
