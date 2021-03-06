package com.artsgard.sociobank.config;

import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;
import com.artsgard.sociobank.prossesor.TransferProcessor;
import com.artsgard.sociobank.reader.AccountReader;
import com.artsgard.sociobank.reader.TransferReader;
import com.artsgard.sociobank.tasklet.DeleteTablesTasklet;
import com.artsgard.sociobank.writer.AccountWriter;
import com.artsgard.sociobank.writer.TransferWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author artsgard
 */
@Configuration
@EnableBatchProcessing
public class BatchFlowConfig {
    
    @Autowired
    @Qualifier("dbTransactionManager") 
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    DeleteTablesTasklet tasklet;
    
    @Autowired
    private JobBuilderFactory jobBuilders;
    
    @Autowired
    AccountStepListener acountListener;

    @Autowired
    private StepBuilderFactory stepBuilders;
    
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AccountReader accountReader;

    @Autowired
    private AccountWriter accountWriter;
    
    @Autowired
    private TransferProcessor transferProcessor;

    @Autowired
    private TransferReader transferReader;

    @Autowired
    private TransferWriter transferWriter;

    @Bean(name = "socio-account-job")
    public Job accountJob() throws Exception {
        return jobBuilders.get("batchAccountJob")
                .repository(jobRepository)
                .start(deleteTablesContentStep())
                .next(accountStep())
                .next(transferStep())
                .build();
    }
    
    @Bean
    public Step deleteTablesContentStep() throws Exception {
        return stepBuilders.get("delete-tables-tasklet")
                .tasklet(tasklet)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Step accountStep() throws Exception {
        return stepBuilders.get("batch-account-step")
                .transactionManager(transactionManager)
                .<Account, Account>chunk(20)
                .reader(accountReader.read())
                .writer(accountWriter)
                .listener(acountListener)
                .build();
    }
    
    @Bean
    public Step transferStep() throws Exception {
        return stepBuilders.get("batch-transfer-step")
                .transactionManager(transactionManager)
                .<AccountTransfer, AccountTransfer>chunk(20)
                .reader(transferReader.read())
                .processor(transferProcessor)
                .writer(transferWriter)
                .build();
    }
    
}
