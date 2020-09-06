package com.artsgard.sociobank.controller;

import com.artsgard.sociobank.SocioBankApplication;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author artsgard
 */  

@RestController
@RequestMapping("/load-db")
public class BatchController {
    
    private static final Logger log = LoggerFactory.getLogger(SocioBankApplication.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("socio-account-job")
    private Job job;

    @GetMapping()
    public String startBatch() throws Exception {
         JobParameters jobParameters = new JobParametersBuilder()
                .addDate("sociobank-date", new Date())
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
        log.info("Time of the Batcg start", dateFormat.format(new Date())); 
        return "db load started: " + jobParameters.getString("sociobank-date");
    }
}

