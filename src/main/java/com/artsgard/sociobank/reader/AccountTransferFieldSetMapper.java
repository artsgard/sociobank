package com.artsgard.sociobank.reader;

import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.model.AccountTransfer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

/**
 *
 * @author artsgard
 */
@Component
public class AccountTransferFieldSetMapper implements FieldSetMapper {
    
    @Override
    public AccountTransferDTO mapFieldSet(FieldSet fieldSet) {
        AccountTransferDTO transfer = new  AccountTransferDTO();
        transfer.setIbanResource(fieldSet.readString("ibanResource"));
        transfer.setIbanDestiny(fieldSet.readString("ibanDestiny"));
        transfer.setAmount(fieldSet.readBigDecimal("amount"));
        transfer.setDescription(fieldSet.readString("description"));
        transfer.setTransferDate(fieldSet.readDate("transferDate"));
        return transfer;
    }
}