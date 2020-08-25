package com.artsgard.sociobank;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author artsgard
 */
@SpringBootApplication
public class SocioBankApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SocioBankApplication.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("socio-account-job")
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(SocioBankApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("sociobank-date", new Date())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(job, jobParameters);
        log.info("execution.getStatus(): " + execution.getStatus());
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
