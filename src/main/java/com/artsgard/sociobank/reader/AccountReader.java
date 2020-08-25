package com.artsgard.sociobank.reader;

import com.artsgard.sociobank.model.Account;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

/**
 *
 * @author artsgard
 */
@Component
public class AccountReader {
    
    @Autowired
    private AccountFieldSetMapper accountMapper;

    public FlatFileItemReader read() {
        return new FlatFileItemReaderBuilder<Account>()
                .linesToSkip(1)
                .name("socio-bank-reader")
                .resource(new FileSystemResource("accounts.csv"))
                .strict(true)
                .linesToSkip(0)
                .delimited().delimiter(";")
                .names(new String[]{"iban", "username", "balance", "currency", "creationDate", "active"})
                .fieldSetMapper(accountMapper)
                //.targetType(Account.class)
                .build();
    }
}
