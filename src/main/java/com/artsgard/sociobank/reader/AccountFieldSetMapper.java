package com.artsgard.sociobank.reader;

import com.artsgard.sociobank.model.Account;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

/**
 *
 * @author artsgard
 */
@Component
public class AccountFieldSetMapper implements FieldSetMapper {

    @Override
    public Account mapFieldSet(FieldSet fieldSet) {
        Account account = new Account();
        account.setIban(fieldSet.readString("iban"));
        account.setUsername(fieldSet.readString("username"));
        account.setBalance(fieldSet.readBigDecimal("balance"));
        account.setCurrency(fieldSet.readString("currency"));
        account.setCreationDate(fieldSet.readDate("creationDate"));
        account.setActive(fieldSet.readBoolean("active"));
        return account;
    }
}
