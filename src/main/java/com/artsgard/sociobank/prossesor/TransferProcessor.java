package com.artsgard.sociobank.prossesor;

import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.ItemProcessor;
import com.artsgard.sociobank.repository.AccountRepository;
import com.artsgard.sociobank.repository.AccountTransferRepository;
import com.artsgard.sociobank.service.AccountTransferService;
import java.util.Date;
import java.util.Optional;

/**
 *
 * @author artsgard
 */
@Component
public class TransferProcessor implements ItemProcessor<AccountTransferDTO, AccountTransfer> {

    @Autowired
    private AccountTransferRepository transferRepo;
    
    @Autowired
    private AccountRepository accountRepo;
    
    @Autowired
    private AccountTransferService transferService;
    

    @Override
    public AccountTransfer process(AccountTransferDTO transferDTO) throws Exception {
        Optional<Account> optAccount1 = accountRepo.findByIban(transferDTO.getIbanResource());
        Optional<Account> optAccount2 = accountRepo.findByIban(transferDTO.getIbanDestiny());
        
         if (optAccount1.isPresent() && optAccount2.isPresent()) {
            Account acc1 = optAccount1.get();
            Account acc2 = optAccount2.get();
            AccountTransfer tran = new AccountTransfer(acc1.getId(), acc2.getId(), acc1,
                    acc2, transferDTO.getAmount(), transferDTO.getDescription(), new Date());
            
            transferService.transactionService(tran);
            return tran;
        } else {
            throw new ResourceNotFoundException("No accounts found with the ibans: " + transferDTO.getIbanResource() + "  /  " + transferDTO.getIbanDestiny());
        }
    }
}