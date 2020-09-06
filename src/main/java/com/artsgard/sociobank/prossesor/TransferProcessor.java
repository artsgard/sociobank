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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author artsgard
 */
@Component
//@Transactional
public class TransferProcessor implements ItemProcessor<AccountTransferDTO, AccountTransfer> {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private AccountTransferService transferService;

    @Qualifier("dbTransactionManager")
    private PlatformTransactionManager transactionManager;

    @Override
    public AccountTransfer process(AccountTransferDTO transfer) throws Exception {
        Optional<Account> optAccount1 = accountRepo.findByIban(transfer.getIbanSource());
        Optional<Account> optAccount2 = accountRepo.findByIban(transfer.getIbanDestiny());

        if (optAccount1.isPresent() && optAccount2.isPresent()) {
            Account acc1 = optAccount1.get();
            Account acc2 = optAccount2.get();
            AccountTransfer tran = new AccountTransfer(null, acc1,
                    acc2, transfer.getAmount(), transfer.getDescription(), new Date());

            BigDecimal debit = transfer.getAmount();
            BigDecimal convert = transferService.convertion(acc1.getCurrency(), acc2.getCurrency());
            BigDecimal credit = debit.multiply(convert);
            acc1.setBalance(acc1.getBalance().subtract(debit));
            acc2.setBalance(acc2.getBalance().add(credit));
            List<Account> list = new ArrayList();
            list.add(acc1);
            list.add(acc2);
            accountRepo.saveAll(list);
            return tran;
        } else {
            throw new ResourceNotFoundException("No accounts found with the ibans: " + transfer.getIbanSource() + "  /  " + transfer.getIbanDestiny());
        }
    }
}
