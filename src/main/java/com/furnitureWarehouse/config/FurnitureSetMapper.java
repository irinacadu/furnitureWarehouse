package com.furnitureWarehouse.config;

import com.furnitureWarehouse.entities.Furniture;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import static com.furnitureWarehouse.entities.FurnitureFields.*;

@Component
@StepScope
public class FurnitureSetMapper implements FieldSetMapper<Furniture> {
    @Override
    public Furniture mapFieldSet(FieldSet fieldSet) throws BindException {

        return new Furniture(

                fieldSet.readString(ID.getIndex()),
                fieldSet.readString(OWNER.getIndex()),
                fieldSet.readString(TYPE_FURNITURE.getIndex()),
                fieldSet.readString(MATERIAL.getIndex()),
                fieldSet.readInt(YEARS_IN_STORAGE.getIndex()),
                fieldSet.readBoolean(WAREHOUSE_OUTPUT.getIndex())

       );
    }

}
