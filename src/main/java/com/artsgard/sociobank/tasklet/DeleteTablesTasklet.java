package com.artsgard.sociobank.tasklet;

import com.artsgard.sociobank.repository.AccountRepository;
import com.artsgard.sociobank.repository.AccountTransferRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteTablesTasklet implements Tasklet {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private AccountTransferRepository transferRepo;

    @Override
    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        if (!transferRepo.findAll().isEmpty()) {
             transferRepo.deleteAll();
        }
        if (!accountRepo.findAll().isEmpty()) {
            accountRepo.deleteAll();
        }
        return null;
    }
}
