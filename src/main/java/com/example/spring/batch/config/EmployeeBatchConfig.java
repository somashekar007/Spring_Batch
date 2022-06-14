package com.example.spring.batch.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.example.spring.batch.model.Employee;
import com.example.spring.batch.model.EmployeeDTO;
import com.example.spring.batch.processor.EmployeeProcessor;

@Configuration
@EnableBatchProcessing

public class EmployeeBatchConfig {

	@Autowired
	private JobBuilderFactory job;
	@Autowired
	private StepBuilderFactory step;
	@Autowired
	private EmployeeProcessor processor;
	@Autowired
	private MongoTemplate template;

	@Bean

	public MongoItemReader<Employee> reader() {
		MongoItemReader<Employee> reader = new MongoItemReader<>();
		reader.setTemplate(template);
		reader.setQuery("{}");
		reader.setTargetType(Employee.class);
		reader.setSort(new HashMap<String, Sort.Direction>() {
			{
				put("_id", Direction.ASC);
			}
		});
		return reader;
	}

//	@Bean
//	public JsonFileItemWriter<Employee> writer()
//	{
//	
	// }
	@Bean
	@StepScope
	public JsonFileItemWriter<Employee> writer()   {
//		String path = File.createTempFile("trade",".json").getAbsolutePath();
//		System.out.println("path : "+path);
		return new JsonFileItemWriterBuilder<Employee>()
		        .name("tradeJsonFileItemWriter")
				.jsonObjectMarshaller(new JacksonJsonObjectMarshaller<Employee>())
				.resource(new FileSystemResource("OutputFiles/trades.json"))
				.build();
	}

//	@StepScope
//	@Bean
//	public JsonFileItemWriter<Employee> writer(
//			@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource) {
//		JsonFileItemWriter<Employee> jsonFileItemWriter = new JsonFileItemWriter<>(fileSystemResource, new JacksonJsonObjectMarshaller<Employee>());
//		
//		return jsonFileItemWriter;
//	}
//	public JsonFileItemWriter<Employee> writer(@Value("#{jobParameters['outputFile']}") FileSystemResource fileSystemResource IOException
//	{
//		JsonFileItemWriterBuilder<Employee> builder = new JsonFileItemWriterBuilder<>();
//		JacksonJsonObjectMarshaller<Employee> marshaller = new JacksonJsonObjectMarshaller<>();
//		return builder
//				.name("EmployeeJson")
//				.jsonObjectMarshaller(marshaller)
//				.resource(new FileSystemResource(output))
//				.build();
//		
//	}
	@Bean
	public Step step1() throws IOException {
		return step.get("step").<Employee, Employee>chunk(2).reader(reader()).processor(processor).writer(writer())
				.build();
	}

	@Bean
	public Job job() throws IOException {
		return job.get("job").incrementer(new RunIdIncrementer()).start(step1()).build();
	}

}
