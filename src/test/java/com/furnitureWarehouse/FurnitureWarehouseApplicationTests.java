package com.furnitureWarehouse;

import com.furnitureWarehouse.config.FurnitureConfig;
import com.furnitureWarehouse.entities.Furniture;
import com.furnitureWarehouse.repositories.FurnitureRepo;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.CsvParser;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.io.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest
@SpringBatchTest
@TestExecutionListeners({
		StepScopeTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class
})
@DisplayName("E2E tests")
class FurnitureWarehouseApplicationTests {

	@Resource
	private Job furnitureJob;

	@Resource
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Resource
	private JobLauncher jobLauncher;

	@Resource
	private FlatFileItemReader<Furniture> furnitureItemReader;

	@Resource
	private RepositoryItemWriter<Furniture> furnitureItemWriter;

	 @Autowired
	FurnitureRepo furnitureRepo;

	@Test
	@DisplayName("Test that verifies that I am retrieving the data I need from the database and that it has the required format")
	public void testContentAndFormatDataReadAndWritten() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		// Ejecuta el trabajo
		JobExecution jobExecution = jobLauncher.run(furnitureJob, jobLauncherTestUtils.getUniqueJobParameters());

		// Verifica que el trabajo haya terminado con éxito
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

		// Obtiene los datos escritos por el escritor
		List<Furniture> writtenFurniture = furnitureRepo.findAll(Sort.by(Sort.Order.asc("id")));



		// Verifica que se hayan leído y escrito correctamente los datos
		List<Furniture> expectedFurniture = getExpectedFurniture();
		System.out.println("Cadena actual: " + writtenFurniture.get(0).getId());
		System.out.println("Cadena esperada: " + expectedFurniture.get(0).getId());
		assertEquals(expectedFurniture.size(), writtenFurniture.size());

		for (int i = 0; i < expectedFurniture.size(); i++) {
			Furniture expected = expectedFurniture.get(i);
			Furniture actual = writtenFurniture.get(i);

			// Comparar los datos esperados con los datos escritos
			assertEquals(expected.getId(), actual.getId());
			assertEquals(expected.getOwner().trim(), actual.getOwner().trim());
			assertEquals(expected.getType().trim(), actual.getType().trim());
			assertEquals(expected.getMaterial().trim(), actual.getMaterial().trim());
			assertEquals(expected.getYearsInStorage(), actual.getYearsInStorage());
			assertEquals(expected.isWarehouseOutput(), actual.isWarehouseOutput());
		}

	}

	private List<Furniture> getExpectedFurniture() {

		return List.of(
				new Furniture(1,"Irina","SOFA","cuero",10,false),
				new Furniture(2,"Conchi","MESA","cristal",2,true),
				new Furniture(3,"Candela","LAMPARA","tela",20,false),
				new Furniture(4,"Candela","ESTANTERIA","hierro",5,true)

		);
	}

	@Test
	@DisplayName("Test that verifies that the data type I receive from the csv matches the data type of the database.")
	public void testTypeOfDataReadAndWritten() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, IOException, CsvValidationException {
		JobExecution jobExecution = jobLauncher.run(furnitureJob, jobLauncherTestUtils.getUniqueJobParameters());


		// Verifica que el trabajo haya terminado con éxito
	assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());


		// Leer los datos del CSV
		List<Map<String, String>> csvData = readCsvData();

		// Leer los datos de la base de datos
		List<Map<String, Object>> dbData = readDataFromDatabase();

		// Comparar tipos de datos
		for (int i = 0; i < csvData.size(); i++) {
			Map<String, String> csvRow = csvData.get(i);
			Map<String, Object> dbRow = dbData.get(i);

			for (String columnName : csvRow.keySet()) {
				String csvValue = csvRow.get(columnName);
				Object dbValue = dbRow.get(columnName);

				if (csvValue != null && dbValue != null) {
					Class<?> csvType = csvValue.getClass();
					Class<?> dbType = dbValue.getClass();

					assertEquals("Tipo de datos en CSV y base de datos no coincide en la columna " + columnName,
							csvType, dbType);
				}
			}
		}



	}

	private List<Map<String,String>> readCsvData() throws IOException, CsvValidationException {

		List<Map<String, String>> csvData = new ArrayList<>();

		try (Reader reader = new InputStreamReader(new ClassPathResource("csv/poc.csv").getInputStream());
			 CSVReader csvReader = new CSVReader(reader)) {

			String[] header = csvReader.readNext();
			String[] line;

			while ((line = csvReader.readNext()) != null) {
				Map<String, String> row = new LinkedHashMap<>(); // LinkedHashMap para mantener el orden de las columnas

				for (int i = 0; i < header.length; i++) {
					row.put(header[i], line[i]);
				}

				csvData.add(row);
			}
		}

		return csvData;
	}
	private List<Map<String, Object>> readDataFromDatabase() {
		List<Map<String, Object>> dbData = new ArrayList<>();

		Iterable<Furniture> entities = furnitureRepo.findAll();

		for (Furniture entity : entities) {
			Map<String, Object> row = new LinkedHashMap<>();

			// Suponiendo que TuEntidad tiene atributos con nombres específicos que deseas incluir en el mapa
			row.put("owner", entity.getOwner());
			row.put("type", entity.getType());
			row.put("material", entity.getMaterial());
			row.put("yearsInStorage", entity.getYearsInStorage());
			row.put("warehouseOutput", entity.isWarehouseOutput());
			// Agrega más columnas según tus necesidades

			dbData.add(row);
		}

		return dbData;
	}
}
