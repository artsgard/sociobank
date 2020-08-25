package com.artsgard.sociobank.serviceimpl;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.service.MapperService;
import com.artsgard.sociobank.exception.ResourceNotFoundException;
import com.artsgard.sociobank.model.Account;
import com.artsgard.sociobank.repository.AccountRepository;
import com.artsgard.sociobank.service.AccountService;;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author WillemDragstra
 */
@Service
public class AccountServiceImpl implements AccountService {
    
    org.slf4j.Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    
    @Autowired
    private AccountRepository repo;
    
    @Autowired
    private MapperService map;
    
    public AccountServiceImpl() {
    }
    
    @Override
    public List<AccountDTO> findAllAccounts() {
        List<AccountDTO> list = new ArrayList();
        repo.findAll().forEach(acc -> {
            list.add(map.mapAccountToAccountDTO(acc));
        });
        return list;
    }
    
    @Override
    public AccountDTO findAccountById(Long id) {
        Optional<Account> optAccount = repo.findById(id);
        if (optAccount.isPresent()) {
            return map.mapAccountToAccountDTO(optAccount.get());
        } else {
            return null;
        } 
    }
    
    @Override
    public AccountDTO findAccountByUsername(String username) {
        Optional<Account> optAccount = repo.findByUsername(username);
        if (optAccount.isPresent()) {
            return map.mapAccountToAccountDTO(optAccount.get());
        } else {
            return null;
        } 
    }
    
    @Override
    public AccountDTO findAccountByIban(String iban) {
        Optional<Account> optAccount = repo.findByIban(iban);
        if (optAccount.isPresent()) {
            return map.mapAccountToAccountDTO(optAccount.get());
        } else {
            return null;
        } 
    }
    
    @Override
    public AccountDTO saveAccount(AccountDTO accountDTO) {
        Account account = map.mapAccountDTOToAccount(accountDTO);
        account.setCreationDate(new Date());
        account.setActive(Boolean.TRUE);
        return map.mapAccountToAccountDTO(repo.save(account));
    }
    
    @Override
    public AccountDTO updateAccount(AccountDTO accountDTO) {
        
        Optional<Account> optAccount = repo.findById(accountDTO.getId());
        if (optAccount.isPresent()) {
            Account account = optAccount.get();
            
            if (accountDTO.getUsername() == null) {
                accountDTO.setUsername(account.getUsername());
            }
            
            if (accountDTO.getIban() == null) {
                accountDTO.setIban(account.getIban());
            }
            
            if (accountDTO.getBalance() == null) {
                accountDTO.setBalance(account.getBalance());
            }
            
            if (accountDTO.getCurrency() == null) {
                accountDTO.setCurrency(account.getCurrency());
            }
            accountDTO.setCreationDate(account.getCreationDate());
            
            account = repo.save(map.mapAccountDTOToAccount(accountDTO));
            
            return map.mapAccountToAccountDTO(account);
        } else {
            throw new ResourceNotFoundException("Account not found with id: " + accountDTO.getId());
        }
    }
    
    @Override
    public void deleteAccountById(Long id) {
        Optional<Account> optAccount = repo.findById(id);
        if (optAccount.isPresent()) {
            repo.delete(optAccount.get());
        } else {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
    }
    
    @Override
    public boolean hasAccountById(Long id) {
        Optional<Account> optAccount = repo.findById(id);
        if (optAccount.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean hasAccountByIban(String iban) {
        Optional<Account> optAccount = repo.findByIban(iban);
        if (optAccount.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
    
}
