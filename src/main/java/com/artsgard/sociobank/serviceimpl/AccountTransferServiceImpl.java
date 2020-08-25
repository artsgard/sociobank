package com.artsgard.sociobank.serviceimpl;

import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.dto.CurrencyDTO;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.model.AccountTransfer;
import com.artsgard.sociobank.repository.AccountRepository;
import com.artsgard.sociobank.repository.AccountTransferRepository;
import com.artsgard.sociobank.service.AccountTransferService;
import com.artsgard.sociobank.service.MapperService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author artsgard
 */
@Service
public class AccountTransferServiceImpl implements AccountTransferService {

    org.slf4j.Logger logger = LoggerFactory.getLogger(AccountTransferServiceImpl.class);

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private MapperService map;

    @Autowired
    private AccountTransferRepository accountTransferRepository;
    
    @Autowired
    ConvertCurrencyExternalService converterService;

    @Override
    public List<AccountTransferDTO> findAllAccountTransfers() {
        List<AccountTransferDTO> list = new ArrayList();
        accountTransferRepository.findAll().forEach(at -> {
            list.add(map.mapAccountTransferToAccountTransferDTO(at));
        });
        return list;
    }

    @Override
    public AccountTransferDTO findAccountTransferByIds(Long accountId, Long accountTransferId) {
        AccountTransfer tran = accountTransferRepository.getByAccountIdAndAccountTransferId(accountId, accountTransferId);
        return map.mapAccountTransferToAccountTransferDTO(tran);
    }

    @Override
    public List<AccountTransferDTO> findAccountTransfersByAccountId(Long accountId) {
        List<AccountTransfer> trans = accountTransferRepository.findByAccountId(accountId);
        List<AccountTransferDTO> list = new ArrayList();
        for (AccountTransfer tran : trans) {
            list.add(map.mapAccountTransferToAccountTransferDTO(tran));
        }
        return list;
    }

    @Override
    public List<AccountTransferDTO> findAccountTransfersByIban(String iban) {
        List<AccountTransfer> trans = accountTransferRepository.findByIban(iban);
        List<AccountTransferDTO> list = new ArrayList();
        for (AccountTransfer tran : trans) {
            list.add(map.mapAccountTransferToAccountTransferDTO(tran));
        }
        return list;
    }

    @Override
    public List<AccountTransferDTO> findAccountTransfersByUsername(String username) {
        List<AccountTransfer> trans = accountTransferRepository.findByUsername(username);
        List<AccountTransferDTO> list = new ArrayList();
        for (AccountTransfer tran : trans) {
            list.add(map.mapAccountTransferToAccountTransferDTO(tran));
        }
        return list;
    }

    @Override
    public AccountTransferDTO saveAccountTransfer(AccountTransferDTO transferDTO) {
        Optional<Account> optAccount1 = accountRepo.findByIban(transferDTO.getIbanResource());
        Optional<Account> optAccount2 = accountRepo.findByIban(transferDTO.getIbanDestiny());

        if (optAccount1.isPresent() && optAccount2.isPresent()) {
            Account acc1 = optAccount1.get();
            Account acc2 = optAccount2.get();
            AccountTransfer tran = new AccountTransfer(acc1.getId(), acc2.getId(), acc1,
                    acc2, transferDTO.getAmount(), transferDTO.getDescription(), new Date());
            
            transactionService(tran);
            
            AccountTransfer transf = accountTransferRepository.save(tran);
            AccountTransferDTO transfDTO = map.mapAccountTransferToAccountTransferDTO(transf);
            transfDTO.setIbanResource(transferDTO.getIbanResource());
            transfDTO.setIbanDestiny(transferDTO.getIbanDestiny());
            return transfDTO;
        } else {
            return null;
        }
    }

    @Override
    public void transactionService(AccountTransfer transfer) {
        Account acc1 = transfer.getAccount();
        Account acc2 = transfer.getAccountTransfer();
        BigDecimal debit = transfer.getAmount();
        BigDecimal convert = convertion(acc1.getCurrency(), acc2.getCurrency());
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<convert: " + convert);
        BigDecimal credit = debit.multiply(convert);
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<credit: " + credit + "   / debit " + debit);
        acc1.setBalance(acc1.getBalance().subtract(debit));
        acc2.setBalance(acc2.getBalance().add(credit));
        accountRepo.save(acc1);
        accountRepo.save(acc2);
    }

    private BigDecimal convertion(String currency1, String currency2) {
        if (currency1.equals(currency2)) {
            return new BigDecimal("1");
        } else {
            // call to external currency converter service
            return getExchangeRate(currency1, currency2);
        }
    }
    
    private BigDecimal getExchangeRate(String currency1, String currency2) {
        CurrencyDTO dto = converterService.getConvertion(currency1, currency2);
        String rate = null;
        for (Map.Entry<String, String> entry : dto.getRates().entrySet()) {
            rate = entry.getValue();
        }
        return new BigDecimal(rate);
    }
}
