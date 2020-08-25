package com.artsgard.sociobank.writer;

import com.artsgard.sociobank.model.Account;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.artsgard.sociobank.repository.AccountRepository;

/**
 *
 * @author artsgard
 */

@Component
public class AccountWriter implements ItemWriter<Account> {
    
    @Autowired
    private AccountRepository repo;
    
    @Override
    public void write(List<? extends Account> accounts) throws Exception {
        repo.saveAll(accounts);
    }
}
