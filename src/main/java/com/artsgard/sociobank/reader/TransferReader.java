package com.artsgard.sociobank.reader;

import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.model.AccountTransfer;
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
public class TransferReader {
    
    @Autowired
    private AccountTransferFieldSetMapper transferMapper;
     
    public FlatFileItemReader read() {
        return new FlatFileItemReaderBuilder<AccountTransferDTO>()
                .name("socio-bank-transfer-reader")
                .resource(new FileSystemResource("transfers.csv"))
                .linesToSkip(0)
                .strict(true)
                .delimited().delimiter(";")
                .names(new String[]{"ibanResource", "ibanDestiny", "amount", "description", "transferDate"})
                .fieldSetMapper(transferMapper)
                //.targetType(AccountTransfer.class)//.customEditors((Map<Class<?>, PropertyEditor>) dateRegister)
                .build();
    }
}

 