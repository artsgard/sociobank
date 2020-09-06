package com.artsgard.sociobank.writer;

import com.artsgard.sociobank.model.AccountTransfer;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.artsgard.sociobank.repository.AccountTransferRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author artsgard
 */

@Component
//@Transactional
public class TransferWriter implements ItemWriter<AccountTransfer> {
    
    @Autowired
    private AccountTransferRepository repo;
    
    @Override
    public void write(List<? extends AccountTransfer> transfers) throws Exception {
        repo.saveAll(transfers);
    }
}
