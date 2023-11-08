package com.furnitureWarehouse.config;

import com.furnitureWarehouse.entities.Furniture;
import com.furnitureWarehouse.processes.FurnitureProcessor;
import com.furnitureWarehouse.repositories.FurnitureRepo;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;

import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;

import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@SpringBatchTest
@ExtendWith(MockitoExtension.class)//mockito para que me cree instancias simuladas
@DisplayName("Clase que testea FurnitureConfig")
public class FurnitureConfigTest {

    @Mock
    FurnitureRepo furnitureRepo;

    @Mock
    FlatFileItemReader<Furniture>furnitureFlatFileItemReader;

    @Mock
    DefaultLineMapper<Furniture> furnitureDefaultLineMapper;

    @Test
    @DisplayName("Test reader")
    public void testFurnitureReader(){
        furnitureFlatFileItemReader.setResource(new ClassPathResource("csv/poc.csv"));
        furnitureFlatFileItemReader.setLineMapper(furnitureDefaultLineMapper);


        try {
            furnitureFlatFileItemReader.open(new ExecutionContext());
            furnitureFlatFileItemReader.read();
            furnitureFlatFileItemReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Se produjo una excepción al leer el archivo.");
        }


    }

    @Test
    @DisplayName("Test writer")
    public void testFurnitureWriter(){

        FurnitureConfig furnitureConfig = new FurnitureConfig();
        RepositoryItemWriter<Furniture> repositoryItemWriter = furnitureConfig.furnitureRepositoryItemWriter(furnitureRepo);
        Assertions.assertNotNull(repositoryItemWriter);

    }

    @Test
    @DisplayName("Test Step")
    public void testFurnitureStep(){

        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        FieldSetMapper<Furniture> furnitureSetMapper = mock(FieldSetMapper.class);

        FurnitureProcessor furnitureProcessor = new FurnitureProcessor();

        FurnitureConfig furnitureConfig = new FurnitureConfig();


        Step step = furnitureConfig.furnitureStep(jobRepository, transactionManager,
                furnitureConfig.furnitureFlatFileItemReader(furnitureSetMapper), furnitureProcessor, furnitureConfig.furnitureRepositoryItemWriter(furnitureRepo));

        Assertions.assertNotNull(step);
    }
    @Test
    @DisplayName("Test Job")
    public void testFurnitureJob(){
        FurnitureConfig furnitureConfig = new FurnitureConfig();
        JobRepository jobRepository = mock(JobRepository.class);
        Step stepFurniture = mock(Step.class);
       Job job = furnitureConfig.furnitureJob(jobRepository,stepFurniture);
       Assertions.assertNotNull(job);
    }
    }

