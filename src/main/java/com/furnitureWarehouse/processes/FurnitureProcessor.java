package com.furnitureWarehouse.processes;

import com.furnitureWarehouse.entities.Furniture;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class FurnitureProcessor implements ItemProcessor<Furniture,Furniture> {
    @Override
    public Furniture process(Furniture furniture) throws Exception {
        String type = furniture.getType();
        String material = furniture.getMaterial();

        int yearsInWarehouse = furniture.getYearsInStorage();

            if (type != null && material != null) {
                furniture.setType(type.toUpperCase());
                furniture.setType(type.toUpperCase());
            }

            if(yearsInWarehouse >= 10){
                furniture.setWarehouseOutput(false);
            }


        return furniture;
    }
}
