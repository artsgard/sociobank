package com.artsgard.sociobank.prossesor;

import com.artsgard.sociobank.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.ItemProcessor;
import com.artsgard.sociobank.repository.AccountRepository;

/**
 *
 * @author artsgard
 */
@Component
public class AccountProcessor implements ItemProcessor<Account, Account> {

    @Autowired
    private AccountRepository repo;

    @Override
    public Account process(Account account) throws Exception {
       
        return account;
    }
}