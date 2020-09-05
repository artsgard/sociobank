package com.artsgard.sociobank.serviceimpl;

import com.artsgard.sociobank.dto.AccountTransferDTO;
import com.artsgard.sociobank.dto.CurrencyDTO;
import com.artsgard.sociobank.exception.ResourceNotFoundException;
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
        List<AccountTransfer> transfers = accountTransferRepository.findAll();
        if (transfers.isEmpty()) {
            throw new ResourceNotFoundException("No account transfers present");
        } else {
            transfers.forEach(trans -> {
                list.add(addIban(trans));
            });
            return list;
        }
    }

    @Override
    public AccountTransferDTO findAccountTransferById(Long id) throws ResourceNotFoundException {
        Optional<AccountTransfer> trans = accountTransferRepository.getAccountTransferById(id);
        if (trans.isPresent()) {
            return addIban(trans.get());
        } else {
            throw new ResourceNotFoundException("No account transfer present with the id: " + id);
        }
    }

    @Override
    public AccountTransferDTO findAccountTransferByIds(Long accountSourceId, Long accountDestinyId) throws ResourceNotFoundException {
        Optional<AccountTransfer> trans = accountTransferRepository.getByAccountSourceIdAndAccountDestinyId(accountSourceId, accountDestinyId);
        if (trans.isPresent()) {
            return addIban(trans.get());
        } else {
            throw new ResourceNotFoundException("No account transfer present with the ids: " + accountSourceId + " / " + accountDestinyId);
        }
    }

    @Override
    public List<AccountTransferDTO> findAccountTransfersByAccountSourceId(Long id) throws ResourceNotFoundException {
        List<AccountTransfer> trans = accountTransferRepository.findByAccountSourceId(id);
        if (trans.isEmpty()) {
            throw new ResourceNotFoundException("No account transfers present with the id: " + id);
        } else {
            List<AccountTransferDTO> list = new ArrayList();
            trans.forEach(tran -> {
                list.add(addIban(tran));
            });
            return list;
        }
    }

    @Override
    public List<AccountTransferDTO> findAccountTransfersByIban(String iban) throws ResourceNotFoundException {
        List<AccountTransfer> trans = accountTransferRepository.findByIban(iban);
        if (trans.isEmpty()) {
            throw new ResourceNotFoundException("No account transfers present with iban: " + iban);
        } else {
            List<AccountTransferDTO> list = new ArrayList();
            trans.forEach(tran -> {
                list.add(addIban(tran));
            });
            return list;
        }
    }

    @Override
    public List<AccountTransferDTO> findAccountTransfersByUsername(String username) throws ResourceNotFoundException {
        List<AccountTransfer> trans = accountTransferRepository.findByUsername(username);
        if (trans.isEmpty()) {
            throw new ResourceNotFoundException("No account transfers present with user name: " + username);
        } else {
            List<AccountTransferDTO> list = new ArrayList();
            trans.forEach(tran -> {
                list.add(addIban(tran));
            });
            return list;
        }
    }

    @Override
    public AccountTransferDTO saveAccountTransfer(AccountTransferDTO transferDTO) throws ResourceNotFoundException {
        Optional<Account> optAccount1 = accountRepo.findByIban(transferDTO.getIbanSource());
        Optional<Account> optAccount2 = accountRepo.findByIban(transferDTO.getIbanDestiny());

        if (optAccount1.isPresent() && optAccount2.isPresent()) {
            Account acc1 = optAccount1.get();
            Account acc2 = optAccount2.get();
            AccountTransfer tran = new AccountTransfer(null, acc1, acc2, transferDTO.getAmount(), transferDTO.getDescription(), new Date());
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<source: " + tran.getAccountSource().getId());
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<destiny: " + tran.getAccountDestiny().getId());
            transactionService(tran);

            AccountTransfer transf = accountTransferRepository.save(tran);
            AccountTransferDTO transfDTO = map.mapAccountTransferToAccountTransferDTO(transf);
            transfDTO.setIbanSource(acc1.getIban());
            transfDTO.setIbanDestiny(acc2.getIban());
            return transfDTO;
        } else {
            throw new ResourceNotFoundException("No account transfer present with the ibans: "
                    + optAccount1.get().getIban() + " / " + optAccount2.get().getIban());
        }
    }

    @Override
    public void transactionService(AccountTransfer transfer) {
        Account acc1 = transfer.getAccountSource();
        Account acc2 = transfer.getAccountDestiny();
        BigDecimal debit = transfer.getAmount();
        BigDecimal convert = convertion(acc1.getCurrency(), acc2.getCurrency());
        //System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<convert: " + convert);
        BigDecimal credit = debit.multiply(convert);
        //System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<credit: " + credit + "   / debit " + debit);
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

    private AccountTransferDTO addIban(AccountTransfer trans) {
        AccountTransferDTO transDTO = map.mapAccountTransferToAccountTransferDTO(trans);
        transDTO.setIbanSource(trans.getAccountSource().getIban());
        transDTO.setIbanDestiny(trans.getAccountDestiny().getIban());
        return transDTO;
    }
}
