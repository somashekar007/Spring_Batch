package com.example.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchMongodbToJsonApplication implements CommandLineRunner  {
	@Autowired
	private Job job;

	@Autowired
	private JobLauncher jobLauncher;
	

	

	
	public static void main(String[] args) {
		SpringApplication.run(SpringBatchMongodbToJsonApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		JobParameters prams = new JobParametersBuilder().addLong("StartAt", System.currentTimeMillis())
			.toJobParameters();
		jobLauncher.run(job, prams);
	}
}
