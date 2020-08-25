package com.artsgard.sociobank.service;

import com.artsgard.sociobank.dto.AccountDTO;
import com.artsgard.sociobank.model.Account;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 
 * @author WillemDragstra
 */
@Service
public interface AccountService  {
    List<AccountDTO> findAllAccounts();
    AccountDTO findAccountById(Long id); 
    AccountDTO findAccountByUsername(String username); 
    AccountDTO findAccountByIban(String iban);
    AccountDTO saveAccount(AccountDTO account);
    AccountDTO updateAccount(AccountDTO account);
    void deleteAccountById(Long id);
    boolean hasAccountById(Long id);
    boolean hasAccountByIban(String iban);
}