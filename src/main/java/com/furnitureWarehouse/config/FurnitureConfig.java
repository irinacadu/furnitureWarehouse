package com.furnitureWarehouse.config;

import com.furnitureWarehouse.entities.Furniture;
import com.furnitureWarehouse.repositories.FurnitureRepo;
import com.furnitureWarehouse.processes.FurnitureProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;


import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class FurnitureConfig {
    private static final Logger log = LoggerFactory.getLogger(FurnitureConfig.class);

    @Bean
    public Job furnitureJob(JobRepository jobRepository,
                            Step furnitureStep){
        return new JobBuilder("furniture-job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(furnitureStep)
                .end()
                .build();
    }

    @Bean
    public Step furnitureStep(JobRepository jobRepository,
                              PlatformTransactionManager transactionManager, FlatFileItemReader<Furniture>furnitureFlatFileItemReader,
                              FurnitureProcessor furnitureProcessor, RepositoryItemWriter<Furniture>furnitureRepositoryItemWriter){
        return new StepBuilder("furniture-step", jobRepository)
                .<Furniture, Furniture>chunk(2, transactionManager)
                .reader(furnitureFlatFileItemReader)
                .processor(furnitureProcessor)
                .writer(furnitureRepositoryItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Furniture> furnitureFlatFileItemReader(FieldSetMapper<Furniture>furnitureSetMapper) {
        log.info("entra reader");
        DelimitedLineTokenizer furnitureLineTokenizer = new DelimitedLineTokenizer();
        furnitureLineTokenizer.setDelimiter(",");
        furnitureLineTokenizer.setStrict(false);
        furnitureLineTokenizer.setNames("id","owner","type","material","yearsInStorage","warehouseOutput");
        furnitureLineTokenizer.setIncludedFields(0,1,2,3,4,5); //esto me faltaba

        DefaultLineMapper<Furniture> furnitureDefaultLineMapper = new DefaultLineMapper<>();
        furnitureDefaultLineMapper.setLineTokenizer(furnitureLineTokenizer);
        furnitureDefaultLineMapper.setFieldSetMapper(furnitureSetMapper); //esto me faltaba

        FlatFileItemReader <Furniture> furnitureFlatFileItemReader = new FlatFileItemReader<>();
        furnitureFlatFileItemReader.setResource(new ClassPathResource("csv/almacen.csv"));//esto lo tenía en otro sitio;
        furnitureFlatFileItemReader.setLineMapper(furnitureDefaultLineMapper);

        return furnitureFlatFileItemReader;
    }


    @Bean
    @StepScope
    public RepositoryItemWriter<Furniture> furnitureRepositoryItemWriter (FurnitureRepo furnitureRepo){

        log.info("entra writer");
        RepositoryItemWriter<Furniture> furnitureWriter = new RepositoryItemWriter<>();
        furnitureWriter.setRepository(furnitureRepo);
        return furnitureWriter;
    }

}
